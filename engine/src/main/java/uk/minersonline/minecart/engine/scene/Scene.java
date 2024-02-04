package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.gui.GuiInstance;
import uk.minersonline.minecart.engine.render.Resizeable;
import uk.minersonline.minecart.engine.render.objects.TextureCache;
import uk.minersonline.minecart.engine.scene.objects.Camera;
import uk.minersonline.minecart.engine.scene.objects.Entity;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.HashMap;
import java.util.Map;

public class Scene implements Destroyable, Resizeable {

	private final Map<String, Model> modelMap;
	private final Projection3D projection;
	private final TextureCache cache;
	private final Camera camera;

	private GuiInstance guiInstance;

	public Scene(int width, int height) {
		modelMap = new HashMap<>();
		projection = new Projection3D(width, height);
		cache = new TextureCache();
		camera = new Camera();
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

	public Camera getCamera() {
		return camera;
	}

	public GuiInstance getGuiInstance() {
		return guiInstance;
	}

	public void setGuiInstance(GuiInstance guiInstance) {
		this.guiInstance = guiInstance;
	}

	@Override
	public void resize(int width, int height) {
		projection.updateProjMatrix(width, height);
	}
}