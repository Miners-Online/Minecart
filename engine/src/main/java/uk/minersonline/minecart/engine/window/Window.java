package uk.minersonline.minecart.engine.window;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.ColorUtils;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.input.MouseInput;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Destroyable {
	private final long handle;
	private final WindowProperties properties;
	private final Callable<Void> resizeFunc;
	private final MouseInput mouseInput;

	private final ImGuiImplGlfw imGuiGlfw;
	private final ImGuiImplGl3 imGuiGl3;

	private Window(long handle, WindowProperties properties, Callable<Void> resizeFunc) {
		this.resizeFunc = resizeFunc;
		this.handle = handle;
		this.properties = properties;
		this.mouseInput = new MouseInput(handle);
		this.imGuiGl3 = new ImGuiImplGl3();
		this.imGuiGlfw = new ImGuiImplGlfw();
	}

	public int getWidth() {
		return properties.width;
	}

	public int getHeight() {
		return properties.height;
	}

	public MouseInput getMouseInput() {
		return mouseInput;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(handle, keyCode) == GLFW_PRESS;
	}

	public void setClearColor(Color color) {
		Vector4f GLColor = ColorUtils.colorToOpenGL(color);
		glClearColor(GLColor.x, GLColor.y, GLColor.z, GLColor.w);
	}

	public void center() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(this.handle, pWidth, pHeight);

			GLFWVidMode vidMode= glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
					this.handle,
					(vidMode.width() - pWidth.get(0)) / 2,
					(vidMode.height() - pHeight.get(0)) / 2
			);
		}
	}

	@Override
	public void destroy() {
		imGuiGl3.dispose();
		imGuiGlfw.dispose();
		ImGui.destroyContext();
		glfwFreeCallbacks(this.handle);
		glfwDestroyWindow(this.handle);
	}

	protected void resized(int width, int height) {
		this.properties.width = width;
		this.properties.height = height;
		try {
			resizeFunc.call();
		} catch (Exception excp) {
			System.err.printf("Error calling resize callback %s", excp);
		}
	}

	public static Window buildWindow(WindowProperties properties, Callable<Void> resizeFunc) {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		if (properties.compatibleProfile) {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
		} else {
			glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		}

		if (!(properties.width > 0 && properties.height > 0)) {
			glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
			GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			properties.width = vidMode.width();
			properties.height = vidMode.height();
		}

		long windowHandle = glfwCreateWindow(properties.width, properties.height, properties.title, NULL, NULL);
		if (windowHandle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		Window window = new Window(windowHandle, properties, resizeFunc);

		glfwSetFramebufferSizeCallback(windowHandle, (_w, w, h) -> window.resized(w, h));

		glfwSetErrorCallback((int errorCode, long msgPtr) ->
				System.err.printf("Error code [%s], msg [%s]%n", errorCode, MemoryUtil.memUTF8(msgPtr))
		);

		glfwMakeContextCurrent(windowHandle);

		if (properties.vsync) {
			if (properties.fps > 0) {
				glfwSwapInterval(0);
			} else {
				glfwSwapInterval(1);
			}
		}

		glfwShowWindow(windowHandle);

		int[] arrWidth = new int[1];
		int[] arrHeight = new int[1];
		glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight);
		properties.width = arrWidth[0];
		properties.height = arrHeight[0];

		GL.createCapabilities();
		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.setIniFilename(null);
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
		io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
		window.imGuiGlfw.init(window.handle, true);
		window.imGuiGl3.init("#version 130");
		return window;
	}

	public long getWindowHandle() {
		return handle;
	}

	public void render(Scene scene) {
		imGuiGlfw.newFrame();
		ImGui.newFrame();

		scene.getGuiInstance().drawGui(scene);

		ImGui.render();
		imGuiGl3.renderDrawData(ImGui.getDrawData());

		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();
			org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(backupWindowPtr);
		}

		GLFW.glfwSwapBuffers(handle);
		GLFW.glfwPollEvents();
	}
}
