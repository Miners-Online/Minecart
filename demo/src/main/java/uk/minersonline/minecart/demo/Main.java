package uk.minersonline.minecart.demo;

import uk.minersonline.minecart.engine.Engine;
import uk.minersonline.minecart.engine.window.Window;
import uk.minersonline.minecart.engine.window.WindowProperties;

import java.awt.*;

public class Main extends Engine {
	public static void main(String[] args) {
		Main main = new Main();
		main.run(new WindowProperties(300, 300, "Demo app"));
	}

//	@Override
//	protected void loopEntry(Window window) {
//		window.clear(Color.CYAN);
//	}
}