package fr.florian.engine.io;

import fr.florian.engine.objects.Camera;
import org.lwjgl.glfw.*;

/**
 * Classe gérant les entrées utilisateur (clavier, souris, molette) avec GLFW.
 */
public class Input {
	/**
	 * État des touches du clavier.
	 * Indexé par les constantes GLFW (ex : GLFW_KEY_W, GLFW_KEY_ESCAPE, etc.).
	 */
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

	/**
	 * État des boutons de la souris.
	 * Indexé par les constantes GLFW (ex : GLFW_MOUSE_BUTTON_LEFT, etc.).
	 */
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	/**
	 * Position actuelle de la souris sur l'axe X (horizontal).
	 */
	private static double mouseX;

	/**
	 * Position actuelle de la souris sur l'axe Y (vertical).
	 */
	private static double mouseY;

	/**
	 * Défilement horizontal de la molette de la souris.
	 */
	private static double scrollX;

	/**
	 * Défilement vertical de la molette de la souris.
	 */
	private static double scrollY;

	/**
	 * Callback pour la gestion des entrées clavier.
	 */
	private GLFWKeyCallback keyboard;

	/**
	 * Callback pour la gestion des mouvements de la souris.
	 */
	private GLFWCursorPosCallback mouseMove;

	/**
	 * Callback pour la gestion des clics de la souris.
	 */
	private GLFWMouseButtonCallback mouseButtons;

	/**
	 * Callback pour la gestion du défilement de la molette de la souris.
	 */
	private GLFWScrollCallback mouseScroll;


	/**
	 * Initialise les callbacks d'entrée pour le clavier, la souris et la molette.
	 */
	public Input() {
		keyboard = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};
		
		mouseMove = new GLFWCursorPosCallback() {
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};
		
		mouseButtons = new GLFWMouseButtonCallback() {
			public void invoke(long window, int button, int action, int mods) {
				buttons[button] = (action != GLFW.GLFW_RELEASE);

				// on masque ou réaffiche le curseur de la souris lors de lappuie souris.
				if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
					if (action == GLFW.GLFW_PRESS) {
						GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
					} else if (action == GLFW.GLFW_RELEASE) {
						GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
					}
				}
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}

	/**
	 * Vérifie si une touche du clavier est actuellement enfoncée.
	 *
	 * @param key Code de la touche (ex : GLFW_KEY_W, GLFW_KEY_ESCAPE).
	 * @return true si la touche est enfoncée, false sinon.
	 */
	public static boolean isKeyDown(int key) {
		return keys[key];
	}


	/**
	 * Vérifie si un bouton de la souris est actuellement enfoncé.
	 *
	 * @param button Code du bouton (ex : GLFW_MOUSE_BUTTON_LEFT).
	 * @return true si le bouton est enfoncé, false sinon.
	 */
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}


	/**
	 * Libère les ressources associées aux callbacks GLFW.
	 */
	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}

	/**
	 * Obtient la position actuelle de la souris sur l'axe X.
	 *
	 * @return Coordonnée X de la souris.
	 */
	public static double getMouseX() {
		return mouseX;
	}

	/**
	 * Obtient la position actuelle de la souris sur l'axe Y.
	 *
	 * @return Coordonnée Y de la souris.
	 */
	public static double getMouseY() {
		return mouseY;
	}


	/**
	 * Obtient le défilement horizontal de la molette de la souris.
	 *
	 * @return Valeur du défilement horizontal.
	 */
	public static double getScrollX() {
		return scrollX;
	}

	/**
	 * Obtient le défilement vertical de la molette de la souris.
	 *
	 * @return Valeur du défilement vertical.
	 */
	public static double getScrollY() {
		return scrollY;
	}

	/**
	 * Retourne le callback du clavier.
	 *
	 * @return Instance de {@link GLFWKeyCallback}.
	 */
	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	/**
	 * Retourne le callback de la position de la souris.
	 *
	 * @return Instance de {@link GLFWCursorPosCallback}.
	 */
	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	/**
	 * Retourne le callback des boutons de la souris.
	 *
	 * @return Instance de {@link GLFWMouseButtonCallback}.
	 */
	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}

	/**
	 * Retourne le callback de la molette de la souris.
	 *
	 * @return Instance de {@link GLFWScrollCallback}.
	 */
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}

	public static boolean isLeftMouseDown() {
		return buttons[GLFW.GLFW_MOUSE_BUTTON_LEFT];
	}
}