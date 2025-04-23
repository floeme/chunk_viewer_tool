package fr.florian.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for loading files from resources as strings.
 * Commonly used for loading shader source code or configuration files.
 */
public class FileUtils {

    /**
     * Loads the content of a resource file and returns it as a string.
     *
     * @param filename The relative path to the file (e.g., "/shaders/vertex.glsl").
     * @return The entire content of the file as a single string.
     * @throws RuntimeException If the file cannot be found or read.
     */
    public static String loadAsString(String filename) {
        InputStream inputStream = FileUtils.class.getResourceAsStream(filename);
        if (inputStream == null) {
            throw new RuntimeException("File not found: " + filename);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + filename, e);
        }
    }
}
