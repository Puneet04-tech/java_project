package com.inventory.dao;

import com.inventory.model.User;
import com.inventory.exception.InventoryException;
import java.util.List;

/**
 * Data Access Object interface for User operations
 */
public interface UserDAO {
    void save(User user) throws InventoryException;
    void update(User user) throws InventoryException;
    void delete(String userId) throws InventoryException;
    User findById(String userId) throws InventoryException;
    User findByUsername(String username) throws InventoryException;
    List<User> findAll() throws InventoryException;
    boolean exists(String userId) throws InventoryException;
    boolean usernameExists(String username) throws InventoryException;
}
