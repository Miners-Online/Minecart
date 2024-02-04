package uk.minersonline.minecart.engine.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
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
}
