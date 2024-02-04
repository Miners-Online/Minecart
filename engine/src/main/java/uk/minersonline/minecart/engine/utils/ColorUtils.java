package uk.minersonline.minecart.engine.utils;

import org.joml.Vector4f;

import java.awt.*;

public class ColorUtils {
	public static Vector4f colorToOpenGL(Color color) {
		float r=(1.0f/255)*color.getRed();
		float g=(1.0f/255)*color.getGreen();
		float b=(1.0f/255)*color.getBlue();
		float a=(1.0f/255)*color.getAlpha();
		return new Vector4f(r, g, b, a);
	}
}
