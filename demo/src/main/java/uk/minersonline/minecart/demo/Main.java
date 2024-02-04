package uk.minersonline.minecart.demo;

import org.joml.Vector4f;
import uk.minersonline.minecart.engine.Application;
import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.objects.Mesh;
import uk.minersonline.minecart.engine.utils.ColorUtils;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;
import uk.minersonline.minecart.engine.render.Render;

import java.awt.*;

public class Main implements Application {
	public static void main(String[] args) {
		Main main = new Main();
		WindowProperties properties = new WindowProperties();
		properties.title = "Demo App";
		properties.width = 300;
		properties.height = 300;
		Engine engine = new Engine(properties, main);
		engine.start();
	}

	@Override
	public void init(Window window, Scene scene, Render render) {
		window.setClearColor(Color.CYAN);
		window.center();

		float[] positions = new float[]{
				-0.5f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f,
				0.5f, 0.5f, 0.0f,
		};
		Vector4f a = ColorUtils.colorToOpenGL(Color.RED);
		Vector4f b = ColorUtils.colorToOpenGL(Color.GREEN);
		Vector4f c = ColorUtils.colorToOpenGL(Color.BLUE);
		Vector4f d = ColorUtils.colorToOpenGL(Color.YELLOW);
		float[] colors = new float[]{
				a.x, a.y, a.z,
				b.x, b.y, b.z,
				c.x, c.y, c.z,
				d.x, d.y, d.z,
		};
		int[] indices = new int[]{
				0, 1, 3, 3, 1, 2,
		};
		Mesh mesh = new Mesh(positions, colors, indices);
		scene.addMesh("quad", mesh);
	}

	@Override
	public void input(Window window, Scene scene, long deltaTime) {

	}

	@Override
	public void update(Window window, Scene scene, long deltaTime) {

	}

	@Override
	public void destroy() {

	}
}