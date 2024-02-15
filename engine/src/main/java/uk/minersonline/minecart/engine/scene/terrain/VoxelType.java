package uk.minersonline.minecart.engine.scene.terrain;

import uk.minersonline.minecart.engine.registry.Identifier;
import uk.minersonline.minecart.engine.registry.Registries;
import uk.minersonline.minecart.engine.registry.Registry;

public class VoxelType {
	public static final VoxelType AIR = new VoxelType(VoxelTypeSettings.create());

	protected final VoxelTypeSettings settings;

	public VoxelType(VoxelTypeSettings settings) {
		this.settings = settings;
	}

	public static void init() {
		Registry.register(Registries.VOXEL_TYPE, new Identifier("minecart", "air"), AIR);
	}

	public static class VoxelTypeSettings {
		private String texture = "minecart/assets/textures/default_texture.png";
		private int atlasLocation;
		private boolean collidable = true;

		public static VoxelTypeSettings create() {
			return new VoxelTypeSettings();
		}

		public VoxelTypeSettings texture(String texture) {
			this.texture = texture;
			return this;
		}

		public VoxelTypeSettings collidable(boolean collidable) {
			this.collidable = collidable;
			return this;
		}

		public VoxelTypeSettings atlasLocation(int atlasLocation) {
			this.atlasLocation = atlasLocation;
			return this;
		}

		public String getTexture() {
			return texture;
		}

		public boolean isCollidable() {
			return collidable;
		}

		public int getAtlasLocation() {
			return atlasLocation;
		}
	}

	public VoxelTypeSettings getSettings() {
		return settings;
	}
}
