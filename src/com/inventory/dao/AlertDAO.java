package com.inventory.dao;

import com.inventory.model.Alert;
import com.inventory.exception.InventoryException;
import java.util.List;

/**
 * Data Access Object interface for Alert operations
 */
public interface AlertDAO {
    void save(Alert alert) throws InventoryException;
    void update(Alert alert) throws InventoryException;
    void delete(String alertId) throws InventoryException;
    Alert findById(String alertId) throws InventoryException;
    List<Alert> findAll() throws InventoryException;
    List<Alert> findUnresolved() throws InventoryException;
    List<Alert> findByProductId(String productId) throws InventoryException;
    List<Alert> findByPriority(Alert.AlertPriority priority) throws InventoryException;
    boolean exists(String alertId) throws InventoryException;
}
