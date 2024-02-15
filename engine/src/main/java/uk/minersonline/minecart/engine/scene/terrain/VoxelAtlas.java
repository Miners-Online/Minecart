package uk.minersonline.minecart.engine.scene.terrain;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import uk.minersonline.minecart.engine.registry.Registries;
import uk.minersonline.minecart.engine.render.objects.Texture;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class VoxelAtlas {
    private int width, height;
    private static Vector2f atlasSize;
    private static int atlasCount;
    private int textureId;
    private static Map<String, Texture> images = new HashMap<>();
    private static Texture textureAtlas;

    public static void createTextureAtlas(){
        int result = glGenTextures();
        int width = images.size() * 256;
        int height = 256;

        int channels = 4;
        int[] atlas_data = new int[width * height * channels + 1];
        atlasSize = new Vector2f(width, height);
        atlasCount = images.size();

        int index = 0;
        for (String path : images.keySet()) {
            for (VoxelType type : Registries.VOXEL_TYPE.getEntries().values()) {
                if (type.settings.getTexture().equals(path)) {
                    type.settings.atlasLocation(index);
                    break;
                }
            }
            Texture subData = images.get(path);
            byte[] imgData = subData.getData();

            int targetX = 0;
            int targetY = index*(((32)-index*32)+32/4);

            for (int sourceY = 0; sourceY < 32; ++sourceY) {
                for (int sourceX = 0; sourceX < 32; ++sourceX) {
                    int from = (sourceY * 32 * channels) + (sourceX * channels);

                    int to = ((targetY + sourceY) * 32 * channels) + ((targetX + sourceX) * channels); // same format as source

                    for(int channel = 0; channel < channels; ++channel) {
                        byte data = imgData[from + channel];
                        atlas_data[to + channel] = data;
                    }
                }
            }
            index = index + 1;
        }

        IntBuffer buffer = BufferUtils.createIntBuffer(atlas_data.length);
        buffer.put(atlas_data);
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glBindTexture(GL_TEXTURE_2D, 0);
        textureAtlas = new Texture(width, height, result);
    }

    public static void addTexture(Texture texture) {
        images.put(texture.getTexturePath(), texture);
    }

    public static Texture getTextureAtlas() {
        return textureAtlas;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Vector2f getAtlasSize() {
        return atlasSize;
    }

    public static int getAtlasCount() {
        return atlasCount;
    }
}
