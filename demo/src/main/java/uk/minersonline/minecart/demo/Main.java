package uk.minersonline.minecart.demo;

import org.joml.Vector2f;
import org.joml.Vector3f;
import uk.minersonline.minecart.engine.Application;
import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.gui.DebugGui;
import uk.minersonline.minecart.engine.gui.GuiInstance;
import uk.minersonline.minecart.engine.render.objects.Texture;
import uk.minersonline.minecart.engine.scene.EntityRenderer;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.components.ModelComponent;
import uk.minersonline.minecart.engine.scene.components.TransformComponent;
import uk.minersonline.minecart.engine.scene.objects.Camera;
import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.scene.objects.Material;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;
import uk.minersonline.minecart.engine.render.MainRenderer;
import uk.minersonline.minecart.engine.window.input.MouseInput;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements Application, GuiInstance {
	private float rotation;

	private static final float MOUSE_SENSITIVITY = 0.2f;
	private static final float MOVEMENT_SPEED = 0.005f;
	private DebugGui debugGui;

	public static void main(String[] args) {
		Main main = new Main();
		WindowProperties properties = new WindowProperties();
		properties.title = "Demo App";
		properties.width = 600;
		properties.height = 600;
		Engine engine = new Engine(properties, main);
		engine.start();
	}

	@Override
	public void init(Window window, Scene scene, MainRenderer renderer) {
		debugGui = new DebugGui();
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
				0.5f, -0.5f, 0.5f
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
				1.0f, 0.5f
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
				4, 6, 7, 5, 4, 7
		};
		Texture texture = scene.getCache().createTexture("models/cube/cube.png", true);
		Material material = new Material();
		material.setTexturePath(texture.getTexturePath());
		List<Material> materialList = new ArrayList<>();
		materialList.add(material);

		Mesh mesh = new Mesh(positions, textCoords, indices);
		material.getMeshList().add(mesh);
		Model cubeModel = new Model("cube-model", materialList);
		scene.addModel(cubeModel);

		scene.getDominion().createEntity(
				"cube-entity",
				new TransformComponent(new Vector3f(0, 0, -2)),
				new ModelComponent("cube-model")
		);

		renderer.addRenderer(new EntityRenderer());
		scene.setGuiInstance(this);
	}

	@Override
	public void input(Window window, Scene scene, long deltaTime, boolean inputConsumed) {
		if (inputConsumed) {
			return;
		}

		float move = deltaTime * MOVEMENT_SPEED;
		Camera camera = scene.getCamera();
		if (window.isKeyPressed(GLFW_KEY_W)) {
			camera.moveForward(move);
		} else if (window.isKeyPressed(GLFW_KEY_S)) {
			camera.moveBackwards(move);
		}
		if (window.isKeyPressed(GLFW_KEY_A)) {
			camera.moveLeft(move);
		} else if (window.isKeyPressed(GLFW_KEY_D)) {
			camera.moveRight(move);
		}
		if (window.isKeyPressed(GLFW_KEY_SPACE)) {
			camera.moveUp(move);
		} else if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
			camera.moveDown(move);
		}

		MouseInput mouseInput = window.getMouseInput();
		if (mouseInput.isRightButtonPressed()) {
			Vector2f mouseDisplacement = mouseInput.getDisplacement();
			camera.addRotation(
				(float) Math.toRadians(-mouseDisplacement.x * MOUSE_SENSITIVITY),
				(float) Math.toRadians(-mouseDisplacement.y * MOUSE_SENSITIVITY)
			);
		}
	}

	@Override
	public void update(Window window, Scene scene, long deltaTime) {
		scene.getDominion().findEntitiesWith(String.class, TransformComponent.class)
			.stream().forEach(result -> {
				String name = result.comp1();
				TransformComponent transform = result.comp2();
				if (name.equals("cube-entity")) {
					rotation += 1.5f;
					if (rotation > 360) {
						rotation = 0;
					}
					transform.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
					transform.updateModelMatrix();
				}
			}
		);
	}

	@Override
	public void destroy() {

	}

	@Override
	public void drawGui(Scene scene) {
		debugGui.drawGui(scene);
	}
}