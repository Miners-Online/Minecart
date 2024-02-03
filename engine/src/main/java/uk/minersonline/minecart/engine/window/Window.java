package uk.minersonline.minecart.engine.window;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private final long handle;

	private final WindowProperties properties;

	private Window(long handle, WindowProperties properties) {
		this.handle = handle;
		this.properties = properties;
	}

	public long getHandle() {
		return handle;
	}

	public WindowProperties getProperties() {
		return properties;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(handle);
	}

	public void loop() {
		glfwSwapBuffers(handle);
		glfwPollEvents();
	}

	public void clear(Color color) {
		float r=(1.0f/255)*color.getRed();
		float g=(1.0f/255)*color.getGreen();
		float b=(1.0f/255)*color.getBlue();
		float a=(1.0f/255)*color.getAlpha();
		glClearColor(r, g, b, a);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void center() {
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(this.handle, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
					this.handle,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}
	}

	public void makeCurrent() {
		glfwMakeContextCurrent(handle);
		glfwSwapInterval(1);
		glfwShowWindow(handle);
	}

	public void destroy() {
		glfwFreeCallbacks(this.handle);
		glfwDestroyWindow(this.handle);
	}

	public static Window buildWindow(WindowProperties properties) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		long handle = glfwCreateWindow(properties.getWidth(), properties.getHeight(), properties.getTitle(), NULL, NULL);
		if (handle == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		glfwSetKeyCallback(handle, (_w, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(handle, true);
		});

		return new Window(handle, properties);
	}
}
