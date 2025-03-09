package fr.florian.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.system.MemoryStack.stackPush;

public class Material {
    private String path;
    private int textureID;
    private float width, height;

    public Material(String path) {
        this.path = path;
    }

    public void create() {
        try (var stack = stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer imageBuffer = loadImageFromClasspath(path);
            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer, channels, 4);

            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + path);
            }

            width = widthBuffer.get(0);
            height = heightBuffer.get(0);

            // Générer une texture OpenGL
            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // Envoyer l'image à OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int) width, (int) height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

            // Configurer les paramètres de texture
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            // Libérer l'image après chargement
            STBImage.stbi_image_free(image);
        } catch (Exception e) {
            throw new RuntimeException("Error loading texture", e);
        }
    }

    private ByteBuffer loadImageFromClasspath(String path) throws Exception {
        try (var inputStream = Objects.requireNonNull(Material.class.getResourceAsStream(path))) {
            byte[] bytes = inputStream.readAllBytes();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes).flip();
            return buffer;
        }
    }

    public void destroy() {
        GL11.glDeleteTextures(textureID);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getTextureID() {
        return textureID;
    }
}

