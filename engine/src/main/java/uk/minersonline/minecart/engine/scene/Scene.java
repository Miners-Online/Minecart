package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.scene.objects.Mesh;
import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.HashMap;
import java.util.Map;

public class Scene implements Destroyable {

	private final Map<String, Mesh> meshMap;

	public Scene() {
		meshMap = new HashMap<>();
	}

	public void addMesh(String meshId, Mesh mesh) {
		meshMap.put(meshId, mesh);
	}

	@Override
	public void destroy() {
		meshMap.values().forEach(Mesh::destroy);
	}

	public Map<String, Mesh> getMeshMap() {
		return meshMap;
	}
}