package fr.florian.engine.io;

import org.lwjgl.glfw.*;

/**
 * Handles user input via keyboard, mouse, and scroll wheel using GLFW.
 * Stores input state and provides access to real-time values.
 */
public class Input {

	/**
	 * Keyboard key states.
	 * Indexed by GLFW key constants (e.g., GLFW_KEY_W, GLFW_KEY_ESCAPE).
	 */
	private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

	/**
	 * Mouse button states.
	 * Indexed by GLFW mouse button constants (e.g., GLFW_MOUSE_BUTTON_LEFT).
	 */
	private static boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

	/** Current horizontal mouse position. */
	private static double mouseX;

	/** Current vertical mouse position. */
	private static double mouseY;

	/** Horizontal scroll offset (accumulated). */
	private static double scrollX;

	/** Vertical scroll offset (accumulated). */
	private static double scrollY;

	/** Callback for keyboard key events. */
	private GLFWKeyCallback keyboard;

	/** Callback for mouse movement events. */
	private GLFWCursorPosCallback mouseMove;

	/** Callback for mouse button press/release events. */
	private GLFWMouseButtonCallback mouseButtons;

	/** Callback for mouse scroll wheel input. */
	private GLFWScrollCallback mouseScroll;

	/**
	 * Initializes all GLFW input callbacks.
	 */
	public Input() {
		keyboard = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key >= 0 && key < keys.length)
					keys[key] = (action != GLFW.GLFW_RELEASE);
			}
		};

		mouseMove = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mouseX = xpos;
				mouseY = ypos;
			}
		};

		mouseButtons = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (button >= 0 && button < buttons.length)
					buttons[button] = (action != GLFW.GLFW_RELEASE);

				// Toggle cursor visibility when right-clicking
				if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
					GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR,
							action == GLFW.GLFW_PRESS ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
				}
			}
		};

		mouseScroll = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double offsetx, double offsety) {
				scrollX += offsetx;
				scrollY += offsety;
			}
		};
	}

	/**
	 * Checks if a specific keyboard key is currently pressed.
	 *
	 * @param key GLFW key code.
	 * @return True if the key is down, false otherwise.
	 */
	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	/**
	 * Checks if a specific mouse button is currently pressed.
	 *
	 * @param button GLFW mouse button code.
	 * @return True if the button is down, false otherwise.
	 */
	public static boolean isButtonDown(int button) {
		return buttons[button];
	}

	/**
	 * Releases all GLFW input callbacks.
	 */
	public void destroy() {
		keyboard.free();
		mouseMove.free();
		mouseButtons.free();
		mouseScroll.free();
	}

	/**
	 * @return The current horizontal mouse position.
	 */
	public static double getMouseX() {
		return mouseX;
	}

	/**
	 * @return The current vertical mouse position.
	 */
	public static double getMouseY() {
		return mouseY;
	}

	/**
	 * @return Total horizontal scroll offset.
	 */
	public static double getScrollX() {
		return scrollX;
	}

	/**
	 * @return Total vertical scroll offset.
	 */
	public static double getScrollY() {
		return scrollY;
	}

	/**
	 * @return The GLFW key callback used by this input manager.
	 */
	public GLFWKeyCallback getKeyboardCallback() {
		return keyboard;
	}

	/**
	 * @return The GLFW cursor position callback.
	 */
	public GLFWCursorPosCallback getMouseMoveCallback() {
		return mouseMove;
	}

	/**
	 * @return The GLFW mouse button callback.
	 */
	public GLFWMouseButtonCallback getMouseButtonsCallback() {
		return mouseButtons;
	}

	/**
	 * @return The GLFW mouse scroll callback.
	 */
	public GLFWScrollCallback getMouseScrollCallback() {
		return mouseScroll;
	}

	/**
	 * @return True if the left mouse button is currently held down.
	 */
	public static boolean isLeftMouseDown() {
		return buttons[GLFW.GLFW_MOUSE_BUTTON_LEFT];
	}
}
