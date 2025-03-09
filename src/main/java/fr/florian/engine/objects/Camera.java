package fr.florian.engine.objects;

import fr.florian.engine.io.Input;
import fr.florian.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {
	private Vector3f position, rotation;
	private float moveSpeed = 0.05F, mouseSensitivity = 0.15F;
	private double oldMouseX=0, oldMouseY=0;
	private double newMouseX=0, newMouseY=0;


	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public void update() {
		newMouseX = Input.getMouseX();
		newMouseY = Input.getMouseY();

		float x = (float) (Math.sin(Math.toRadians(rotation.getY())) * moveSpeed);
		float z = (float) (Math.cos(Math.toRadians(rotation.getY())) * moveSpeed);

		if(Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0, x));
		if(Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0, -x));
		if(Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0, -z));
		if(Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0, z));
		if(Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = Vector3f.add(position, new Vector3f(0, moveSpeed, 0));
		if(Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) position = Vector3f.add(position, new Vector3f(0, -moveSpeed, 0));

		if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			float dx = (float) (newMouseX - oldMouseX);
			float dy = (float) (newMouseY - oldMouseY);
			//on applique la rotation
			rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));

			// limitation de  la rotation verticale
			if (rotation.getX() > 90) rotation = new Vector3f(90, rotation.getY(), rotation.getZ());
			if (rotation.getX() < -90) rotation = new Vector3f(-90, rotation.getY(), rotation.getZ());
		}
		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
}