package com.inventory.exception;

/**
 * Exception thrown when a requested resource is not found
 */
public class ResourceNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
