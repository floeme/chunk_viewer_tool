package fr.florian.engine.objects;

import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.maths.Vector3f;

/**
 * Represents a single block in a voxel world.
 * Extends the generic Entity class to include an identifier and block-specific logic.
 */
public class Block extends Entity {

    /** Constant size for all blocks (unit cube). */
    public static final Vector3f SIZE = new Vector3f(1, 1, 1);

    /** Unique identifier for the block (e.g., 0 = Air, 1 = Grass, etc.). */
    private final int id;

    /**
     * Constructs a new Block with a given ID, transform, and mesh.
     * Automatically creates the mesh (uploads it to GPU).
     *
     * @param id       The block's type ID.
     * @param position The world position of the block.
     * @param rotation The rotation of the block (usually zero).
     * @param scale    The scale of the block (typically 1,1,1).
     * @param mesh     The mesh to render the block.
     */
    public Block(int id, Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh) {
        super(position, rotation, scale, mesh);
        this.id = id;
        mesh.create();
    }

    /**
     * Returns the block's ID.
     *
     * @return The unique integer identifier of the block.
     */
    public int getId() {
        return id;
    }

    /**
     * Checks if this block is considered "air" (non-solid / invisible).
     *
     * @return True if the block is air (ID 0), false otherwise.
     */
    public boolean isAir() {
        return id == 0;
    }
}
