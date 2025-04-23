package fr.florian.engine.maths;

/**
 * A simple 3D vector class using float precision.
 * Provides basic arithmetic operations, normalization, and common vector math.
 */
public class Vector3f {

	/** The X component of the vector. */
	private float x;

	/** The Y component of the vector. */
	private float y;

	/** The Z component of the vector. */
	private float z;

	/**
	 * Constructs a 3D vector with the given components.
	 *
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @param z The Z coordinate.
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Sets all components of the vector at once.
	 *
	 * @param x New X value.
	 * @param y New Y value.
	 * @param z New Z value.
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Adds two vectors component-wise.
	 *
	 * @param vector1 First vector.
	 * @param vector2 Second vector.
	 * @return A new vector that is the sum of both.
	 */
	public static Vector3f add(Vector3f vector1, Vector3f vector2) {
		return new Vector3f(
				vector1.getX() + vector2.getX(),
				vector1.getY() + vector2.getY(),
				vector1.getZ() + vector2.getZ()
		);
	}

	/**
	 * Subtracts vector2 from vector1 component-wise.
	 *
	 * @param vector1 First vector (minuend).
	 * @param vector2 Second vector (subtrahend).
	 * @return A new vector representing the difference.
	 */
	public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
		return new Vector3f(
				vector1.getX() - vector2.getX(),
				vector1.getY() - vector2.getY(),
				vector1.getZ() - vector2.getZ()
		);
	}

	/**
	 * Multiplies two vectors component-wise.
	 *
	 * @param vector1 First vector.
	 * @param vector2 Second vector.
	 * @return A new vector with each component multiplied.
	 */
	public static Vector3f multiply(Vector3f vector1, Vector3f vector2) {
		return new Vector3f(
				vector1.getX() * vector2.getX(),
				vector1.getY() * vector2.getY(),
				vector1.getZ() * vector2.getZ()
		);
	}

	/**
	 * Divides two vectors component-wise.
	 *
	 * @param vector1 Dividend vector.
	 * @param vector2 Divisor vector.
	 * @return A new vector resulting from the division.
	 */
	public static Vector3f divide(Vector3f vector1, Vector3f vector2) {
		return new Vector3f(
				vector1.getX() / vector2.getX(),
				vector1.getY() / vector2.getY(),
				vector1.getZ() / vector2.getZ()
		);
	}

	/**
	 * Computes the length (magnitude) of the vector.
	 *
	 * @param vector The vector to evaluate.
	 * @return The length as a float.
	 */
	public static float length(Vector3f vector) {
		return (float) Math.sqrt(
				vector.getX() * vector.getX() +
						vector.getY() * vector.getY() +
						vector.getZ() * vector.getZ()
		);
	}

	/**
	 * Normalizes the vector (unit length).
	 *
	 * @param vector The vector to normalize.
	 * @return A new normalized vector.
	 */
	public static Vector3f normalize(Vector3f vector) {
		float len = Vector3f.length(vector);
		return Vector3f.divide(vector, new Vector3f(len, len, len));
	}

	/**
	 * Computes the dot product of two vectors.
	 *
	 * @param vector1 First vector.
	 * @param vector2 Second vector.
	 * @return The dot product as a float.
	 */
	public static float dot(Vector3f vector1, Vector3f vector2) {
		return vector1.getX() * vector2.getX() +
				vector1.getY() * vector2.getY() +
				vector1.getZ() * vector2.getZ();
	}

	/**
	 * Returns the X component.
	 *
	 * @return The X value.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the X component.
	 *
	 * @param x New X value.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the Y component.
	 *
	 * @return The Y value.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the Y component.
	 *
	 * @param y New Y value.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the Z component.
	 *
	 * @return The Z value.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the Z component.
	 *
	 * @param z New Z value.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Hash code based on vector components.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	/**
	 * Compares this vector with another for equality.
	 *
	 * @param obj The object to compare to.
	 * @return True if both vectors have identical components.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Vector3f other = (Vector3f) obj;
		return Float.floatToIntBits(x) == Float.floatToIntBits(other.x) &&
				Float.floatToIntBits(y) == Float.floatToIntBits(other.y) &&
				Float.floatToIntBits(z) == Float.floatToIntBits(other.z);
	}
}
