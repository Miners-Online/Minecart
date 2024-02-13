package uk.minersonline.minecart.engine.utils;

import java.util.List;

public class ListUtils {
	public static float[] convertToArray(List<Float> floatList) {
		float[] floatArray = new float[floatList.size()];
		int i = 0;

		for (Float f : floatList) {
			floatArray[i++] = (f != null ? f : Float.NaN);
		}
		return floatArray;
	}
}
