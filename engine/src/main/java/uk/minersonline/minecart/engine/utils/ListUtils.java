package uk.minersonline.minecart.engine.utils;

import java.nio.ByteBuffer;
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

	// from: https://stackoverflow.com/a/21388198 by jdmichal / seh
	public static ByteBuffer cloneByteBuffer(final ByteBuffer original) {
		// Create clone with same capacity as original.
		final ByteBuffer clone = (original.isDirect()) ?
				ByteBuffer.allocateDirect(original.capacity()) :
				ByteBuffer.allocate(original.capacity());

		// Create a read-only copy of the original.
		// This allows reading from the original without modifying it.
		final ByteBuffer readOnlyCopy = original.asReadOnlyBuffer();

		// Flip and read from the original.
		readOnlyCopy.flip();
		clone.put(readOnlyCopy);

		return clone;
	}
}
