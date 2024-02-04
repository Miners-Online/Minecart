package uk.minersonline.minecart.demo;

import uk.minersonline.minecart.engine.Application;
import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.objects.Mesh;
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
				0.0f, 0.5f, 0.0f,
				-0.5f, -0.5f, 0.0f,
				0.5f, -0.5f, 0.0f
		};
		Mesh mesh = new Mesh(positions, 3);
		scene.addMesh("triangle", mesh);
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