package uk.minersonline.minecart.engine;

import uk.minersonline.minecart.engine.gui.GuiInstance;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;
import uk.minersonline.minecart.engine.render.MainRenderer;

public class Engine implements Destroyable {
	public static final int TARGET_UPS = 30;
	private final Application application;
	private final Window window;
	private MainRenderer render;
	private boolean running;
	private Scene scene;
	private int targetFps;
	private int targetUps;

	public Engine(WindowProperties properties, Application application) {
		window = Window.buildWindow(properties, () -> {
			resize();
			return null;
		});
		targetFps = properties.fps;
		targetUps = properties.ups;
		this.application = application;
		render = new MainRenderer();
		scene = new Scene(properties.width, properties.height);
		application.init(window, scene, render);
		running = true;
	}

	@Override
	public void destroy() {
		application.destroy();
		render.destroy();
		scene.destroy();
		window.destroy();
	}

	private void resize() {
		scene.resize(window.getWidth(), window.getHeight());
		render.resize(window.getWidth(), window.getHeight());
	}

	private void run() {
		GuiInstance guiInstance = scene.getGuiInstance();
		long initialTime = System.currentTimeMillis();
		float timeU = 1000.0f / targetUps;
		float timeR = targetFps > 0 ? 1000.0f / targetFps : 0;
		float deltaUpdate = 0;
		float deltaFps = 0;

		long updateTime = initialTime;
		while (running && !window.shouldClose()) {
			window.pollEvents();

			long now = System.currentTimeMillis();
			deltaUpdate += (now - initialTime) / timeU;
			deltaFps += (now - initialTime) / timeR;

			if (targetFps <= 0 || deltaFps >= 1) {
				window.getMouseInput().input();
				boolean inputConsumed = guiInstance != null && guiInstance.handleGuiInput(scene, window);
				application.input(window, scene, now - initialTime, inputConsumed);
			}

			if (deltaUpdate >= 1) {
				long deltaTime = now - updateTime;
				application.update(window, scene, deltaTime);
				updateTime = now;
				deltaUpdate--;
			}

			if (targetFps <= 0 || deltaFps >= 1) {
				render.render(window, scene);
				deltaFps--;
				window.update();
			}
			initialTime = now;
		}

		destroy();
	}

	public void start() {
		running = true;
		run();
	}

	public void stop() {
		running = false;
	}
}