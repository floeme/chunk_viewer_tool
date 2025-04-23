package fr.florian.engine.graphics;

import fr.florian.engine.io.Window;
import fr.florian.engine.objects.Camera;
import fr.florian.engine.maths.Matrix4f;
import fr.florian.engine.objects.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

/**
 * Handles rendering of 3D entities using OpenGL and a provided shader.
 * Responsible for setting up shader uniforms and binding vertex data.
 */
public class Renderer {

	/** Shader used for rendering. */
	private Shader shader;

	/** Associated window for accessing the projection matrix. */
	private Window window;

	/**
	 * Constructs a new Renderer with the specified window and shader.
	 *
	 * @param window The rendering window.
	 * @param shader The shader used to draw meshes.
	 */
	public Renderer(Window window, Shader shader) {
		this.shader = shader;
		this.window = window;
	}

	/**
	 * Renders a single 3D entity using its mesh, transform, and material.
	 *
	 * @param entity The entity to render (position, rotation, scale, mesh).
	 * @param camera The active camera used to generate the view matrix.
	 */
	public void renderMesh(Entity entity, Camera camera) {
		// Bind the mesh's Vertex Array Object (VAO)
		GL30.glBindVertexArray(entity.getMesh().getVAO());

		// Enable vertex attribute arrays: positions (0), texture coords (1), normals (2)
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);

		// Bind index buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, entity.getMesh().getIBO());

		// Bind the mesh's texture to texture unit 0
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, entity.getMesh().getMaterial().getTextureID());

		// Activate the shader and upload transformation matrices
		shader.bind();
		shader.setUniform("model", Matrix4f.transform(entity.getPosition(), entity.getRotation(), entity.getScale()));
		shader.setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		shader.setUniform("projection", window.getProjectionMatrix());

		// Draw the mesh elements using triangles
		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);

		// Unbind the shader and reset state
		shader.unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}
