package fr.florian.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Represents a material that loads and manages a 2D texture from a file.
 * Uses stb_image for image loading and OpenGL for GPU texture handling.
 */
public class Material {

    /** Path to the image resource on the classpath. */
    private String path;

    /** OpenGL ID of the generated texture. */
    private int textureID;

    /** Width of the texture (in pixels). */
    private float width;

    /** Height of the texture (in pixels). */
    private float height;

    /**
     * Constructs a material with the specified image path.
     *
     * @param path The classpath-relative path to the texture image.
     */
    public Material(String path) {
        this.path = path;
    }

    /**
     * Loads the image from the classpath and creates an OpenGL texture.
     * Throws a RuntimeException if loading or binding fails.
     */
    public void create() {
        try (var stack = stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Read the image file into a ByteBuffer
            ByteBuffer imageBuffer = loadImageFromClasspath(path);

            // Decode image using stb_image
            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, widthBuffer, heightBuffer, channels, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + path);
            }

            width = widthBuffer.get(0);
            height = heightBuffer.get(0);

            // Generate and bind OpenGL texture
            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // Upload the image data to OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, (int) width, (int) height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

            // Set texture parameters
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            // Free image memory after uploading
            STBImage.stbi_image_free(image);
        } catch (Exception e) {
            throw new RuntimeException("Error loading texture", e);
        }
    }

    /**
     * Loads the image bytes from the classpath as a ByteBuffer.
     *
     * @param path Classpath-relative resource path.
     * @return A direct ByteBuffer containing the image data.
     * @throws Exception If the file is not found or cannot be read.
     */
    private ByteBuffer loadImageFromClasspath(String path) throws Exception {
        try (var inputStream = Objects.requireNonNull(Material.class.getResourceAsStream(path))) {
            byte[] bytes = inputStream.readAllBytes();
            ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
            buffer.put(bytes).flip();
            return buffer;
        }
    }

    /**
     * Deletes the OpenGL texture from GPU memory.
     */
    public void destroy() {
        GL11.glDeleteTextures(textureID);
    }

    /** @return The width of the texture in pixels. */
    public float getWidth() {
        return width;
    }

    /** @return The height of the texture in pixels. */
    public float getHeight() {
        return height;
    }

    /** @return The OpenGL texture ID. */
    public int getTextureID() {
        return textureID;
    }
}
