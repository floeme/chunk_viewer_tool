package fr.florian.engine.objects;

import fr.florian.engine.io.Input;
import fr.florian.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 * Represents a free-fly 3D camera controlled via keyboard and mouse input.
 * Supports first-person movement and mouse-based orientation.
 */
public class Camera {

	/** Current position of the camera in world space. */
	private Vector3f position;

	/** Current rotation of the camera. */
	private Vector3f rotation;

	/** Movement speed in units per frame. */
	private float moveSpeed = 0.05F;

	/** Mouse sensitivity for camera rotation. */
	private float mouseSensitivity = 0.15F;

	/** Previous mouse X position (used for calculating movement delta). */
	private double oldMouseX = 0;

	/** Previous mouse Y position. */
	private double oldMouseY = 0;

	/** Current mouse X position. */
	private double newMouseX = 0;

	/** Current mouse Y position. */
	private double newMouseY = 0;

	/**
	 * Constructs a new camera with a given position and rotation.
	 *
	 * @param position Initial camera position.
	 * @param rotation Initial camera rotation.
	 */
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	/**
	 * Updates the camera's position and rotation based on user input.
	 * WASD to move, SPACE/CTRL to move vertically, right mouse button to rotate.
	 */
	public void update() {
		newMouseX = Input.getMouseX();
		newMouseY = Input.getMouseY();

		// Calculate movement direction based on current rotation (yaw)
		float x = (float) (Math.sin(Math.toRadians(rotation.getY())) * moveSpeed);
		float z = (float) (Math.cos(Math.toRadians(rotation.getY())) * moveSpeed);

		// Movement controls (first-person style)
		if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0, x));
		if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0, -x));
		if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0, -z));
		if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0, z));
		if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = Vector3f.add(position, new Vector3f(0, moveSpeed, 0));
		if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) position = Vector3f.add(position, new Vector3f(0, -moveSpeed, 0));

		// Mouse control for rotation
		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			float dx = (float) (newMouseX - oldMouseX);
			float dy = (float) (newMouseY - oldMouseY);

			// Apply rotation based on mouse movement
			rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

			// Clamp pitch to avoid camera flipping
			if (rotation.getX() > 90) rotation = new Vector3f(90, rotation.getY(), rotation.getZ());
			if (rotation.getX() < -90) rotation = new Vector3f(-90, rotation.getY(), rotation.getZ());
		}

		// Store mouse position for the next frame
		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}

	/** @return The current camera position. */
	public Vector3f getPosition() {
		return position;
	}

	/** @return The current camera rotation. */
	public Vector3f getRotation() {
		return rotation;
	}
}
