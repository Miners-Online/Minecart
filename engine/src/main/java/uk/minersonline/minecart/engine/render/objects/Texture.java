package uk.minersonline.minecart.engine.render.objects;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.utils.FileUtils;

import java.nio.*;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Destroyable {
	private int textureId;
	private byte[] data;
	private final String texturePath;

	public Texture(int width, int height, int textureId) {
		this.texturePath = "";
		this.textureId = textureId;
	}

	public Texture(String texturePath, boolean isResource) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			this.texturePath = texturePath;
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer channels = stack.mallocInt(1);

			byte[] data;
			if (isResource) {
				data = FileUtils.readResourceToByteArray(texturePath);
			} else {
				data = FileUtils.readFileToByteArray(texturePath);
			}
			this.data = data;

			ByteBuffer img = BufferUtils.createByteBuffer(data.length);
			img.put(data);
			img.flip();
			ByteBuffer buf = stbi_load_from_memory(img, w, h, channels, 4);
			if (buf == null) {
				throw new RuntimeException("Image file [" + texturePath + "] not loaded: " + stbi_failure_reason());
			}

			int width = w.get();
			int height = h.get();

			generateTexture(width, height, buf);

			stbi_image_free(buf);
		}
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}

	@Override
	public void destroy() {
		glDeleteTextures(textureId);
	}

	private void generateTexture(int width, int height, ByteBuffer buf) {
		textureId = glGenTextures();

		glBindTexture(GL_TEXTURE_2D, textureId);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0,
				GL_RGBA, GL_UNSIGNED_BYTE, buf);
		glGenerateMipmap(GL_TEXTURE_2D);
	}

	public String getTexturePath() {
		return texturePath;
	}

	public int getTextureId() {
		return textureId;
	}

	public byte[] getData() {
		return data;
	}
}