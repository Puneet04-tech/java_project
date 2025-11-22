package com.inventory.service;

import com.inventory.model.Alert;
import com.inventory.dao.AlertDAO;
import com.inventory.dao.impl.AlertDAOImpl;
import com.inventory.exception.InventoryException;
import com.inventory.util.Logger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for alert management
 */
public class AlertService {
    private static AlertService instance;
    private AlertDAO alertDAO;
    
    private AlertService() {
        this.alertDAO = AlertDAOImpl.getInstance();
    }
    
    public static synchronized AlertService getInstance() {
        if (instance == null) {
            instance = new AlertService();
        }
        return instance;
    }
    
    /**
     * Get all alerts
     */
    public List<Alert> getAllAlerts() throws InventoryException {
        return alertDAO.findAll();
    }
    
    /**
     * Get unresolved alerts
     */
    public List<Alert> getUnresolvedAlerts() throws InventoryException {
        return alertDAO.findUnresolved();
    }
    
    /**
     * Get alerts by priority
     */
    public List<Alert> getAlertsByPriority(Alert.AlertPriority priority) throws InventoryException {
        return alertDAO.findByPriority(priority);
    }
    
    /**
     * Get critical unresolved alerts
     */
    public List<Alert> getCriticalAlerts() throws InventoryException {
        return alertDAO.findUnresolved().stream()
                .filter(Alert::isUrgent)
                .collect(Collectors.toList());
    }
    
    /**
     * Resolve an alert
     */
    public void resolveAlert(String alertId, String userId) throws InventoryException {
        Alert alert = alertDAO.findById(alertId);
        if (alert == null) {
            throw new InventoryException("Alert not found: " + alertId);
        }
        
        if (alert.isResolved()) {
            throw new InventoryException("Alert is already resolved");
        }
        
        alert.resolve(userId);
        alertDAO.update(alert);
        
        Logger.info("Alert resolved: " + alertId + " by user: " + userId);
    }
    
    /**
     * Get alert count
     */
    public int getUnresolvedAlertCount() throws InventoryException {
        return alertDAO.findUnresolved().size();
    }
    
    /**
     * Delete alert
     */
    public void deleteAlert(String alertId) throws InventoryException {
        alertDAO.delete(alertId);
        Logger.info("Alert deleted: " + alertId);
    }
    
    /**
     * Get alerts by product
     */
    public List<Alert> getAlertsByProduct(String productId) throws InventoryException {
        return alertDAO.findByProductId(productId);
    }
}
