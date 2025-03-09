package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Mesh;
import fr.florian.engine.graphics.Vertex;
import fr.florian.engine.maths.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MeshBuilder {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Integer> indices = new ArrayList<>();
    private int index = 0;

    public void addTopFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{0, 1, 2, 2, 3, 0});
    }

    public void addBottomFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{4, 5, 6, 6, 7, 4});
    }

    public void addRightFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{8, 9, 10, 10, 11, 8});
    }

    public void addLeftFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{12, 13, 14, 14, 15, 12});
    }

    public void addFrontFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{16, 17, 18, 18, 19, 16});
    }

    public void addBackFace(Block block, int x, int y, int z) {
        addFace(block, x, y, z, new int[]{20, 21, 22, 22, 23, 20});
    }

    private void addFace(Block block, int x, int y, int z, int[] faceIndices) {
        for (int i = 0; i < faceIndices.length; i++) {
            indices.add(index + faceIndices[i]);
        }
        for (Vertex vertex : block.getMesh().getVertices()) {
            vertices.add(new Vertex(
                    new Vector3f(vertex.getPosition().getX() + x,
                                vertex.getPosition().getY() + y,
                                vertex.getPosition().getZ() + z),
                    vertex.getTextureCoord()
            ));
        }
        index += block.getMesh().getVertices().length;
    }

    public Mesh build() {
        return new Mesh(vertices.toArray(new Vertex[0]), indices.stream().mapToInt(i -> i).toArray(), new Material("/textures/default.png"));
    }
}
