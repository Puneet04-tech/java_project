package com.inventory.util;

import com.inventory.exception.ValidationException;
import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class Validator {
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[0-9]{10}$");
    
    /**
     * Validate that string is not null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
    }
    
    /**
     * Validate positive number
     */
    public static void validatePositive(double value, String fieldName) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be positive");
        }
    }
    
    /**
     * Validate non-negative number
     */
    public static void validateNonNegative(int value, String fieldName) throws ValidationException {
        if (value < 0) {
            throw new ValidationException(fieldName + " cannot be negative");
        }
    }
    
    /**
     * Validate email format
     */
    public static void validateEmail(String email) throws ValidationException {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Invalid email format");
        }
    }
    
    /**
     * Validate phone number format
     */
    public static void validatePhone(String phone) throws ValidationException {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException("Phone number must be 10 digits");
        }
    }
    
    /**
     * Validate string length
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) 
            throws ValidationException {
        if (value == null) {
            throw new ValidationException(fieldName + " cannot be null");
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new ValidationException(fieldName + " must be between " + 
                    minLength + " and " + maxLength + " characters");
        }
    }
    
    /**
     * Validate range
     */
    public static void validateRange(double value, String fieldName, double min, double max) 
            throws ValidationException {
        if (value < min || value > max) {
            throw new ValidationException(fieldName + " must be between " + min + " and " + max);
        }
    }
    
    /**
     * Validate password strength
     */
    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long");
        }
    }
}
