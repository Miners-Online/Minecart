package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.render.Shader;
import uk.minersonline.minecart.engine.render.UniformsMap;
import uk.minersonline.minecart.engine.scene.objects.Entity;
import uk.minersonline.minecart.engine.scene.objects.Model;
import uk.minersonline.minecart.engine.utils.Destroyable;
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
	}

	@Override
	public void destroy() {
		shaderProgram.destroy();
	}

	@Override
	public void render(Scene scene) {
		shaderProgram.bind();
		uniforms.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());

		Collection<Model> models = scene.getModelMap().values();
		for (Model model : models) {
			model.getMeshList().forEach(mesh -> {
				glBindVertexArray(mesh.getVaoId());
				List<Entity> entities = model.getEntitiesList();
				for (Entity entity : entities) {
					uniforms.setUniform("modelMatrix", entity.getModelMatrix());
					glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
				}
			});
		}

		glBindVertexArray(0);

		shaderProgram.unbind();
	}
}
