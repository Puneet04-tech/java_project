package com.inventory.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * User entity class for authentication and authorization
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdDate;
    private LocalDateTime lastLoginDate;
    
    // Enum for user roles
    public enum UserRole {
        ADMIN, MANAGER, CASHIER
    }
    
    // Constructor
    public User(String userId, String username, String password, String fullName, 
                String email, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Default constructor
    public User() {
        this.active = true;
        this.createdDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    // Business methods
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public boolean verifyPassword(String password) {
        return this.passwordHash.equals(hashPassword(password));
    }
    
    public void changePassword(String oldPassword, String newPassword) {
        if (!verifyPassword(oldPassword)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        this.passwordHash = hashPassword(newPassword);
    }
    
    public void updateLastLogin() {
        this.lastLoginDate = LocalDateTime.now();
    }
    
    public boolean hasPermission(String permission) {
        switch (role) {
            case ADMIN:
                return true; // Admin has all permissions
            case MANAGER:
                return !permission.equals("USER_MANAGEMENT");
            case CASHIER:
                return permission.equals("VIEW_PRODUCTS") || 
                       permission.equals("RECORD_SALE");
            default:
                return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("User[ID=%s, Username=%s, Name=%s, Role=%s, Active=%s]",
                userId, username, fullName, role, active);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId != null && userId.equals(user.userId);
    }
    
    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }
}
