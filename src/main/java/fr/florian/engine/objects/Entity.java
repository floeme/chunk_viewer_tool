package fr.florian.engine.objects;

import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.maths.Vector3f;

public class Entity {
    private Vector3f position, rotation, scale;
    private Mesh mesh;

    public Entity(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.mesh = mesh;
    }

    public void update() {
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
