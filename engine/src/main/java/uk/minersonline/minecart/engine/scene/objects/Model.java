package uk.minersonline.minecart.engine.scene.objects;

import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.*;

public class Model implements Destroyable {
	private final String id;
	private final List<Entity> entitiesList;
	private final List<Mesh> meshList;

	public Model(String id, List<Mesh> meshList) {
		this.id = id;
		this.meshList = meshList;
		entitiesList = new ArrayList<>();
	}

	@Override
	public void destroy() {
		meshList.forEach(Mesh::destroy);
	}

	public List<Entity> getEntitiesList() {
		return entitiesList;
	}

	public String getId() {
		return id;
	}

	public List<Mesh> getMeshList() {
		return meshList;
	}
}