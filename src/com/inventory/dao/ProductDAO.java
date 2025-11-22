package com.inventory.dao;

import com.inventory.model.Product;
import com.inventory.exception.InventoryException;
import java.util.List;

/**
 * Data Access Object interface for Product operations
 */
public interface ProductDAO {
    void save(Product product) throws InventoryException;
    void update(Product product) throws InventoryException;
    void delete(String productId) throws InventoryException;
    Product findById(String productId) throws InventoryException;
    List<Product> findAll() throws InventoryException;
    List<Product> findByCategory(String category) throws InventoryException;
    List<Product> findByName(String name) throws InventoryException;
    List<Product> findLowStockProducts() throws InventoryException;
    boolean exists(String productId) throws InventoryException;
}
