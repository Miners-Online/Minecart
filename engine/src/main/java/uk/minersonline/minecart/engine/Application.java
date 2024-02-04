package uk.minersonline.minecart.engine;

import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.render.MainRenderer;

public interface Application extends Destroyable {
	void init(Window window, Scene scene, MainRenderer render);

	void input(Window window, Scene scene, long deltaTime, boolean inputConsumed);

	void update(Window window, Scene scene, long deltaTime);
}
