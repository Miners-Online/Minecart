package uk.minersonline.minecart.engine.utils;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.*;

public class FileUtils {
	public static String readFile(String filePath) {
		String str;
		try {
			str = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return str;
	}

	public static String readResource(String filePath) {
		String str;
		try {
			InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
			byte[] data = stream.readAllBytes();
			str = new String(data);
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return str;
	}

	public static ByteBuffer readFileToBuffer(String filePath) {
		ByteBuffer buffer;
		try {
			byte[] data = Files.readAllBytes(Paths.get(filePath));
			buffer = BufferUtils.createByteBuffer(data.length);
			buffer.put(data);
			buffer.flip();
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return buffer;
	}

	public static ByteBuffer readResourceToBuffer(String filePath) {
		ByteBuffer buffer;
		try {
			InputStream stream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
			byte[] data = stream.readAllBytes();
			buffer = BufferUtils.createByteBuffer(data.length);
			buffer.put(data);
			buffer.flip();
		} catch (IOException excp) {
			throw new RuntimeException("Error reading file [" + filePath + "]", excp);
		}
		return buffer;
	}
}
