package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.graphics.Vertex;
import fr.florian.engine.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for building meshes dynamically from block faces.
 * Used in chunk generation to assemble only the visible faces of blocks.
 */
public class MeshBuilder {

    /** List of vertices that make up the mesh. */
    private final List<Vertex> vertices = new ArrayList<>();

    /** List of indices for rendering triangles from the vertex list. */
    private final List<Integer> indices = new ArrayList<>();

    /** Running index used for correctly indexing vertex data. */
    private int index = 0;

    /**
     * Adds the top face of a block to the mesh at the given position.
     */
    public void addTopFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{0, 1, 2, 2, 3, 0});
    }

    /**
     * Adds the bottom face of a block to the mesh at the given position.
     */
    public void addBottomFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{4, 5, 6, 6, 7, 4});
    }

    /**
     * Adds the right face of a block to the mesh at the given position.
     */
    public void addRightFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{8, 9, 10, 10, 11, 8});
    }

    /**
     * Adds the left face of a block to the mesh at the given position.
     */
    public void addLeftFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{12, 13, 14, 14, 15, 12});
    }

    /**
     * Adds the front face of a block to the mesh at the given position.
     */
    public void addFrontFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{16, 17, 18, 18, 19, 16});
    }

    /**
     * Adds the back face of a block to the mesh at the given position.
     */
    public void addBackFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{20, 21, 22, 22, 23, 20});
    }

    /**
     * Internal method to add a block face to the mesh.
     *
     * @param block       The block whose face is being added.
     * @param x           Block's X position in the world.
     * @param y           Block's Y position in the world.
     * @param z           Block's Z position in the world.
     * @param faceIndices Indices defining the face's triangles.
     */
    private void addFace(Block block, int x, int y, int z, int[] faceIndices) {
        // Add indices (offset by current index)
        for (int i = 0; i < faceIndices.length; i++) {
            indices.add(index + faceIndices[i]);
        }

        // Add translated vertices
        for (Vertex vertex : block.getMesh().getVertices()) {
            vertices.add(new Vertex(
                    new Vector3f(
                            vertex.getPosition().getX() + x,
                            vertex.getPosition().getY() + y,
                            vertex.getPosition().getZ() + z),
                    vertex.getTextureCoord()
            ));
        }

        // Update base index for next face
        index += block.getMesh().getVertices().length;
    }

    /**
     * Finalizes the mesh and returns a complete Mesh object with default material.
     *
     * @return The generated mesh with vertices, indices, and texture.
     */
    public Mesh build() {
        return new Mesh(
                vertices.toArray(new Vertex[0]),
                indices.stream().mapToInt(i -> i).toArray(),
                new Material("/textures/default.png")
        );
    }
}
