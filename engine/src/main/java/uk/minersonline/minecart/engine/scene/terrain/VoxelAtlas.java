package uk.minersonline.minecart.engine.scene.terrain;

import org.lwjgl.BufferUtils;
import uk.minersonline.minecart.engine.render.objects.Texture;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class VoxelAtlas {
    private static final Map<String, Texture> images = new HashMap<>();
    private static Texture textureAtlas;

    public static void createTextureAtlas() {
        int width = 0;
        int height = 0;

        // Adjust width and height based on the biggest texture
        for (Texture texture : images.values()) {
             width = width + texture.getWidth();
             if (texture.getHeight() > height) {
                 height = texture.getHeight();
             }
        }

        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
//        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

//        int index = 0;
        int totalX = 0;
        for (String path : images.keySet()) {
            Texture texture = images.get(path);

            // Calculate position within the atlas
            int targetX = totalX + texture.getWidth();
            int targetY = 16; // Assuming all textures are aligned at the top

            // Copy texture data into the atlas
            byte[] imgData = texture.getData();
            System.out.println(path);
            System.out.println(Arrays.toString(imgData));
            ByteBuffer buffer = ByteBuffer.allocateDirect(imgData.length);
            buffer.put(imgData);
            buffer.flip(); // Reset position to 0 before passing to OpenGL

            glPixelStorei(GL_UNPACK_ROW_LENGTH, texture.getWidth());
            glPixelStorei(GL_UNPACK_SKIP_PIXELS, targetX);
            glPixelStorei(GL_UNPACK_SKIP_ROWS, targetY);
            glTexSubImage2D(GL_TEXTURE_2D, 0, targetX, targetY, texture.getWidth(), texture.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            // Update index for the next texture
//            index++;
            totalX = totalX + targetX;
        }
        glBindTexture(GL_TEXTURE_2D, 0);
        textureAtlas = new Texture(width, height, result);
    }

    public static void addTexture(Texture texture) {
        images.put(texture.getTexturePath(), texture);
    }

    public static Texture getTextureAtlas() {
        return textureAtlas;
    }
}