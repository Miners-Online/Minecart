package uk.minersonline.minecart.engine.scene.terrain;

import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NonnullDefault;
import uk.minersonline.minecart.engine.utils.Destroyable;
import uk.minersonline.minecart.engine.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class VoxelMesh implements Destroyable {
	private final List<Float> vertices;
	private final List<Float> normals;
	private final List<Float> texCoords;

	private int vaoId = -1;
	private boolean dirty = true;
	private List<Integer> vboIdList = new ArrayList<>();

	public VoxelMesh(List<Float> vertices, List<Float> normals, List<Float> texCoords) {
		this.vertices = vertices;
		this.normals = normals;
		this.texCoords = texCoords;
	}

	public VoxelMesh() {
		this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	void update(VoxelMesh other, int x, int y, int z) {
		for (int i = 0; i < other.vertices.size() / 3; ++i) {
			vertices.add(other.vertices.get(i * 3) + x);
			vertices.add(other.vertices.get(1 + i * 3) + y);
			vertices.add(other.vertices.get(2 + i * 3) + z);
		}
		normals.addAll(other.normals);
		texCoords.addAll(other.texCoords);
		this.dirty = true;
	}

	public int triangleCount() {
		return vertices.size() / 3;
	}

	@NonnullDefault
	public static VoxelMesh getMeshForFace(VoxelFace face) {
		switch (face) {
			case Top -> {
				return new VoxelMesh(
					new ArrayList<>(List.of(
						-0.5f,  0.5f, -0.5f,
						0.5f,  0.5f, -0.5f,
						0.5f,  0.5f,  0.5f,
						0.5f,  0.5f,  0.5f,
						-0.5f,  0.5f,  0.5f,
						-0.5f,  0.5f, -0.5f
					)),
					new ArrayList<>(List.of(
						0.0f,  1.0f,  0.0f
					)),
					new ArrayList<>(List.of(
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						0.0f, 0.0f
					))
				);
			}
			case Bottom -> {
				return new VoxelMesh(
					new ArrayList<>(List.of(
						-0.5f, -0.5f, -0.5f,
						0.5f, -0.5f, -0.5f,
						0.5f, -0.5f,  0.5f,
						0.5f, -0.5f,  0.5f,
						-0.5f, -0.5f,  0.5f,
						-0.5f, -0.5f, -0.5f
					)),
					new ArrayList<>(List.of(
						0.0f, -1.0f,  0.0f
					)),
					new ArrayList<>(List.of(
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						0.0f, 0.0f
					))
				);
			}
			case Front -> {
				return new VoxelMesh(
					new ArrayList<>(List.of(
						-0.5f, -0.5f,  0.5f,
						0.5f, -0.5f,  0.5f,
						0.5f,  0.5f,  0.5f,
						0.5f,  0.5f,  0.5f,
						-0.5f,  0.5f,  0.5f,
						-0.5f, -0.5f,  0.5f
					)),
					new ArrayList<>(List.of(
						0.0f,  0.0f,  1.0f
					)),
					new ArrayList<>(List.of(
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						0.0f, 0.0f
					))
				);
			}
			case Back -> {
				return new VoxelMesh(
					new ArrayList<>(List.of(
						-0.5f, -0.5f, -0.5f,
						0.5f, -0.5f, -0.5f,
						0.5f,  0.5f, -0.5f,
						0.5f,  0.5f, -0.5f,
						-0.5f,  0.5f, -0.5f,
						-0.5f, -0.5f, -0.5f
					)),
					new ArrayList<>(List.of(
						0.0f,  0.0f, -1.0f
					)),
					new ArrayList<>(List.of(
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						0.0f, 0.0f
					))
				);
			}
			case Right -> {
				return new VoxelMesh(
					new ArrayList<>(List.of(
						0.5f,  0.5f,  0.5f,
						0.5f,  0.5f, -0.5f,
						0.5f, -0.5f, -0.5f,
						0.5f, -0.5f, -0.5f,
						0.5f, -0.5f,  0.5f,
						0.5f,  0.5f,  0.5f
					)),
					new ArrayList<>(List.of(
						1.0f,  0.0f,  0.0f
					)),
					new ArrayList<>(List.of(
						0.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 0.0f,
						1.0f, 1.0f,
						0.0f, 1.0f,
						0.0f, 0.0f
					))
				);
			}
		}
		return new VoxelMesh( //case Left
			new ArrayList<>(List.of(
				-0.5f,  0.5f,  0.5f,
				-0.5f,  0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f,  0.5f,
				-0.5f,  0.5f,  0.5f
			)),
			new ArrayList<>(List.of(
				-1.0f,  0.0f,  0.0f
			)),
			new ArrayList<>(List.of(
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f,
				0.0f, 0.0f
			))
		);
	}

	public void buildMesh() {
		if (!this.dirty) {
			return;
		}
		if (!this.texCoords.isEmpty() && !this.normals.isEmpty() && !this.vertices.isEmpty()) {
			this.destroy();
			vboIdList = new ArrayList<>();

			vaoId = glGenVertexArrays();
			glBindVertexArray(vaoId);

			// Positions VBO
			int vboId = glGenBuffers();
			vboIdList.add(vboId);
			float[] positions = ListUtils.convertToArray(vertices);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

			// Texture Coordinates VBO
			vboId = glGenBuffers();
			vboIdList.add(vboId);
			float[] textCoords = ListUtils.convertToArray(texCoords);
			glBindBuffer(GL_ARRAY_BUFFER, vboId);
			glBufferData(GL_ARRAY_BUFFER, textCoords, GL_STATIC_DRAW);
			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			dirty = false;
		}
	}

	@Override
	public void destroy() {
		if (vaoId != -1) {
			vboIdList.forEach(GL30::glDeleteBuffers);
			glDeleteVertexArrays(vaoId);
		}
	}

	public final int getVaoId() {
		return vaoId;
	}
}
