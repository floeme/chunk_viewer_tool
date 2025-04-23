package fr.florian.engine.objects;

import fr.florian.engine.graphics.Material;
import fr.florian.engine.graphics.Renderer;
import fr.florian.engine.maths.Vector3f;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents a chunk in the voxel world, composed of a 3D array of blocks.
 * Supports asynchronous block generation and incremental updates.
 */
public class Chunk {

    /** Width and height of the chunk (X and Z dimensions). */
    public static final int SIZE = 8;

    /** Vertical depth of the chunk (Y dimension). */
    public static final int DEPTH = SIZE * 3;

    /** Number of threads used for parallel generation. */
    private final int THREAD_COUNT = 8;

    /** 3D array storing the blocks of this chunk. */
    private final Block[][][] blocks = new Block[SIZE][DEPTH][SIZE];

    /** Queue of block creation tasks for deferred execution. */
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

    /** Thread pool for parallel chunk generation. */
    private final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

    /** Material used for the top layer (grass blocks). */
    private final Material grassMaterial;

    /** Material used for inner layers (dirt blocks). */
    private final Material dirtMaterial;

    /** Position of this chunk in the world. */
    private Vector3f chunkPosition;

    /**
     * Initializes the chunk at the given world position and starts parallel generation.
     *
     * @param position The world-space position of the chunk origin.
     */
    public Chunk(Vector3f position) {
        this.chunkPosition = position;
        this.grassMaterial = new Material("/textures/grassblock.png");
        this.dirtMaterial = new Material("/textures/dirtblock.png");

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> generateByX(threadId));
        }
    }

    /**
     * Generates a vertical slice of the chunk based on the X-axis (parallelized).
     */
    private void generateByX(int threadId) {
        int chunkSize = SIZE / THREAD_COUNT;
        int chunkStartX = threadId * chunkSize;
        int chunkEndX = Math.min(chunkStartX + chunkSize, SIZE);

        int chunkStartY = (int) chunkPosition.getZ();
        int chunkEndY = chunkStartY + DEPTH;

        int chunkStartZ = 0;
        int chunkEndZ = SIZE;

        for (int x = chunkStartX; x < chunkEndX; x++) {
            for (int y = chunkStartY; y < chunkEndY; y++) {
                for (int z = chunkStartZ; z < chunkEndZ; z++) {
                    int finalX = x, finalY = y, finalZ = z;
                    taskQueue.add(() -> createBlock(finalX, finalY, finalZ));
                }
            }
        }
    }

    /**
     * Alternative generation method using the Z-axis. (Unused by default)
     */
    private void generateByZ(int threadId) {
        int chunkSize = SIZE / THREAD_COUNT;
        int chunkStartZ = threadId * chunkSize;
        int chunkEndZ = Math.min(chunkStartZ + chunkSize, SIZE);

        int chunkStartX = 0;
        int chunkEndX = SIZE;

        int chunkStartY = (int) chunkPosition.getZ();
        int chunkEndY = chunkStartY + DEPTH;

        for (int x = chunkStartX; x < chunkEndX; x++) {
            for (int y = chunkStartY; y < chunkEndY; y++) {
                for (int z = chunkStartZ; z < chunkEndZ; z++) {
                    int finalX = x, finalY = y, finalZ = z;
                    taskQueue.add(() -> createBlock(finalX, finalY, finalZ));
                }
            }
        }
    }

    /**
     * Creates a block at the specified chunk coordinates and assigns the appropriate mesh.
     */
    private void createBlock(int x, int y, int z) {
        blocks[x][y][z] = new Block(
                x * y * z,
                new Vector3f(x, y, z),
                new Vector3f(0, 0, 0),
                new Vector3f(1, 1, 1),
                (y == DEPTH - 1) ? Blocks.createCubeMesh(grassMaterial) : Blocks.createCubeMesh(dirtMaterial)
        );
    }

    /**
     * Updates the chunk state:
     * - Executes a limited number of deferred block creation tasks.
     * - Calls update on each block.
     */
    public void update() {
        int maxTasksPerFrame = 1;
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

    /**
     * Renders all visible blocks in the chunk using the given renderer and camera.
     *
     * @param renderer The renderer to draw the mesh.
     * @param camera   The current scene camera.
     */
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

    /**
     * Determines whether a block is visible (i.e., on the edge or has an exposed face).
     *
     * @param x Block X position in the chunk.
     * @param y Block Y position in the chunk.
     * @param z Block Z position in the chunk.
     * @return True if the block should be rendered.
     */
    private boolean isVisible(int x, int y, int z) {
        if (x == 0 || y == 0 || z == 0 || x == SIZE - 1 || y == DEPTH - 1 || z == SIZE - 1) {
            return true;
        }

        return blocks[x - 1][y][z] == null || blocks[x + 1][y][z] == null ||
                blocks[x][y - 1][z] == null || blocks[x][y + 1][z] == null ||
                blocks[x][y][z - 1] == null || blocks[x][y][z + 1] == null;
    }
}
