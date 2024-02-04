package uk.minersonline.minecart.engine.render;

import org.lwjgl.opengl.GL;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.SceneRender;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;

import static org.lwjgl.opengl.GL11.*;

public class Render implements Destroyable {
	private final SceneRender sceneRender;

	public Render() {
		GL.createCapabilities();
		glEnable(GL_DEPTH_TEST);
		sceneRender = new SceneRender();
	}

	@Override
	public void destroy() {
		sceneRender.destroy();
	}

	public void render(Window window, Scene scene) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		WindowProperties properties = window.getProperties();
		glViewport(0, 0, properties.width, properties.height);
		sceneRender.render(scene);
	}
}
