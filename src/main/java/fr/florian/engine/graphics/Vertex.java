package fr.florian.engine.graphics;

import fr.florian.engine.maths.Vector2f;
import fr.florian.engine.maths.Vector3f;

/**
 * Represents a single vertex in 3D space, containing both position and texture coordinate data.
 * Used in mesh construction for rendering geometry.
 */
public class Vertex {

	/** 3D position of the vertex. */
	private Vector3f position;

	/** 2D texture coordinate associated with the vertex. */
	private Vector2f textureCoord;

	/**
	 * Constructs a new vertex with a given position and texture coordinate.
	 *
	 * @param position      The 3D position of the vertex.
	 * @param textureCoord  The 2D texture coordinates (UV) for texturing.
	 */
	public Vertex(Vector3f position, Vector2f textureCoord) {
		this.position = position;
		this.textureCoord = textureCoord;
	}

	/**
	 * Returns the 3D position of the vertex.
	 *
	 * @return A {@link Vector3f} representing the vertex's position.
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * Returns the 2D texture coordinate (UV) of the vertex.
	 *
	 * @return A {@link Vector2f} representing the vertex's texture coordinate.
	 */
	public Vector2f getTextureCoord() {
		return textureCoord;
	}
}
