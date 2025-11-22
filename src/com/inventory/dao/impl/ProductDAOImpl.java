package com.inventory.dao.impl;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of ProductDAO using serialization
 */
public class ProductDAOImpl implements ProductDAO {
    private static final String DATA_FILE = "data/products.dat";
    private static ProductDAOImpl instance;
    private Map<String, Product> productMap;
    
    private ProductDAOImpl() {
        productMap = new HashMap<>();
        loadFromFile();
    }
    
    // Singleton pattern
    public static synchronized ProductDAOImpl getInstance() {
        if (instance == null) {
            instance = new ProductDAOImpl();
        }
        return instance;
    }
    
    @Override
    public void save(Product product) throws InventoryException {
        if (product == null || product.getProductId() == null) {
            throw new InventoryException("Product or Product ID cannot be null");
        }
        if (productMap.containsKey(product.getProductId())) {
            throw new InventoryException("Product with ID " + product.getProductId() + " already exists");
        }
        productMap.put(product.getProductId(), product);
        saveToFile();
    }
    
    @Override
    public void update(Product product) throws InventoryException {
        if (product == null || product.getProductId() == null) {
            throw new InventoryException("Product or Product ID cannot be null");
        }
        if (!productMap.containsKey(product.getProductId())) {
            throw new InventoryException("Product with ID " + product.getProductId() + " not found");
        }
        productMap.put(product.getProductId(), product);
        saveToFile();
    }
    
    @Override
    public void delete(String productId) throws InventoryException {
        if (productId == null) {
            throw new InventoryException("Product ID cannot be null");
        }
        if (!productMap.containsKey(productId)) {
            throw new InventoryException("Product with ID " + productId + " not found");
        }
        productMap.remove(productId);
        saveToFile();
    }
    
    @Override
    public Product findById(String productId) throws InventoryException {
        if (productId == null) {
            throw new InventoryException("Product ID cannot be null");
        }
        return productMap.get(productId);
    }
    
    @Override
    public List<Product> findAll() throws InventoryException {
        return new ArrayList<>(productMap.values());
    }
    
    @Override
    public List<Product> findByCategory(String category) throws InventoryException {
        if (category == null) {
            throw new InventoryException("Category cannot be null");
        }
        return productMap.values().stream()
                .filter(p -> category.equalsIgnoreCase(p.getCategory()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByName(String name) throws InventoryException {
        if (name == null) {
            throw new InventoryException("Name cannot be null");
        }
        return productMap.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findLowStockProducts() throws InventoryException {
        return productMap.values().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean exists(String productId) throws InventoryException {
        if (productId == null) {
            throw new InventoryException("Product ID cannot be null");
        }
        return productMap.containsKey(productId);
    }
    
    private void loadFromFile() {
        try {
            Object data = FileHandler.readFromFile(DATA_FILE);
            if (data instanceof Map) {
                productMap = (Map<String, Product>) data;
            }
        } catch (Exception e) {
            productMap = new HashMap<>();
        }
    }
    
    private void saveToFile() throws InventoryException {
        try {
            FileHandler.writeToFile(DATA_FILE, productMap);
        } catch (Exception e) {
            throw new InventoryException("Error saving products to file", e);
        }
    }
}
