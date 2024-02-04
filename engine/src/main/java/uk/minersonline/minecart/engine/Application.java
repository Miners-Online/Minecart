package uk.minersonline.minecart.engine;

import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.render.Render;

public interface Application extends Destroyable {
	void init(Window window, Scene scene, Render render);

	void input(Window window, Scene scene, long deltaTime);

	void update(Window window, Scene scene, long deltaTime);
}
