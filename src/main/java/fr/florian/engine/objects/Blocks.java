package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.graphics.Vertex;
import fr.florian.engine.maths.Vector2f;
import fr.florian.engine.maths.Vector3f;

/**
 * Utility class for creating standard block meshes.
 * Provides a method to generate a textured cube mesh used by voxel blocks.
 */
public class Blocks {

    /**
     * Creates a cube mesh with the given material.
     * The cube is made of 6 faces, each composed of 4 vertices and 2 triangles.
     *
     * @param material The texture material applied to the cube.
     * @return A Mesh representing a unit cube (1x1x1) with texture coordinates.
     */
    public static Mesh createCubeMesh(Material material) {
        return new Mesh(
                new Vertex[]{
                        // Back face (-Z)
                        new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 0)),

                        // Front face (+Z)
                        new Vertex(new Vector3f(0, 1, 1), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(0, 0, 1), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(1, 1, 1), new Vector2f(1, 0)),

                        // Right face (+X)
                        new Vertex(new Vector3f(1, 1, 0), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(1, 0, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(1, 1, 1), new Vector2f(1, 0)),

                        // Left face (-X)
                        new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(0, 0, 1), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(0, 1, 1), new Vector2f(1, 0)),

                        // Top face (+Y)
                        new Vertex(new Vector3f(0, 1, 1), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(0, 1, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(1, 1, 0), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(1, 1, 1), new Vector2f(1, 0)),

                        // Bottom face (-Y)
                        new Vertex(new Vector3f(0, 0, 1), new Vector2f(0, 0)),
                        new Vertex(new Vector3f(0, 0, 0), new Vector2f(0, 1)),
                        new Vertex(new Vector3f(1, 0, 0), new Vector2f(1, 1)),
                        new Vertex(new Vector3f(1, 0, 1), new Vector2f(1, 0)),
                },
                new int[]{
                        // Back face
                        0, 3, 1, 3, 2, 1,
                        // Front face
                        4, 5, 7, 5, 6, 7,
                        // Right face (wound to ensure correct facing)
                        8, 11, 9, 9, 11, 10,
                        // Left face
                        12, 13, 15, 15, 13, 14,
                        // Top face (wound for correct normal direction)
                        16, 19, 17, 17, 19, 18,
                        // Bottom face
                        20, 21, 23, 23, 21, 22
                },
                material
        );
    }
}
