package uk.minersonline.minecart.engine.render;

import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MainRenderer implements Destroyable, Resizeable {
	private final List<Renderer> renderers = new ArrayList<>();

	public void addRenderer(Renderer renderer) {
		renderers.add(renderer);
	}

	@Override
	public void destroy() {
		renderers.forEach(Renderer::destroy);
	}

	public void render(Window window, Scene scene) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glViewport(0, 0, window.getWidth(), window.getHeight());
		renderers.forEach((renderer) -> renderer.render(scene));
	}

	@Override
	public void resize(int width, int height) {
		for (Renderer renderer : renderers) {
			if (renderer instanceof Resizeable resizeable) {
				resizeable.resize(width, height);
			}
		}
	}
}
