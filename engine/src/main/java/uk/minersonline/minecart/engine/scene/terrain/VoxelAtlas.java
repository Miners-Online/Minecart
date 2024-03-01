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

//        int index = 0;
        int totalX = 0;
        for (String path : images.keySet()) {
            Texture texture = images.get(path);

            // Calculate position within the atlas
            int targetX = totalX + texture.getWidth();
            int targetY = texture.getHeight();

            // Copy texture data into the atlas
            ByteBuffer imgData = texture.getData();
            imgData.flip();
            System.out.println(path);
//            System.out.println(Arrays.toString(imgData));
//            ByteBuffer buffer = ByteBuffer.allocateDirect(imgData.length);
//            buffer.put(imgData);
//            buffer.flip(); // Reset position to 0 before passing to OpenGL

//            glPixelStorei(GL_UNPACK_ROW_LENGTH, texture.getWidth());
//            glPixelStorei(GL_UNPACK_SKIP_PIXELS, 0);
//            glPixelStorei(GL_UNPACK_SKIP_ROWS, 0);
            glTexSubImage2D(GL_TEXTURE_2D, 0, targetX, targetY, texture.getWidth(), texture.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE, imgData);
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