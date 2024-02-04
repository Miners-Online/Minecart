package uk.minersonline.minecart.demo;

import org.joml.Vector3f;
import org.joml.Vector4f;
import uk.minersonline.minecart.engine.Application;
import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.render.objects.Texture;
import uk.minersonline.minecart.engine.scene.EntityRenderer;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.objects.Entity;
import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.scene.objects.Material;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;
import uk.minersonline.minecart.engine.render.MainRenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements Application {
	private Entity cubeEntity;
	private final Vector4f displacement = new Vector4f();
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
	public void init(Window window, Scene scene, MainRenderer renderer) {
		window.setClearColor(Color.CYAN);
		window.center();

		float[] positions = new float[]{
				// V0
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

				// For text coords in top face
				// V8: V4 repeated
				-0.5f, 0.5f, -0.5f,
				// V9: V5 repeated
				0.5f, 0.5f, -0.5f,
				// V10: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V11: V3 repeated
				0.5f, 0.5f, 0.5f,

				// For text coords in right face
				// V12: V3 repeated
				0.5f, 0.5f, 0.5f,
				// V13: V2 repeated
				0.5f, -0.5f, 0.5f,

				// For text coords in left face
				// V14: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V15: V1 repeated
				-0.5f, -0.5f, 0.5f,

				// For text coords in bottom face
				// V16: V6 repeated
				-0.5f, -0.5f, -0.5f,
				// V17: V7 repeated
				0.5f, -0.5f, -0.5f,
				// V18: V1 repeated
				-0.5f, -0.5f, 0.5f,
				// V19: V2 repeated
				0.5f, -0.5f, 0.5f,
		};
		float[] textCoords = new float[]{
				0.0f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.5f, 0.0f,

				0.0f, 0.0f,
				0.5f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,

				// For text coords in top face
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.0f, 1.0f,
				0.5f, 1.0f,

				// For text coords in right face
				0.0f, 0.0f,
				0.0f, 0.5f,

				// For text coords in left face
				0.5f, 0.0f,
				0.5f, 0.5f,

				// For text coords in bottom face
				0.5f, 0.0f,
				1.0f, 0.0f,
				0.5f, 0.5f,
				1.0f, 0.5f,
		};
		int[] indices = new int[]{
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				8, 10, 11, 9, 8, 11,
				// Right face
				12, 13, 7, 5, 12, 7,
				// Left face
				14, 15, 6, 4, 14, 6,
				// Bottom face
				16, 18, 19, 17, 16, 19,
				// Back face
				4, 6, 7, 5, 4, 7,};
		Texture texture = scene.getCache().createTexture("models/cube/cube.png", true);
		Material material = new Material();
		material.setTexturePath(texture.getTexturePath());
		List<Material> materialList = new ArrayList<>();
		materialList.add(material);

		Mesh mesh = new Mesh(positions, textCoords, indices);
		material.getMeshList().add(mesh);
		Model cubeModel = new Model("cube-model", materialList);
		scene.addModel(cubeModel);

		cubeEntity = new Entity("cube-entity", cubeModel.getId());
		cubeEntity.setPosition(0, 0, -2);
		scene.addEntity(cubeEntity);

		renderer.addRenderer(new EntityRenderer());
	}

	@Override
	public void input(Window window, Scene scene, long diffTimeMillis) {
		displacement.zero();
		if (window.isKeyPressed(GLFW_KEY_UP)) {
			displacement.y = 1;
		} else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
			displacement.y = -1;
		}
		if (window.isKeyPressed(GLFW_KEY_LEFT)) {
			displacement.x = -1;
		} else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
			displacement.x = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			displacement.z = -1;
		} else if (window.isKeyPressed(GLFW_KEY_Q)) {
			displacement.z = 1;
		}
		if (window.isKeyPressed(GLFW_KEY_Z)) {
			displacement.w = -1;
		} else if (window.isKeyPressed(GLFW_KEY_X)) {
			displacement.w = 1;
		}

		displacement.mul(diffTimeMillis / 1000.0f);

		Vector3f entityPos = cubeEntity.getPosition();
		cubeEntity.setPosition(displacement.x + entityPos.x, displacement.y + entityPos.y, displacement.z + entityPos.z);
		cubeEntity.setScale(cubeEntity.getScale() + displacement.w);
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