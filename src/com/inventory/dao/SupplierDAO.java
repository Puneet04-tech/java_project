package com.inventory.dao;

import com.inventory.model.Supplier;
import com.inventory.exception.InventoryException;
import java.util.List;

/**
 * Data Access Object interface for Supplier operations
 */
public interface SupplierDAO {
    void save(Supplier supplier) throws InventoryException;
    void update(Supplier supplier) throws InventoryException;
    void delete(String supplierId) throws InventoryException;
    Supplier findById(String supplierId) throws InventoryException;
    List<Supplier> findAll() throws InventoryException;
    List<Supplier> findByName(String name) throws InventoryException;
    boolean exists(String supplierId) throws InventoryException;
}
