package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.model.Alert;
import com.inventory.dao.ProductDAO;
import com.inventory.dao.AlertDAO;
import com.inventory.dao.impl.ProductDAOImpl;
import com.inventory.dao.impl.AlertDAOImpl;
import com.inventory.exception.InventoryException;
import com.inventory.exception.ValidationException;
import com.inventory.util.Validator;
import com.inventory.util.Logger;
import com.inventory.util.IDGenerator;
import java.util.List;

/**
 * Service class for inventory/product management business logic
 */
public class InventoryService {
    private static InventoryService instance;
    private ProductDAO productDAO;
    private AlertDAO alertDAO;
    
    private InventoryService() {
        this.productDAO = ProductDAOImpl.getInstance();
        this.alertDAO = AlertDAOImpl.getInstance();
    }
    
    public static synchronized InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }
    
    /**
     * Add a new product to inventory
     */
    public Product addProduct(String name, String category, double price, int quantity,
                             int minStockLevel, String supplierId, String description) 
            throws InventoryException, ValidationException {
        // Validate inputs
        Validator.validateNotEmpty(name, "Product name");
        Validator.validateNotEmpty(category, "Category");
        Validator.validatePositive(price, "Price");
        Validator.validateNonNegative(quantity, "Quantity");
        Validator.validateNonNegative(minStockLevel, "Minimum stock level");
        
        // Create product
        String productId = IDGenerator.generateProductId();
        Product product = new Product(productId, name, category, price, quantity, 
                                     minStockLevel, supplierId, description);
        
        // Save product
        productDAO.save(product);
        Logger.info("Product added: " + product.getName() + " [ID: " + productId + "]");
        
        // Check and create alert if necessary
        checkAndCreateStockAlert(product);
        
        return product;
    }
    
    /**
     * Update an existing product
     */
    public void updateProduct(Product product) throws InventoryException, ValidationException {
        Validator.validateNotEmpty(product.getName(), "Product name");
        Validator.validatePositive(product.getPrice(), "Price");
        Validator.validateNonNegative(product.getQuantity(), "Quantity");
        
        productDAO.update(product);
        Logger.info("Product updated: " + product.getProductId());
        
        // Check stock level after update
        checkAndCreateStockAlert(product);
    }
    
    /**
     * Delete a product
     */
    public void deleteProduct(String productId) throws InventoryException {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new InventoryException("Product not found: " + productId);
        }
        
        productDAO.delete(productId);
        Logger.info("Product deleted: " + productId);
    }
    
    /**
     * Get product by ID
     */
    public Product getProductById(String productId) throws InventoryException {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new InventoryException("Product not found: " + productId);
        }
        return product;
    }
    
    /**
     * Get all products
     */
    public List<Product> getAllProducts() throws InventoryException {
        return productDAO.findAll();
    }
    
    /**
     * Search products by name
     */
    public List<Product> searchProductsByName(String name) throws InventoryException {
        return productDAO.findByName(name);
    }
    
    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(String category) throws InventoryException {
        return productDAO.findByCategory(category);
    }
    
    /**
     * Get low stock products
     */
    public List<Product> getLowStockProducts() throws InventoryException {
        return productDAO.findLowStockProducts();
    }
    
    /**
     * Add stock to product
     */
    public void addStock(String productId, int quantity) throws InventoryException {
        if (quantity <= 0) {
            throw new InventoryException("Quantity must be positive");
        }
        
        Product product = getProductById(productId);
        product.addStock(quantity);
        productDAO.update(product);
        
        Logger.info(String.format("Added %d units to product %s. New quantity: %d", 
                quantity, productId, product.getQuantity()));
        
        // Resolve low stock alerts if applicable
        if (!product.isLowStock()) {
            resolveStockAlerts(productId);
        }
    }
    
    /**
     * Reduce stock from product
     */
    public void reduceStock(String productId, int quantity) throws InventoryException {
        if (quantity <= 0) {
            throw new InventoryException("Quantity must be positive");
        }
        
        Product product = getProductById(productId);
        
        if (product.getQuantity() < quantity) {
            throw new InventoryException("Insufficient stock. Available: " + product.getQuantity());
        }
        
        product.reduceStock(quantity);
        productDAO.update(product);
        
        Logger.info(String.format("Reduced %d units from product %s. New quantity: %d", 
                quantity, productId, product.getQuantity()));
        
        // Check for low stock
        checkAndCreateStockAlert(product);
    }
    
    /**
     * Check stock level and create alert if needed
     */
    private void checkAndCreateStockAlert(Product product) {
        try {
            if (product.isOutOfStock()) {
                createStockAlert(product, Alert.AlertType.OUT_OF_STOCK, 
                               Alert.AlertPriority.CRITICAL,
                               "Product is out of stock: " + product.getName());
            } else if (product.isLowStock()) {
                createStockAlert(product, Alert.AlertType.LOW_STOCK,
                               Alert.AlertPriority.HIGH,
                               "Low stock alert for: " + product.getName() + 
                               " (Current: " + product.getQuantity() + ", Min: " + 
                               product.getMinStockLevel() + ")");
            }
        } catch (Exception e) {
            Logger.error("Failed to create stock alert", e);
        }
    }
    
    /**
     * Create stock alert
     */
    private void createStockAlert(Product product, Alert.AlertType type, 
                                 Alert.AlertPriority priority, String message) 
            throws InventoryException {
        String alertId = IDGenerator.generateAlertId();
        Alert alert = new Alert(alertId, type, priority, product.getProductId(), message);
        alertDAO.save(alert);
        Logger.warning("Alert created: " + message);
    }
    
    /**
     * Resolve stock alerts for a product
     */
    private void resolveStockAlerts(String productId) throws InventoryException {
        List<Alert> alerts = alertDAO.findByProductId(productId);
        for (Alert alert : alerts) {
            if (!alert.isResolved() && 
                (alert.getType() == Alert.AlertType.LOW_STOCK || 
                 alert.getType() == Alert.AlertType.OUT_OF_STOCK)) {
                alert.resolve("SYSTEM");
                alertDAO.update(alert);
            }
        }
    }
    
    /**
     * Get total inventory value
     */
    public double getTotalInventoryValue() throws InventoryException {
        List<Product> products = productDAO.findAll();
        return products.stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
    }
    
    /**
     * Get product count
     */
    public int getProductCount() throws InventoryException {
        return productDAO.findAll().size();
    }
}
