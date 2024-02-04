package uk.minersonline.minecart.engine.scene.objects;

import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.*;

public class Material implements Destroyable {
	private final List<Mesh> meshList;
	private String texturePath;

	public Material() {
		meshList = new ArrayList<>();
	}

	public void destroy() {
		meshList.forEach(Mesh::destroy);
	}

	public List<Mesh> getMeshList() {
		return meshList;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}
}