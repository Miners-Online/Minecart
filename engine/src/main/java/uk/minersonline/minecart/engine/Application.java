package uk.minersonline.minecart.engine;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class Application {
	private Window window;

	public void run(WindowProperties properties) {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init(properties);
		loop();

		window.destroy();

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init(WindowProperties properties) {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		window = Window.buildWindow(properties);
		window.center();
		window.makeCurrent();
	}

	private void loop() {
		GL.createCapabilities();
		while (!window.shouldClose()) {
			this.loopEntry(window);
			window.loop();
		}
	}

	protected abstract void loopEntry(Window window);
}