package com.inventory.service;

import com.inventory.model.User;
import com.inventory.dao.UserDAO;
import com.inventory.dao.impl.UserDAOImpl;
import com.inventory.exception.AuthenticationException;
import com.inventory.exception.InventoryException;
import com.inventory.exception.ValidationException;
import com.inventory.util.Validator;
import com.inventory.util.Logger;
import com.inventory.util.IDGenerator;
import java.util.List;

/**
 * Service class for user management and authentication
 */
public class UserService {
    private static UserService instance;
    private UserDAO userDAO;
    private User currentUser;
    
    private UserService() {
        this.userDAO = UserDAOImpl.getInstance();
    }
    
    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    /**
     * Authenticate user
     */
    public User authenticate(String username, String password) throws AuthenticationException {
        try {
            User user = userDAO.findByUsername(username);
            
            if (user == null) {
                Logger.warning("Failed login attempt for username: " + username);
                throw new AuthenticationException("Invalid username or password");
            }
            
            if (!user.isActive()) {
                Logger.warning("Login attempt for inactive user: " + username);
                throw new AuthenticationException("User account is inactive");
            }
            
            if (!user.verifyPassword(password)) {
                Logger.warning("Failed login attempt for username: " + username);
                throw new AuthenticationException("Invalid username or password");
            }
            
            // Update last login
            user.updateLastLogin();
            userDAO.update(user);
            
            currentUser = user;
            Logger.info("User logged in: " + username + " [Role: " + user.getRole() + "]");
            
            return user;
        } catch (InventoryException e) {
            throw new AuthenticationException("Authentication failed", e);
        }
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        if (currentUser != null) {
            Logger.info("User logged out: " + currentUser.getUsername());
            currentUser = null;
        }
    }
    
    /**
     * Get current logged-in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Add a new user
     */
    public User addUser(String username, String password, String fullName, String email,
                       User.UserRole role) throws InventoryException, ValidationException {
        // Validate inputs
        Validator.validateNotEmpty(username, "Username");
        Validator.validatePassword(password);
        Validator.validateNotEmpty(fullName, "Full name");
        Validator.validateEmail(email);
        
        // Check if username already exists
        if (userDAO.usernameExists(username)) {
            throw new InventoryException("Username already exists: " + username);
        }
        
        // Create user
        String userId = IDGenerator.generateUserId();
        User user = new User(userId, username, password, fullName, email, role);
        
        // Save user
        userDAO.save(user);
        Logger.info("User added: " + username + " [Role: " + role + "]");
        
        return user;
    }
    
    /**
     * Update user
     */
    public void updateUser(User user) throws InventoryException, ValidationException {
        Validator.validateNotEmpty(user.getUsername(), "Username");
        Validator.validateNotEmpty(user.getFullName(), "Full name");
        Validator.validateEmail(user.getEmail());
        
        userDAO.update(user);
        Logger.info("User updated: " + user.getUserId());
    }
    
    /**
     * Delete user
     */
    public void deleteUser(String userId) throws InventoryException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new InventoryException("User not found: " + userId);
        }
        
        userDAO.delete(userId);
        Logger.info("User deleted: " + userId);
    }
    
    /**
     * Get user by ID
     */
    public User getUserById(String userId) throws InventoryException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new InventoryException("User not found: " + userId);
        }
        return user;
    }
    
    /**
     * Get all users
     */
    public List<User> getAllUsers() throws InventoryException {
        return userDAO.findAll();
    }
    
    /**
     * Change user password
     */
    public void changePassword(String userId, String oldPassword, String newPassword)
            throws InventoryException, ValidationException {
        Validator.validatePassword(newPassword);
        
        User user = getUserById(userId);
        user.changePassword(oldPassword, newPassword);
        userDAO.update(user);
        
        Logger.info("Password changed for user: " + userId);
    }
    
    /**
     * Deactivate user
     */
    public void deactivateUser(String userId) throws InventoryException {
        User user = getUserById(userId);
        user.setActive(false);
        userDAO.update(user);
        Logger.info("User deactivated: " + userId);
    }
    
    /**
     * Activate user
     */
    public void activateUser(String userId) throws InventoryException {
        User user = getUserById(userId);
        user.setActive(true);
        userDAO.update(user);
        Logger.info("User activated: " + userId);
    }
    
    /**
     * Check if current user has permission
     */
    public boolean hasPermission(String permission) {
        if (currentUser == null) {
            return false;
        }
        return currentUser.hasPermission(permission);
    }
}
