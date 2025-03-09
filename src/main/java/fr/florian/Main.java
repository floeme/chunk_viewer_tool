package fr.florian;

import fr.florian.engine.graphics.Renderer;
import fr.florian.engine.graphics.Shader;
import fr.florian.engine.objects.*;
import fr.florian.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import fr.florian.engine.io.Window;
import fr.florian.engine.io.Input;

/**
 * Classe principale pour exécuter le moteur voxel.
 */
public class Main implements Runnable {
	/** Largeur de la fenêtre. */
	public final int WIDTH = 1920;//1280;

	/** Hauteur de la fenêtre. */
	public final int HEIGHT = 1080;//760;

	/** fenêtre principale. */
	public Window window;

	/** Thread principal du moteur. */
	public Thread game;

	/** Renderer du moteur. */
	public Renderer renderer;

	/** Shader du moteur */
	public Shader shader;

	public Chunk chunk1;

	public Camera camera = new Camera(new Vector3f(0,1,-1), new Vector3f(0,0,0));


	/** Démarre le thread principal. */
	public void start() {
		game = new Thread(this, "Voxel Engine");
		game.start();
	}

	/** Initialise la fenêtre et les paramètres. */
	public void init() {
		window = new Window(WIDTH, HEIGHT, "Voxel Engine");
		shader = new Shader("/shaders/vertex.glsl", "/shaders/fragment.glsl");
		renderer = new Renderer(window, shader);
		//guiRender = new GuiRender(window);

		window.setBackgroundColor(1.0f, 0, 0);
		window.create();
		shader.create();

		chunk1 = new Chunk(new Vector3f(0,-24,0));
	}


	public void run() {
		init();
		while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
			if(Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) window.mouseState(true);
		}
		close();
	}

	/** Met à jour l'état du jeu. */
	private void update() {
		chunk1.update();

		window.update();
		camera.update();

	}

	/** Rend l'image et affiche la prochaine frame. */
	private void render() {
		chunk1.render(renderer, camera);
		window.swapBuffers();
	}

	private void close() {
		window.destroy();
		shader.destroy();
	}

	public static void main(String[] args) {
		new Main().start();
	}
}