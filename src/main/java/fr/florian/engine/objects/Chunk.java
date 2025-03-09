package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Renderer;
import fr.florian.engine.maths.Vector3f;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Chunk {
    public static final int SIZE = 8;
    public static final int DEPTH = SIZE * 3;
    private final int THREAD_COUNT = 8;
    private final Block[][][] blocks = new Block[SIZE][DEPTH][SIZE];

    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    private final Material grassMaterial;
    private final Material dirtMaterial;

    private Vector3f chunkPosition;

    public Chunk(Vector3f position) {
        this.chunkPosition = position;
        this.grassMaterial = new Material("/textures/grassblock.png");
        this.dirtMaterial = new Material("/textures/dirtblock.png");

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> generateByX(threadId));
        }

    }

    // Génération basée sur l'axe X
    private void generateByX(int threadId) {
        int chunkSize = SIZE / THREAD_COUNT;
        int chunkStartX = threadId * chunkSize;
        int chunkEndX = chunkStartX + chunkSize;

        int chunkStartY = (int) chunkPosition.getZ();
        int chunkEndY = chunkStartY + DEPTH;

        int chunkStartZ = 0;
        int chunkEndZ = SIZE;

        chunkEndX = Math.min(chunkEndX, SIZE);

        for (int bx = chunkStartX; bx < chunkEndX; bx++) {
            for (int by = chunkStartY; by < chunkEndY; by++) {
                for (int bz = chunkStartZ; bz < chunkEndZ; bz++) {
                    if (bx < SIZE && by < DEPTH && bz < SIZE) {
                        int finalBx = bx, finalBy = by, finalBz = bz;
                        taskQueue.add(() -> createBlock(finalBx, finalBy, finalBz));
                    }
                }
            }
        }
    }

    // Génération basée sur l'axe Z
    private void generateByZ(int threadId) {
        int chunkSize = SIZE / THREAD_COUNT;
        int chunkStartZ = threadId * chunkSize;
        int chunkEndZ = chunkStartZ + chunkSize;

        int chunkStartX = 0;
        int chunkEndX = SIZE;

        int chunkStartY = (int) chunkPosition.getZ();
        int chunkEndY = chunkStartY + DEPTH;

        chunkEndZ = Math.min(chunkEndZ, SIZE);

        for (int bx = chunkStartX; bx < chunkEndX; bx++) {
            for (int by = chunkStartY; by < chunkEndY; by++) {
                for (int bz = chunkStartZ; bz < chunkEndZ; bz++) {
                    if (bx < SIZE && by < DEPTH && bz < SIZE) {
                        int finalBx = bx, finalBy = by, finalBz = bz;
                        taskQueue.add(() -> createBlock(finalBx, finalBy, finalBz));
                    }
                }
            }
        }
    }

    private void createBlock(int x, int y, int z) {
        blocks[x][y][z] = new Block(
                x * y * z,
                new Vector3f(x, y, z),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                (y == DEPTH - 1) ? Blocks.createCubeMesh(grassMaterial) : Blocks.createCubeMesh(dirtMaterial)
        );
    }

    public void update() {
        int maxTasksPerFrame = 1; // Limiter les tâches par frame pour la fluidité
        for (int i = 0; i < maxTasksPerFrame && !taskQueue.isEmpty(); i++) {
            Runnable task = taskQueue.poll();
            if (task != null) task.run();
        }

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < DEPTH; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (blocks[x][y][z] != null) {
                        blocks[x][y][z].update();
                    }
                }
            }
        }
    }

    public void render(Renderer renderer, Camera camera) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < DEPTH; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (blocks[x][y][z] != null && isVisible(x, y, z)) {
                        renderer.renderMesh(blocks[x][y][z], camera);
                    }
                }
            }
        }
    }

    private boolean isVisible(int x, int y, int z) {
        // Vérifier les bords du chunk
        if (x == 0 || y == 0 || z == 0 || x == SIZE - 1 || y == DEPTH - 1 || z == SIZE - 1) {
            return true; // Le bloc est sur le bord et donc visible
        }

        // Vérifier si le bloc a un voisin dans chaque direction (s'il est exposé)
        return blocks[x - 1][y][z] == null || blocks[x + 1][y][z] == null ||
                blocks[x][y - 1][z] == null || blocks[x][y + 1][z] == null ||
                blocks[x][y][z - 1] == null || blocks[x][y][z + 1] == null;
    }
}
