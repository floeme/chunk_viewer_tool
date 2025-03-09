package fr.florian.engine.objects;

import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.maths.Vector3f;

public class Block extends Entity{
    public static final Vector3f SIZE = new Vector3f(1, 1, 1);
    private final int id;  // id du bloc (ex : 0 = Air, 1 = Grass)

    public Block(int id, Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
        super(position, rotation, scale, mesh);
        this.id = id;
        mesh.create();
    }

    public int getId() {
        return id;
    }

    public boolean isAir() {
        return id == 0;
    }
}
