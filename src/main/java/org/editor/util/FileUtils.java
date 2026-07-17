package org.editor.util;

import java.io.*;

/**
 * File utility methods for the editor.
 */
public final class FileUtils {

    private FileUtils() {
    } // utility class

    /**
     * Read the entire content of a file into a string.
     */
    public static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        // Remove trailing newline if present
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Write string content to a file.
     */
    public static void writeFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    /**
     * Get the file extension (lowercase, without dot) from a file path.
     */
    public static String getFileExtension(String filePath) {
        if (filePath == null)
            return null;
        int dotIndex = filePath.lastIndexOf('.');
        int sepIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        if (dotIndex > sepIndex) {
            return filePath.substring(dotIndex + 1).toLowerCase();
        }
        return "txt";
    }

    /**
     * Get the file name (without extension) from a file path.
     */
    public static String getFileName(String filePath) {
        if (filePath == null)
            return "Untitled";
        int sepIndex = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        String name = filePath.substring(sepIndex + 1);
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0) {
            return name.substring(0, dotIndex);
        }
        return name;
    }
}
