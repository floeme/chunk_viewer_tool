package fr.florian.engine.io;


import fr.florian.engine.maths.Matrix4f;
import fr.florian.engine.maths.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * The Window class handles the creation, display, and management of a GLFW window.
 * It also manages input events (keyboard, mouse), fullscreen mode, and OpenGL initialization.
 */
public class Window {

    /** Window width in pixels. */
    private int width;

    /** Window height in pixels. */
    private int height;

    /** Title of the window. */
    private String title;

    /** GLFW window handle. */
    private long window;

    /** Number of frames rendered in the last second. */
    private int frames;

    /** Time reference for FPS calculation (in milliseconds). */
    private static long time;

    /** Input handler for keyboard and mouse events. */
    private Input input;

    /** RGB background color. */
    private Vector3f background = new Vector3f(0, 0, 0);

    /** GLFW callback for window resize events. */
    private GLFWWindowSizeCallback sizeCallback;

    /** Indicates if the window has been resized. */
    private boolean isResized;

    /** Indicates if the window is currently in fullscreen mode. */
    private boolean isFullscreen;

    /** Window X position before switching to fullscreen. */
    private int[] windowPosX = new int[1];

    /** Window Y position before switching to fullscreen. */
    private int[] windowPosY = new int[1];

    /** Projection matrix for 3D rendering. */
    private Matrix4f projectionMatrix;

    /**
     * Creates a new window with the given size and title.
     *
     * @param width  Window width in pixels.
     * @param height Window height in pixels.
     * @param title  Title shown in the window title bar.
     */
    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.projectionMatrix = Matrix4f.projection(70.0f, (float) width / height, 0.1f, 1000.0f);
    }

    /**
     * Initializes GLFW, creates the window, and sets up the OpenGL context and callbacks.
     */
    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initialized");
            return;
        }

        input = new Input();
        window = GLFW.glfwCreateWindow(width, height, title, isFullscreen ? GLFW.glfwGetPrimaryMonitor() : 0, 0);

        if (window == 0) {
            System.err.println("ERROR: Window wasn't created");
            return;
        }

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        windowPosX[0] = (videoMode.width() - width) / 2;
        windowPosY[0] = (videoMode.height() - height) / 2;
        GLFW.glfwSetWindowPos(window, windowPosX[0], windowPosY[0]);

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);

        createCallbacks();

        GLFW.glfwShowWindow(window);
        GLFW.glfwSwapInterval(1);
        time = System.currentTimeMillis();
    }

    /**
     * Sets up the necessary GLFW callbacks (resize, input).
     */
    private void createCallbacks() {
        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };

        GLFW.glfwSetKeyCallback(window, input.getKeyboardCallback());
        GLFW.glfwSetCursorPosCallback(window, input.getMouseMoveCallback());
        GLFW.glfwSetMouseButtonCallback(window, input.getMouseButtonsCallback());
        GLFW.glfwSetScrollCallback(window, input.getMouseScrollCallback());
        GLFW.glfwSetWindowSizeCallback(window, sizeCallback);
    }

    /**
     * Updates the window: handles resizing, clears buffers, polls input events, and updates FPS.
     */
    public void update() {
        if (isResized) {
            GL11.glViewport(0, 0, width, height);
            isResized = false;
        }

        GL11.glClearColor(background.getX(), background.getY(), background.getZ(), 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GLFW.glfwPollEvents();
        frames++;

        if (System.currentTimeMillis() > time + 1000) {
            GLFW.glfwSetWindowTitle(window, title + " | FPS: " + frames);
            time = System.currentTimeMillis();
            frames = 0;
        }
    }

    /**
     * Swaps the front and back buffers, displaying the rendered frame.
     */
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    /**
     * Checks whether the window should close.
     *
     * @return true if the user has requested to close the window.
     */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    /**
     * Destroys the window and releases all resources.
     */
    public void destroy() {
        input.destroy();
        sizeCallback.free();

        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    /**
     * Sets the background clear color.
     *
     * @param r Red component (0.0–1.0).
     * @param g Green component (0.0–1.0).
     * @param b Blue component (0.0–1.0).
     */
    public void setBackgroundColor(float r, float g, float b) {
        background.set(r, g, b);
    }

    /**
     * Returns whether the window is in fullscreen mode.
     */
    public boolean isFullscreen() {
        return isFullscreen;
    }

    /**
     * Toggles fullscreen mode on or off.
     *
     * @param isFullscreen True to enable fullscreen mode.
     */
    public void setFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        isResized = true;

        if (isFullscreen) {
            GLFW.glfwGetWindowPos(window, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(window, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }
    }

    /**
     * Locks or unlocks the mouse cursor.
     *
     * @param lock True to lock the cursor (e.g., for FPS camera mode).
     */
    public void mouseState(boolean lock) {
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }

    /** @return The current window width in pixels. */
    public int getWidth() {
        return width;
    }

    /** @return The current window height in pixels. */
    public int getHeight() {
        return height;
    }

    /** @return The title of the window. */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the native GLFW window handle.
     *
     * @return The window handle.
     */
    public long getWindow() {
        return window;
    }

    /**
     * Returns the current projection matrix for 3D rendering.
     *
     * @return The projection matrix.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
