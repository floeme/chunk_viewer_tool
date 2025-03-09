package fr.florian.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    public static String loadAsString(String filename) {
        InputStream inputStream = FileUtils.class.getResourceAsStream(filename);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
