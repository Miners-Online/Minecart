package uk.minersonline.minecart.demo;

import uk.minersonline.minecart.engine.scene.terrain.AbstractTerrainGenerator;
import uk.minersonline.minecart.engine.scene.terrain.VoxelType;

public class ExampleWorldGenerator extends AbstractTerrainGenerator {
	@Override
	public VoxelType generate(int x, int y, int z) {
		if (y == 10) {
			return VoxelType.AIR;
		} else if (z > 12) {
			return VoxelType.AIR;
		} else {
			return ExampleVoxels.BLACK_CUBE;
		}
	}
}
