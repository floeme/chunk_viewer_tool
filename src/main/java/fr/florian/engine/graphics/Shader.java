package fr.florian.engine.graphics;

import fr.florian.engine.utils.FileUtils;
import fr.florian.engine.maths.Matrix4f;
import fr.florian.engine.maths.Vector2f;
import fr.florian.engine.maths.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * Manages an OpenGL shader program composed of a vertex and fragment shader.
 * Handles compilation, linking, binding, and uniform uploading.
 */
public class Shader {

    /** Source code of the vertex shader (GLSL). */
    private String vertexFile;

    /** Source code of the fragment shader (GLSL). */
    private String fragmentFile;

    /** OpenGL ID for the compiled vertex shader. */
    private int vertexID;

    /** OpenGL ID for the compiled fragment shader. */
    private int fragmentID;

    /** OpenGL ID for the final linked shader program. */
    private int programID;

    /**
     * Loads shader source code from specified files.
     *
     * @param vertexPath   Path to the vertex shader file.
     * @param fragmentPath Path to the fragment shader file.
     */
    public Shader(String vertexPath, String fragmentPath) {
        vertexFile = FileUtils.loadAsString(vertexPath);
        fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    /**
     * Compiles the vertex and fragment shaders and links them into a shader program.
     * Prints errors to stderr if compilation or linking fails.
     */
    public void create() {
        programID = GL20.glCreateProgram();

        // Compile vertex shader
        vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vertexID, vertexFile);
        GL20.glCompileShader(vertexID);
        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID));
            return;
        }

        // Compile fragment shader
        fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fragmentID, fragmentFile);
        GL20.glCompileShader(fragmentID);
        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID));
            return;
        }

        // Link program
        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID));
            return;
        }

        // Validate program
        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programID));
        }
    }

    /**
     * Gets the location of a uniform variable in the shader.
     *
     * @param name Name of the uniform.
     * @return The uniform's location ID.
     */
    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    /** Uploads a float uniform to the shader. */
    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    /** Uploads an int uniform to the shader. */
    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    /** Uploads a boolean uniform to the shader (as integer). */
    public void setUniform(String name, boolean value) {
        GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
    }

    /** Uploads a 2D vector uniform to the shader. */
    public void setUniform(String name, Vector2f value) {
        GL20.glUniform2f(getUniformLocation(name), value.getX(), value.getY());
    }

    /** Uploads a 3D vector uniform to the shader. */
    public void setUniform(String name, Vector3f value) {
        GL20.glUniform3f(getUniformLocation(name), value.getX(), value.getY(), value.getZ());
    }

    /** Uploads a 4x4 matrix uniform to the shader. */
    public void setUniform(String name, Matrix4f value) {
        FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
        matrix.put(value.getAll()).flip();
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
        MemoryUtil.memFree(matrix);
    }

    /**
     * Activates the shader program for rendering.
     */
    public void bind() {
        GL20.glUseProgram(programID);
    }

    /**
     * Deactivates the shader program.
     */
    public void unbind() {
        GL20.glUseProgram(0);
    }

    /**
     * Deletes the shader program and its attached shaders from the GPU.
     */
    public void destroy() {
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragmentID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragmentID);
        GL20.glDeleteProgram(programID);
    }
}
