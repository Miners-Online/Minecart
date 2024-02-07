package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.render.Shader;
import uk.minersonline.minecart.engine.render.UniformsMap;
import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.render.objects.Texture;
import uk.minersonline.minecart.engine.render.objects.TextureCache;
import uk.minersonline.minecart.engine.scene.components.ModelComponent;
import uk.minersonline.minecart.engine.scene.components.TransformComponent;
import uk.minersonline.minecart.engine.scene.objects.Material;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.render.Renderer;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class EntityRenderer implements Renderer {
	private final Shader shaderProgram;
	private final UniformsMap uniforms;

	public EntityRenderer() {
		List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/entity.vert", GL_VERTEX_SHADER, true));
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/entity.frag", GL_FRAGMENT_SHADER, true));
		shaderProgram = new Shader(shaderModuleDataList);
		uniforms = new UniformsMap(shaderProgram.getProgramId());
		uniforms.createUniform("projectionMatrix");
		uniforms.createUniform("modelMatrix");
		uniforms.createUniform("txtSampler");
		uniforms.createUniform("viewMatrix");
	}

	@Override
	public void destroy() {
		shaderProgram.destroy();
	}

	@Override
	public void render(Scene scene) {
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		shaderProgram.bind();

		uniforms.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
		uniforms.setUniform("txtSampler", 0);
		uniforms.setUniform("viewMatrix", scene.getCamera().getViewMatrix());

		Collection<Model> models = scene.getModelMap().values();
		TextureCache textureCache = scene.getCache();
		for (Model model : models) {
			scene.getDominion().findEntitiesWith(ModelComponent.class, TransformComponent.class)
				.stream().forEach(result -> {
					String modelId = result.comp1().getModelId();
					TransformComponent position = result.comp2();
					if (modelId.equals(model.getId())) {
						for (Material material : model.getMaterialList()) {
							Texture texture = textureCache.getTexture(material.getTexturePath());
							glActiveTexture(GL_TEXTURE0);
							texture.bind();

							for (Mesh mesh : material.getMeshList()) {
								glBindVertexArray(mesh.getVaoId());
								uniforms.setUniform("modelMatrix", position.getModelMatrix());
								glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
							}
						}
					}
				}
			);
		}

		glBindVertexArray(0);

		shaderProgram.unbind();
	}
}
