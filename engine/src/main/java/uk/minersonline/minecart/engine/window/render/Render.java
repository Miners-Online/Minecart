package uk.minersonline.minecart.engine.window.render;

import org.lwjgl.opengl.GL;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;

import static org.lwjgl.opengl.GL11.*;

public class Render implements Destroyable {
	public Render() {
		GL.createCapabilities();
	}

	@Override
	public void destroy() {
		// Nothing to be done here yet
	}

	public void render(Window window, Scene scene) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}
