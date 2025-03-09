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
 * Classe Window
 * <p>
 * Cette classe gère la création, l'affichage et la gestion d'une fenêtre GLFW.
 * Elle prend en charge les événements d'entrée (clavier, souris) et le mode plein écran.
 */
public class Window {

    /** Largeur de la fenêtre en pixels. */
    private int width;

    /** Hauteur de la fenêtre en pixels. */
    private int height;

    /** Titre de la fenêtre. */
    private String title;

    /** Identifiant de la fenêtre GLFW. */
    private long window;

    /** Nombre d'images rendues en une seconde (FPS). */
    private int frames;

    /** Temps de référence en millisecondes pour le calcul des FPS. */
    private static long time;

    /** Gestionnaire des entrées clavier et souris. */
    private Input input;

    /** Composantes couleur de fond RGB. */
    private Vector3f background = new Vector3f(0, 0, 0);

    /** Callback pour gérer le redimensionnement de la fenêtre. */
    private GLFWWindowSizeCallback sizeCallback;

    /** Indique si la fenêtre a été redimensionnée. */
    private boolean isResized;

    /** Indique si la fenêtre est en mode plein écran. */
    private boolean isFullscreen;

    /** Position X de la fenêtre avant le passage en plein écran. */
    private int[] windowPosX = new int[1];

    /** Position Y de la fenêtre avant le passage en plein écran. */
    private int[] windowPosY = new int[1];

    /** Matrice de projection pour passer de la 3D à la 2D. */
    private Matrix4f projectionMatrix;

    /**
     * Constructeur de la classe Window.
     *
     * @param width  Largeur de la fenêtre.
     * @param height Hauteur de la fenêtre.
     * @param title  Titre de la fenêtre.
     */
    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        projectionMatrix = Matrix4f.projection(70.0f, (float) width / (float) height, 0.1f, 1000.0f);
    }

    /**
     * Crée la fenêtre GLFW et initialise le contexte OpenGL.
     */
    public void create() {
        if (!GLFW.glfwInit()) {
            System.err.println("ERROR: GLFW wasn't initializied");
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
     * Crée et enregistre les callbacks pour gérer les événements de la fenêtre.
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
     * Met à jour l'état de la fenêtre (FPS, événements, etc.).
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
     * Échange les buffers pour afficher l'image suivante.
     */
    public void swapBuffers() {
        GLFW.glfwSwapBuffers(window);
    }

    /**
     * Vérifie si la fenêtre doit être fermée.
     *
     * @return true si la fenêtre doit être fermée, false sinon.
     */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    /**
     * Détruit la fenêtre et libère les ressources associées.
     */
    public void destroy() {
		input.destroy();
        sizeCallback.free();

        GLFW.glfwWindowShouldClose(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    /**
     * Modifie la couleur de fond de la fenêtre.
     *
     * @param r Composante rouge.
     * @param g Composante verte.
     * @param b Composante bleue.
     */
    public void setBackgroundColor(float r, float g, float b) {
        background.set(r,g,b);
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }


    /**
     * Permet de mettre la fenêtre en plein écran.
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


    public void mouseState(boolean lock){
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, lock ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Renvoie l'identifiant de la fenêtre GLFW.
     *
     * @return Identifiant de la fenêtre.
     */
    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}