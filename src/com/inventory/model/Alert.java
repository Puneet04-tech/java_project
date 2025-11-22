package com.inventory.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Alert entity class for system notifications
 */
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String alertId;
    private AlertType type;
    private AlertPriority priority;
    private String productId;
    private String message;
    private LocalDateTime createdDate;
    private boolean resolved;
    private LocalDateTime resolvedDate;
    private String resolvedBy;
    
    // Enum for alert types
    public enum AlertType {
        LOW_STOCK, OUT_OF_STOCK, CRITICAL_STOCK, PRICE_CHANGE, SUPPLIER_ISSUE
    }
    
    // Enum for alert priority
    public enum AlertPriority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    // Constructor
    public Alert(String alertId, AlertType type, AlertPriority priority, 
                String productId, String message) {
        this.alertId = alertId;
        this.type = type;
        this.priority = priority;
        this.productId = productId;
        this.message = message;
        this.createdDate = LocalDateTime.now();
        this.resolved = false;
    }
    
    // Default constructor
    public Alert() {
        this.createdDate = LocalDateTime.now();
        this.resolved = false;
    }
    
    // Getters and Setters
    public String getAlertId() {
        return alertId;
    }
    
    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }
    
    public AlertType getType() {
        return type;
    }
    
    public void setType(AlertType type) {
        this.type = type;
    }
    
    public AlertPriority getPriority() {
        return priority;
    }
    
    public void setPriority(AlertPriority priority) {
        this.priority = priority;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public boolean isResolved() {
        return resolved;
    }
    
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
    
    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public String getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
    
    // Business methods
    public void resolve(String userId) {
        this.resolved = true;
        this.resolvedDate = LocalDateTime.now();
        this.resolvedBy = userId;
    }
    
    public long getAgeInHours() {
        return java.time.Duration.between(createdDate, LocalDateTime.now()).toHours();
    }
    
    public boolean isUrgent() {
        return priority == AlertPriority.CRITICAL || priority == AlertPriority.HIGH;
    }
    
    @Override
    public String toString() {
        return String.format("Alert[ID=%s, Type=%s, Priority=%s, Product=%s, Resolved=%s]",
                alertId, type, priority, productId, resolved);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alert alert = (Alert) obj;
        return alertId != null && alertId.equals(alert.alertId);
    }
    
    @Override
    public int hashCode() {
        return alertId != null ? alertId.hashCode() : 0;
    }
}
