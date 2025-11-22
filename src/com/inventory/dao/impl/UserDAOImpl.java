package com.inventory.dao.impl;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.*;

/**
 * File-based implementation of UserDAO
 */
public class UserDAOImpl implements UserDAO {
    private static final String DATA_FILE = "data/users.dat";
    private static UserDAOImpl instance;
    private Map<String, User> userMap;
    
    private UserDAOImpl() {
        userMap = new HashMap<>();
        loadFromFile();
        initializeDefaultUser();
    }
    
    public static synchronized UserDAOImpl getInstance() {
        if (instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }
    
    private void initializeDefaultUser() {
        try {
            if (userMap.isEmpty()) {
                User admin = new User("U001", "admin", "admin123", 
                        "System Administrator", "admin@inventory.com", User.UserRole.ADMIN);
                userMap.put(admin.getUserId(), admin);
                saveToFile();
            }
        } catch (Exception e) {
            System.err.println("Error initializing default user: " + e.getMessage());
        }
    }
    
    @Override
    public void save(User user) throws InventoryException {
        if (user == null || user.getUserId() == null) {
            throw new InventoryException("User or User ID cannot be null");
        }
        if (userMap.containsKey(user.getUserId())) {
            throw new InventoryException("User with ID " + user.getUserId() + " already exists");
        }
        if (usernameExists(user.getUsername())) {
            throw new InventoryException("Username " + user.getUsername() + " already exists");
        }
        userMap.put(user.getUserId(), user);
        saveToFile();
    }
    
    @Override
    public void update(User user) throws InventoryException {
        if (user == null || user.getUserId() == null) {
            throw new InventoryException("User or User ID cannot be null");
        }
        if (!userMap.containsKey(user.getUserId())) {
            throw new InventoryException("User with ID " + user.getUserId() + " not found");
        }
        userMap.put(user.getUserId(), user);
        saveToFile();
    }
    
    @Override
    public void delete(String userId) throws InventoryException {
        if (userId == null) {
            throw new InventoryException("User ID cannot be null");
        }
        if (!userMap.containsKey(userId)) {
            throw new InventoryException("User with ID " + userId + " not found");
        }
        userMap.remove(userId);
        saveToFile();
    }
    
    @Override
    public User findById(String userId) throws InventoryException {
        if (userId == null) {
            throw new InventoryException("User ID cannot be null");
        }
        return userMap.get(userId);
    }
    
    @Override
    public User findByUsername(String username) throws InventoryException {
        if (username == null) {
            throw new InventoryException("Username cannot be null");
        }
        return userMap.values().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<User> findAll() throws InventoryException {
        return new ArrayList<>(userMap.values());
    }
    
    @Override
    public boolean exists(String userId) throws InventoryException {
        if (userId == null) {
            throw new InventoryException("User ID cannot be null");
        }
        return userMap.containsKey(userId);
    }
    
    @Override
    public boolean usernameExists(String username) throws InventoryException {
        if (username == null) {
            throw new InventoryException("Username cannot be null");
        }
        return userMap.values().stream()
                .anyMatch(u -> username.equals(u.getUsername()));
    }
    
    private void loadFromFile() {
        try {
            Object data = FileHandler.readFromFile(DATA_FILE);
            if (data instanceof Map) {
                userMap = (Map<String, User>) data;
            }
        } catch (Exception e) {
            userMap = new HashMap<>();
        }
    }
    
    private void saveToFile() throws InventoryException {
        try {
            FileHandler.writeToFile(DATA_FILE, userMap);
        } catch (Exception e) {
            throw new InventoryException("Error saving users to file", e);
        }
    }
}
