package com.inventory.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Product entity class representing items in inventory
 * Implements Serializable for file-based persistence
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String productId;
    private String name;
    private String category;
    private double price;
    private int quantity;
    private int minStockLevel;
    private String supplierId;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String description;
    
    // Constructor
    public Product(String productId, String name, String category, double price, 
                   int quantity, int minStockLevel, String supplierId, String description) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.minStockLevel = minStockLevel;
        this.supplierId = supplierId;
        this.description = description;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    // Default constructor
    public Product() {
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public int getMinStockLevel() {
        return minStockLevel;
    }
    
    public void setMinStockLevel(int minStockLevel) {
        this.minStockLevel = minStockLevel;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    // Business methods
    public boolean isLowStock() {
        return quantity <= minStockLevel;
    }
    
    public boolean isOutOfStock() {
        return quantity == 0;
    }
    
    public void addStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.quantity += amount;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public void reduceStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (amount > this.quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        this.quantity -= amount;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public double getTotalValue() {
        return price * quantity;
    }
    
    @Override
    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Category=%s, Price=%.2f, Qty=%d, MinStock=%d]",
                productId, name, category, price, quantity, minStockLevel);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product = (Product) obj;
        return productId != null && productId.equals(product.productId);
    }
    
    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
}
