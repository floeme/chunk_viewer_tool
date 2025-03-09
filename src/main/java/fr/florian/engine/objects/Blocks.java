package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.graphics.Vertex;
import fr.florian.engine.maths.Vector2f;
import fr.florian.engine.maths.Vector3f;

public class Blocks {
    public static Mesh createCubeMesh(Material material) {
        return new Mesh(new Vertex[]{
                // Face arrière
                new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),

                // Face avant
                new Vertex(new Vector3f(0.0f, 1.0f, 1.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 1.0f, 1.0f), new Vector2f(1.0f, 0.0f)),

                // Face droite
                new Vertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 1.0f, 1.0f), new Vector2f(1.0f, 0.0f)),

                // Face gauche
                new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.0f, 1.0f, 1.0f), new Vector2f(1.0f, 0.0f)),

                // Face supérieure
                new Vertex(new Vector3f(0.0f, 1.0f, 1.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 1.0f, 1.0f), new Vector2f(1.0f, 0.0f)),

                // Face inférieure
                new Vertex(new Vector3f(0.0f, 0.0f, 1.0f), new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(1.0f, 0.0f, 1.0f), new Vector2f(1.0f, 0.0f)),
        }, new int[]{
                // Face arrière (normale vers -Z)
                0, 3, 1, 3, 2, 1,

                // Face avant (normale vers +Z)
                4, 5, 7, 5, 6, 7,

                // Face droite (normale vers +X) - inversée pour la rendre visible
                8, 11, 9, 9, 11, 10,

                // Face gauche (normale vers -X)
                12, 13, 15, 15, 13, 14,

                // Face supérieure (normale vers +Y) - inversée pour la rendre visible
                16, 19, 17, 17, 19, 18,

                // Face inférieure (normale vers -Y)
                20, 21, 23, 23, 21, 22
        },
                material
        );
    }
}
