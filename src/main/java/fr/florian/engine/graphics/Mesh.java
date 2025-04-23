package fr.florian.engine.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

/**
 * Represents a 3D mesh made up of vertices, indices, and a texture material.
 * Handles GPU resource allocation (VAO, VBOs, IBO) and buffer uploads.
 */
public class Mesh {

	/** List of vertices composing the geometry. */
	private Vertex[] vertices;

	/** Index data used to define the mesh's triangles. */
	private int[] indices;

	/** Material containing the texture and rendering properties. */
	private Material material;

	/** OpenGL ID of the Vertex Array Object (VAO). */
	private int vao;

	/** OpenGL ID of the position buffer (VBO). */
	private int pbo;

	/** OpenGL ID of the (optional) color buffer. */
	private int cbo;

	/** OpenGL ID of the index buffer (IBO / EBO). */
	private int ibo;

	/** OpenGL ID of the texture coordinates buffer (UVs). */
	private int tbo;

	/**
	 * Constructs a new Mesh with the given vertex data, indices, and material.
	 *
	 * @param vertices The array of vertex objects.
	 * @param indices The index array defining triangles.
	 * @param material The material used for rendering the mesh.
	 */
	public Mesh(Vertex[] vertices, int[] indices, Material material) {
		this.vertices = vertices;
		this.indices = indices;
		this.material = material;
	}

	/**
	 * Creates the OpenGL VAO, VBOs and IBO, and uploads vertex/index data to GPU memory.
	 */
	public void create() {
		System.out.println("Creating Mesh on Thread: " + Thread.currentThread().getName());

		material.create();

		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		// Upload position data
		FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			positionData[i * 3]     = vertices[i].getPosition().getX();
			positionData[i * 3 + 1] = vertices[i].getPosition().getY();
			positionData[i * 3 + 2] = vertices[i].getPosition().getZ();
		}
		positionBuffer.put(positionData).flip();
		pbo = storeData(positionBuffer, 0, 3);

		// Texture coordinates (UVs)
		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
		float[] textureData = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			textureData[i * 2]     = vertices[i].getTextureCoord().getX();
			textureData[i * 2 + 1] = vertices[i].getTextureCoord().getY();
		}
		textureBuffer.put(textureData).flip();
		tbo = storeData(textureBuffer, 2, 2);

		// Upload indices
		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();
		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	/**
	 * Stores a float buffer in a VBO and binds it to the specified attribute index.
	 *
	 * @param buffer The buffer containing vertex attribute data.
	 * @param index The location index in the shader (layout = index).
	 * @param size The number of components per vertex (e.g., 3 for vec3).
	 * @return The OpenGL buffer ID.
	 */
	private int storeData(FloatBuffer buffer, int index, int size) {
		int bufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return bufferID;
	}

	/**
	 * Frees all GPU resources associated with this mesh (VBOs, VAO, material).
	 */
	public void destroy() {
		GL15.glDeleteBuffers(pbo);
		GL15.glDeleteBuffers(cbo);  // Not used, but declared for potential future use
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(tbo);

		GL30.glDeleteVertexArrays(vao);
		material.destroy();
	}

	/** @return The vertex array. */
	public Vertex[] getVertices() {
		return vertices;
	}

	/** @return The index array. */
	public int[] getIndices() {
		return indices;
	}

	/** @return The VAO ID. */
	public int getVAO() {
		return vao;
	}

	/** @return The Position Buffer Object ID. */
	public int getPBO() {
		return pbo;
	}

	/** @return The Color Buffer Object ID (not used currently). */
	public int getCBO() {
		return cbo;
	}

	/** @return The Texture Buffer Object ID. */
	public int getTbo() {
		return tbo;
	}

	/** @return The Index Buffer Object ID. */
	public int getIBO() {
		return ibo;
	}

	/** @return The material used for texturing this mesh. */
	public Material getMaterial() {
		return material;
	}
}
