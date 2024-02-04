package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.render.objects.TextureCache;
import uk.minersonline.minecart.engine.scene.objects.Entity;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.HashMap;
import java.util.Map;

public class Scene implements Destroyable {

	private final Map<String, Model> modelMap;
	private final Projection3D projection;
	private final TextureCache cache;

	public Scene(int width, int height) {
		modelMap = new HashMap<>();
		projection = new Projection3D(width, height);
		cache = new TextureCache();
	}

	public void addEntity(Entity entity) {
		String modelId = entity.getModelId();
		Model model = modelMap.get(modelId);
		if (model == null) {
			throw new RuntimeException("Could not find model [" + modelId + "]");
		}
		model.getEntitiesList().add(entity);
	}

	public void addModel(Model model) {
		modelMap.put(model.getId(), model);
	}

	@Override
	public void destroy() {
		modelMap.values().forEach(Model::destroy);
	}

	public Map<String, Model> getModelMap() {
		return modelMap;
	}

	public Projection3D getProjection() {
		return projection;
	}

	public TextureCache getCache() {
		return cache;
	}

	public void resize(int width, int height) {
		projection.updateProjMatrix(width, height);
	}
}