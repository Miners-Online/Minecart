package uk.minersonline.minecart.engine.scene.terrain;

import imgui.ImGui;
import uk.minersonline.minecart.engine.render.objects.Mesh;
import uk.minersonline.minecart.engine.scene.components.AbstractComponent;

import static uk.minersonline.minecart.engine.scene.terrain.VoxelMesh.getMeshForFace;

public class VoxelTerrainComponent extends AbstractComponent {
	public static int CHUNK_WIDTH = 16;
	public static int CHUNK_HEIGHT = 16;
	public static int CHUNK_DEPTH = 16;

	private final VoxelType[][][] blocks = new VoxelType[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_DEPTH];

	private final AbstractTerrainGenerator generator;

	public VoxelTerrainComponent(AbstractTerrainGenerator generator) {
		this.generator = generator;
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_DEPTH; z ++) {
					this.blocks[x][y][z] = VoxelType.AIR;
				}
			}
		}
	}

	public void generate() {
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int y = 0; y < CHUNK_HEIGHT; y++) {
				for (int z = 0; z < CHUNK_DEPTH; z ++) {
					this.blocks[x][y][z] = this.generator.generate(x, y, z);
				}
			}
		}
	}

	@Override
	public void drawGui() {
		ImGui.text("Voxel Terrain Component");
		ImGui.separator();
	}

	public void setBlock(int x, int y, int z, VoxelType block) {
		this.blocks[x][y][z] = block;
	}

	public VoxelType getBlock(int x, int y, int z) {
		try {
			return this.blocks[x][y][z];
		} catch (IndexOutOfBoundsException e) {
			return VoxelType.AIR;
		}
	}

	public VoxelMesh makeMesh() {
		VoxelMesh temp_mesh = new VoxelMesh();
		for (int x = 0; x < CHUNK_WIDTH; ++x) {
			for (int y = 0; y < CHUNK_HEIGHT; ++y) {
				for (int z = 0; z < CHUNK_DEPTH; ++z) {
					if (x > 0 && getBlock(x - 1, y, z) == VoxelType.AIR) {
						// add face back
						temp_mesh.update(getMeshForFace(VoxelFace.Back), x - 1, y, z);
					}
					if (y > 0 && getBlock(x, y - 1, z) == VoxelType.AIR) {
						// add face bottom
						temp_mesh.update(getMeshForFace(VoxelFace.Bottom), x, y - 1, z);
					}
					if (z > 0 && getBlock(x, y, z - 1) == VoxelType.AIR) {
						// add face left
						temp_mesh.update(getMeshForFace(VoxelFace.Left), x, y, z - 1);
					}
					if (x + 1 < CHUNK_WIDTH && getBlock(x + 1, y, z) == VoxelType.AIR) {
						// add face front
						temp_mesh.update(getMeshForFace(VoxelFace.Front), x + 1, y, z);
					}
					if (y + 1 < CHUNK_HEIGHT && getBlock(x, y + 1, z) == VoxelType.AIR) {
						// add face top
						temp_mesh.update(getMeshForFace(VoxelFace.Top), x, y + 1, z);
					}
					if (z + 1 < CHUNK_DEPTH && getBlock(x, y, z + 1) == VoxelType.AIR) {
						// add face right
						temp_mesh.update(getMeshForFace(VoxelFace.Right), x, y, z + 1);
					}
				}
			}
		}
		return temp_mesh;
	}
}
