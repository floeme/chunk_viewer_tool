package fr.florian.engine.maths;

/**
 * A simple 2D vector class with float precision.
 * Supports common vector operations such as addition, subtraction, multiplication, division,
 * normalization, length computation, and dot product.
 */
public class Vector2f {

	/** The X component of the vector. */
	private float x;

	/** The Y component of the vector. */
	private float y;

	/**
	 * Constructs a 2D vector with the specified components.
	 *
	 * @param x X component.
	 * @param y Y component.
	 */
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Adds two vectors.
	 */
	public static Vector2f add(Vector2f vector1, Vector2f vector2) {
		return new Vector2f(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY());
	}

	/**
	 * Subtracts the second vector from the first.
	 */
	public static Vector2f subtract(Vector2f vector1, Vector2f vector2) {
		return new Vector2f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY());
	}

	/**
	 * Multiplies two vectors component-wise.
	 */
	public static Vector2f multiply(Vector2f vector1, Vector2f vector2) {
		return new Vector2f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY());
	}

	/**
	 * Divides two vectors component-wise.
	 */
	public static Vector2f divide(Vector2f vector1, Vector2f vector2) {
		return new Vector2f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY());
	}

	/**
	 * Returns the length (magnitude) of the vector.
	 */
	public static float length(Vector2f vector) {
		return (float) Math.sqrt(vector.getX() * vector.getX() + vector.getY() * vector.getY());
	}

	/**
	 * Returns a normalized (unit length) version of the given vector.
	 */
	public static Vector2f normalize(Vector2f vector) {
		float len = Vector2f.length(vector);
		return Vector2f.divide(vector, new Vector2f(len, len));
	}

	/**
	 * Computes the dot product of two vectors.
	 */
	public static float dot(Vector2f vector1, Vector2f vector2) {
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vector2f other = (Vector2f) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x) &&
				Float.floatToIntBits(y) == Float.floatToIntBits(other.y);
	}

	/** @return The X component of the vector. */
	public float getX() {
		return x;
	}

	/** Sets the X component of the vector. */
	public void setX(float x) {
		this.x = x;
	}

	/** @return The Y component of the vector. */
	public float getY() {
		return y;
	}

	/** Sets the Y component of the vector. */
	public void setY(float y) {
		this.y = y;
	}
}
