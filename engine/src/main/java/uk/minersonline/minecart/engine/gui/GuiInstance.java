package uk.minersonline.minecart.engine.gui;

import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.window.Window;

public interface GuiInstance {
	void drawGui();

	boolean handleGuiInput(Scene scene, Window window);
}
