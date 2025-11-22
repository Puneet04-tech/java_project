package com.inventory.util;

import java.io.*;
import java.nio.file.*;

/**
 * Utility class for file operations including serialization
 */
public class FileHandler {
    
    /**
     * Write an object to a file using serialization
     */
    public static void writeToFile(String filename, Object data) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs(); // Create directories if they don't exist
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        }
    }
    
    /**
     * Read an object from a file using deserialization
     */
    public static Object readFromFile(String filename) throws IOException, ClassNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        }
    }
    
    /**
     * Write text content to a file
     */
    public static void writeTextToFile(String filename, String content) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
    
    /**
     * Append text content to a file
     */
    public static void appendTextToFile(String filename, String content) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(content);
            writer.newLine();
        }
    }
    
    /**
     * Read text content from a file
     */
    public static String readTextFromFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return "";
        }
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * Check if file exists
     */
    public static boolean fileExists(String filename) {
        return new File(filename).exists();
    }
    
    /**
     * Delete a file
     */
    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        return file.delete();
    }
    
    /**
     * Create backup of a file
     */
    public static void backupFile(String filename) throws IOException {
        File source = new File(filename);
        if (source.exists()) {
            String backupName = filename + ".backup";
            Files.copy(source.toPath(), Paths.get(backupName), 
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
