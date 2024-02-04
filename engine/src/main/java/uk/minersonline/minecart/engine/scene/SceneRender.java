package uk.minersonline.minecart.engine.scene;


import uk.minersonline.minecart.engine.render.Shader;
import uk.minersonline.minecart.engine.utils.Destroyable;

import java.util.*;

import static org.lwjgl.opengl.GL30.*;

public class SceneRender implements Destroyable {

	private final Shader shaderProgram;

	public SceneRender() {
		List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/scene.vert", GL_VERTEX_SHADER, true));
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/scene.frag", GL_FRAGMENT_SHADER, true));
		shaderProgram = new Shader(shaderModuleDataList);
	}

	@Override
	public void destroy() {
		shaderProgram.destroy();
	}

	public void render(Scene scene) {
		shaderProgram.bind();

		scene.getMeshMap().values().forEach(mesh -> {
					glBindVertexArray(mesh.getVaoId());
					glDrawArrays(GL_TRIANGLES, 0, mesh.getNumVertices());
				}
		);

		glBindVertexArray(0);

		shaderProgram.unbind();
	}
}
