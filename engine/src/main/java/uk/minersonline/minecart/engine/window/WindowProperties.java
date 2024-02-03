package uk.minersonline.minecart.engine.window;

public class WindowProperties {
	private final int width;
	private final int height;
	private final String title;

	public WindowProperties(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}
}
