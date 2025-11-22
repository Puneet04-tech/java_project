package com.inventory.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for application logging
 */
public class Logger {
    private static final String LOG_FILE = "logs/application.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public enum LogLevel {
        INFO, WARNING, ERROR, DEBUG
    }
    
    /**
     * Log a message with specified level
     */
    public static void log(LogLevel level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format("[%s] [%s] %s", timestamp, level, message);
        
        // Print to console
        System.out.println(logEntry);
        
        // Write to file
        try {
            FileHandler.appendTextToFile(LOG_FILE, logEntry);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
    
    /**
     * Log info message
     */
    public static void info(String message) {
        log(LogLevel.INFO, message);
    }
    
    /**
     * Log warning message
     */
    public static void warning(String message) {
        log(LogLevel.WARNING, message);
    }
    
    /**
     * Log error message
     */
    public static void error(String message) {
        log(LogLevel.ERROR, message);
    }
    
    /**
     * Log error with exception
     */
    public static void error(String message, Exception e) {
        String fullMessage = message + " - " + e.getClass().getSimpleName() + ": " + e.getMessage();
        log(LogLevel.ERROR, fullMessage);
    }
    
    /**
     * Log debug message
     */
    public static void debug(String message) {
        log(LogLevel.DEBUG, message);
    }
    
    /**
     * Log user action
     */
    public static void logUserAction(String username, String action) {
        String message = String.format("User '%s' performed: %s", username, action);
        info(message);
    }
}
