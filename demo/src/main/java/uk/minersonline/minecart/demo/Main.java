package uk.minersonline.minecart.demo;

import org.joml.Vector3f;
import org.joml.Vector4f;
import uk.minersonline.minecart.engine.Application;
import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.objects.Entity;
import uk.minersonline.minecart.engine.scene.objects.Mesh;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;
import uk.minersonline.minecart.engine.render.Render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements Application {
	private Entity cubeEntity;
	private final Vector4f displInc = new Vector4f();
	private float rotation;

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
				// VO
				-0.5f, 0.5f, 0.5f,
				// V1
				-0.5f, -0.5f, 0.5f,
				// V2
				0.5f, -0.5f, 0.5f,
				// V3
				0.5f, 0.5f, 0.5f,
				// V4
				-0.5f, 0.5f, -0.5f,
				// V5
				0.5f, 0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f,
		};
		float[] colors = new float[]{
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
				0.5f, 0.0f, 0.0f,
				0.0f, 0.5f, 0.0f,
				0.0f, 0.0f, 0.5f,
				0.0f, 0.5f, 0.5f,
		};
		int[] indices = new int[]{
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				4, 0, 3, 5, 4, 3,
				// Right face
				3, 2, 7, 5, 3, 7,
				// Left face
				6, 1, 0, 6, 0, 4,
				// Bottom face
				2, 1, 6, 2, 6, 7,
				// Back face
				7, 6, 4, 7, 4, 5,
		};
		List<Mesh> meshList = new ArrayList<>();
		Mesh mesh = new Mesh(positions, colors, indices);
		meshList.add(mesh);
		String cubeModelId = "cube-model";
		Model model = new Model(cubeModelId, meshList);
		scene.addModel(model);

		cubeEntity = new Entity("cube-entity", cubeModelId);
		cubeEntity.setPosition(0, 0, -2);
		scene.addEntity(cubeEntity);
	}

	@Override
	public void input(Window window, Scene scene, long diffTimeMillis) {
		displInc.zero();
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			displInc.y = 1;
		} else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			displInc.y = -1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			displInc.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			displInc.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			displInc.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_Q)) {
			displInc.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {
			displInc.w = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			displInc.w = 1;
		}

		displInc.mul(diffTimeMillis / 1000.0f);

		Vector3f entityPos = cubeEntity.getPosition();
		cubeEntity.setPosition(displInc.x + entityPos.x, displInc.y + entityPos.y, displInc.z + entityPos.z);
		cubeEntity.setScale(cubeEntity.getScale() + displInc.w);
		cubeEntity.updateModelMatrix();
	}

	@Override
	public void update(Window window, Scene scene, long deltaTime) {
		rotation += 1.5f;
		if (rotation > 360) {
			rotation = 0;
		}
		cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
		cubeEntity.updateModelMatrix();
	}

	@Override
	public void destroy() {

	}
}