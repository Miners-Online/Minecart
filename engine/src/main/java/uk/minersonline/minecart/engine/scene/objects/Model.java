package uk.minersonline.minecart.engine.scene.objects;

import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.*;

public class Model implements Destroyable {
	private final String id;
	private final List<Entity> entitiesList;
	private final List<Material> materialList;

	public Model(String id, List<Material> materialList) {
		this.id = id;
		this.materialList = materialList;
		entitiesList = new ArrayList<>();
	}

	@Override
	public void destroy() {
		materialList.forEach(Material::destroy);
	}

	public List<Entity> getEntitiesList() {
		return entitiesList;
	}

	public String getId() {
		return id;
	}

	public List<Material> getMaterialList() {
		return materialList;
	}
}