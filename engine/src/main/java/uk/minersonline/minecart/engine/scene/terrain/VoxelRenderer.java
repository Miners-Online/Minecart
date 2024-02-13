package uk.minersonline.minecart.engine.scene.terrain;


import uk.minersonline.minecart.engine.render.Renderer;
import uk.minersonline.minecart.engine.render.Shader;
import uk.minersonline.minecart.engine.render.UniformsMap;
import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.render.objects.Texture;
import uk.minersonline.minecart.engine.render.objects.TextureCache;
import uk.minersonline.minecart.engine.scene.Scene;
import uk.minersonline.minecart.engine.scene.components.TransformComponent;
import uk.minersonline.minecart.engine.scene.objects.Material;
import uk.minersonline.minecart.engine.scene.objects.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class VoxelRenderer implements Renderer {
	private final Shader shaderProgram;
	private final UniformsMap uniforms;

	public VoxelRenderer() {
		List<Shader.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/voxel_terrain/shader.vert", GL_VERTEX_SHADER, true));
		shaderModuleDataList.add(new Shader.ShaderModuleData("shaders/voxel_terrain/shader.frag", GL_FRAGMENT_SHADER, true));
		shaderProgram = new Shader(shaderModuleDataList);
		uniforms = new UniformsMap(shaderProgram.getProgramId());
		uniforms.createUniform("model");
		uniforms.createUniform("view");
		uniforms.createUniform("projection");
		uniforms.createUniform("texture1");
	}

	@Override
	public void destroy() {
		shaderProgram.destroy();
	}

	@Override
	public void render(Scene scene) {
//		glEnable(GL_DEPTH_TEST);
		glDisable(GL_CULL_FACE);
		shaderProgram.bind();

		uniforms.setUniform("projection", scene.getProjection().getProjMatrix());
		uniforms.setUniform("texture1", 0);
		uniforms.setUniform("view", scene.getCamera().getViewMatrix());


		TextureCache textureCache = scene.getCache();
		scene.getDominion().findEntitiesWith(VoxelTerrainComponent.class, TransformComponent.class)
			.stream().forEach(result -> {
				VoxelTerrainComponent terrain = result.comp1();
				TransformComponent transform = result.comp2();
				processTerrain(terrain, transform, textureCache);
			}
		);

		glBindVertexArray(0);

		shaderProgram.unbind();
	}

	private void processTerrain(VoxelTerrainComponent terrain, TransformComponent transform, TextureCache textureCache) {
		Texture texture = textureCache.getTexture(terrain.getTextureID());
		glActiveTexture(GL_TEXTURE0);
		texture.bind();

		VoxelMesh mesh = terrain.makeMesh();
		mesh.buildMesh();

		glBindVertexArray(mesh.getVaoId());
		transform.updateModelMatrix();
		uniforms.setUniform("model", transform.getModelMatrix());
		glDrawArrays(GL_TRIANGLES, 0, mesh.triangleCount());

		mesh.destroy();
	}
}
