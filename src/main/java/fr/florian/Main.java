package fr.florian;

import fr.florian.engine.graphics.Renderer;
import fr.florian.engine.graphics.Shader;
import fr.florian.engine.objects.Chunk;
import fr.florian.engine.objects.Camera;
import fr.florian.engine.maths.Vector3f;
import fr.florian.engine.io.Window;
import fr.florian.engine.io.Input;
import org.lwjgl.glfw.GLFW;

/**
 * Main class for the 3D chunk generation visualizer.
 * Initializes the window, shaders, camera, and chunk,
 * and manages the main update and render loop.
 */
public class Main implements Runnable {

	/** Window width in pixels. */
	public final int WIDTH = 1280;

	/** Window height in pixels. */
	public final int HEIGHT = 760;

	/** The main application window. */
	public Window window;

	/** The game engine thread. */
	public Thread game;

	/** The renderer responsible for drawing the scene. */
	public Renderer renderer;

	/** The shader used for rendering. */
	public Shader shader;

	/** A single chunk displayed in the scene. */
	public Chunk chunk1;

	/** The camera used to view and navigate the 3D scene. */
	public Camera camera = new Camera(new Vector3f(0, 1, -1), new Vector3f(0, 0, 0));

	/**
	 * Starts the engine in a new thread.
	 */
	public void start() {
		game = new Thread(this, "Voxel Engine");
		game.start();
	}

	/**
	 * Initializes the window, shader, renderer, and the first chunk.
	 */
	public void init() {
		window = new Window(WIDTH, HEIGHT, "Voxel Engine");
		shader = new Shader("/shaders/vertex.glsl", "/shaders/fragment.glsl");
		renderer = new Renderer(window, shader);

		window.setBackgroundColor(1.0f, 0.0f, 0.0f); // red background
		window.create();
		shader.create();

		chunk1 = new Chunk(new Vector3f(0, -24, 0));
	}

	/**
	 * The main loop: updates and renders while the window is open.
	 */
	@Override
	public void run() {
		init();
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();

			// Toggle fullscreen mode
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) {
				window.setFullscreen(!window.isFullscreen());
			}

			// Lock mouse input when left button is pressed
			if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				window.mouseState(true);
			}
		}
		close();
	}

	/**
	 * Updates the chunk, camera, and input handling.
	 */
	private void update() {
		chunk1.update();
		window.update();
		camera.update();
	}

	/**
	 * Renders the chunk and swaps the window buffers.
	 */
	private void render() {
		chunk1.render(renderer, camera);
		window.swapBuffers();
	}

	/**
	 * Frees up resources (window and shader) before exiting.
	 */
	private void close() {
		window.destroy();
		shader.destroy();
	}

	/**
	 * Program entry point.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		new Main().start();
	}
}
