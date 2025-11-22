package com.inventory.dao.impl;

import com.inventory.dao.AlertDAO;
import com.inventory.model.Alert;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of AlertDAO
 */
public class AlertDAOImpl implements AlertDAO {
    private static final String DATA_FILE = "data/alerts.dat";
    private static AlertDAOImpl instance;
    private Map<String, Alert> alertMap;
    
    private AlertDAOImpl() {
        alertMap = new HashMap<>();
        loadFromFile();
    }
    
    public static synchronized AlertDAOImpl getInstance() {
        if (instance == null) {
            instance = new AlertDAOImpl();
        }
        return instance;
    }
    
    @Override
    public void save(Alert alert) throws InventoryException {
        if (alert == null || alert.getAlertId() == null) {
            throw new InventoryException("Alert or Alert ID cannot be null");
        }
        if (alertMap.containsKey(alert.getAlertId())) {
            throw new InventoryException("Alert with ID " + alert.getAlertId() + " already exists");
        }
        alertMap.put(alert.getAlertId(), alert);
        saveToFile();
    }
    
    @Override
    public void update(Alert alert) throws InventoryException {
        if (alert == null || alert.getAlertId() == null) {
            throw new InventoryException("Alert or Alert ID cannot be null");
        }
        if (!alertMap.containsKey(alert.getAlertId())) {
            throw new InventoryException("Alert with ID " + alert.getAlertId() + " not found");
        }
        alertMap.put(alert.getAlertId(), alert);
        saveToFile();
    }
    
    @Override
    public void delete(String alertId) throws InventoryException {
        if (alertId == null) {
            throw new InventoryException("Alert ID cannot be null");
        }
        if (!alertMap.containsKey(alertId)) {
            throw new InventoryException("Alert with ID " + alertId + " not found");
        }
        alertMap.remove(alertId);
        saveToFile();
    }
    
    @Override
    public Alert findById(String alertId) throws InventoryException {
        if (alertId == null) {
            throw new InventoryException("Alert ID cannot be null");
        }
        return alertMap.get(alertId);
    }
    
    @Override
    public List<Alert> findAll() throws InventoryException {
        return new ArrayList<>(alertMap.values());
    }
    
    @Override
    public List<Alert> findUnresolved() throws InventoryException {
        return alertMap.values().stream()
                .filter(a -> !a.isResolved())
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Alert> findByProductId(String productId) throws InventoryException {
        if (productId == null) {
            throw new InventoryException("Product ID cannot be null");
        }
        return alertMap.values().stream()
                .filter(a -> productId.equals(a.getProductId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Alert> findByPriority(Alert.AlertPriority priority) throws InventoryException {
        if (priority == null) {
            throw new InventoryException("Priority cannot be null");
        }
        return alertMap.values().stream()
                .filter(a -> priority.equals(a.getPriority()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean exists(String alertId) throws InventoryException {
        if (alertId == null) {
            throw new InventoryException("Alert ID cannot be null");
        }
        return alertMap.containsKey(alertId);
    }
    
    private void loadFromFile() {
        try {
            Object data = FileHandler.readFromFile(DATA_FILE);
            if (data instanceof Map) {
                alertMap = (Map<String, Alert>) data;
            }
        } catch (Exception e) {
            alertMap = new HashMap<>();
        }
    }
    
    private void saveToFile() throws InventoryException {
        try {
            FileHandler.writeToFile(DATA_FILE, alertMap);
        } catch (Exception e) {
            throw new InventoryException("Error saving alerts to file", e);
        }
    }
}
