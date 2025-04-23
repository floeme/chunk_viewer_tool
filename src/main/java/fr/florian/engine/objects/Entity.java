package fr.florian.engine.objects;

import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.maths.Vector3f;

/**
 * Represents a 3D object in the scene with position, rotation, scale, and geometry.
 * This is a base class for renderable or interactive objects.
 */
public class Entity {

    /** The position of the entity in the 3D world. */
    private Vector3f position;

    /** The rotation of the entity (in degrees, per axis). */
    private Vector3f rotation;

    /** The scale of the entity along each axis. */
    private Vector3f scale;

    /** The mesh representing the entity's geometry. */
    private Mesh mesh;

    /**
     * Constructs a new Entity with the given transform and mesh.
     *
     * @param position The initial position.
     * @param rotation The initial rotation.
     * @param scale    The initial scale.
     * @param mesh     The mesh associated with this entity.
     */
    public Entity(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.mesh = mesh;
    }

    /**
     * Update method, intended to be overridden by subclasses if needed.
     * Used for animation, movement, or logic updates.
     */
    public void update() {
        // Placeholder for logic updates
    }

    /** @return The current position of the entity. */
    public Vector3f getPosition() {
        return position;
    }

    /** @return The current rotation of the entity. */
    public Vector3f getRotation() {
        return rotation;
    }

    /** @return The current scale of the entity. */
    public Vector3f getScale() {
        return scale;
    }

    /** @return The mesh associated with this entity. */
    public Mesh getMesh() {
        return mesh;
    }
}
