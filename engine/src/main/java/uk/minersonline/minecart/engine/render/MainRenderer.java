package uk.minersonline.minecart.engine.render;

import org.lwjgl.opengl.GL;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MainRenderer implements Destroyable {
	private final List<Renderer> renderers = new ArrayList<>();

	public MainRenderer() {
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
	}

	public void addRenderer(Renderer renderer) {
		renderers.add(renderer);
	}

	@Override
	public void destroy() {
		renderers.forEach(Renderer::destroy);
	}

	public void render(Window window, Scene scene) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		WindowProperties properties = window.getProperties();
		glViewport(0, 0, properties.width, properties.height);
		renderers.forEach((renderer) -> renderer.render(scene));
	}
}
