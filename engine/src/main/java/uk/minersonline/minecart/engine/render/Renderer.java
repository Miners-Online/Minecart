package uk.minersonline.minecart.engine.render;

import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.utils.Destroyable;

public interface Renderer extends Destroyable {
	void render(Scene scene);
}
