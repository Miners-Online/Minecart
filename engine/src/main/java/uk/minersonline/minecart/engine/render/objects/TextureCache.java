package uk.minersonline.minecart.engine.render.objects;


import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.*;

public class TextureCache implements Destroyable {
	public static final String DEFAULT_TEXTURE = "minecart/assets/textures/default_texture.png";

	private final Map<String, Texture> textureMap;

	public TextureCache() {
		textureMap = new HashMap<>();
		textureMap.put(DEFAULT_TEXTURE, new Texture(DEFAULT_TEXTURE, true));
	}

	@Override
	public void destroy() {
		textureMap.values().forEach(Texture::destroy);
	}

	public Texture createTexture(String texturePath, boolean isResource) {
		return textureMap.computeIfAbsent(texturePath,  (path) -> new Texture(path, isResource));
	}

	public Texture getTexture(String texturePath) {
		Texture texture = null;
		if (texturePath != null) {
			texture = textureMap.get(texturePath);
		}
		if (texture == null) {
			texture = textureMap.get(DEFAULT_TEXTURE);
		}
		return texture;
	}

	public Map<String, Texture> getTextureMap() {
		return Collections.unmodifiableMap(textureMap);
	}
}
