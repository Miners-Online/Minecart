package uk.minersonline.minecart.demo;

import uk.minersonline.minecart.engine.registry.Identifier;
import uk.minersonline.minecart.engine.registry.Registries;
import uk.minersonline.minecart.engine.registry.Registry;
import uk.minersonline.minecart.engine.scene.terrain.VoxelType;

public class ExampleVoxels {
    public static final VoxelType GRASS = new VoxelType(VoxelType.VoxelTypeSettings.create().texture("minecart/assets/textures/cube.png").collidable(true));
    public static final VoxelType BLACK_CUBE = new VoxelType(VoxelType.VoxelTypeSettings.create().texture("minecart/assets/textures/default_texture.png").collidable(true));

    public static void init() {
        Registry.register(Registries.VOXEL_TYPE, new Identifier("demo", "grass"), GRASS);
        Registry.register(Registries.VOXEL_TYPE, new Identifier("demo", "black_cube"), BLACK_CUBE);
    }
}

