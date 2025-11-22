package com.inventory.dao.impl;

import com.inventory.dao.SupplierDAO;
import com.inventory.model.Supplier;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of SupplierDAO
 */
public class SupplierDAOImpl implements SupplierDAO {
    private static final String DATA_FILE = "data/suppliers.dat";
    private static SupplierDAOImpl instance;
    private Map<String, Supplier> supplierMap;
    
    private SupplierDAOImpl() {
        supplierMap = new HashMap<>();
        loadFromFile();
    }
    
    public static synchronized SupplierDAOImpl getInstance() {
        if (instance == null) {
            instance = new SupplierDAOImpl();
        }
        return instance;
    }
    
    @Override
    public void save(Supplier supplier) throws InventoryException {
        if (supplier == null || supplier.getSupplierId() == null) {
            throw new InventoryException("Supplier or Supplier ID cannot be null");
        }
        if (supplierMap.containsKey(supplier.getSupplierId())) {
            throw new InventoryException("Supplier with ID " + supplier.getSupplierId() + " already exists");
        }
        supplierMap.put(supplier.getSupplierId(), supplier);
        saveToFile();
    }
    
    @Override
    public void update(Supplier supplier) throws InventoryException {
        if (supplier == null || supplier.getSupplierId() == null) {
            throw new InventoryException("Supplier or Supplier ID cannot be null");
        }
        if (!supplierMap.containsKey(supplier.getSupplierId())) {
            throw new InventoryException("Supplier with ID " + supplier.getSupplierId() + " not found");
        }
        supplierMap.put(supplier.getSupplierId(), supplier);
        saveToFile();
    }
    
    @Override
    public void delete(String supplierId) throws InventoryException {
        if (supplierId == null) {
            throw new InventoryException("Supplier ID cannot be null");
        }
        if (!supplierMap.containsKey(supplierId)) {
            throw new InventoryException("Supplier with ID " + supplierId + " not found");
        }
        supplierMap.remove(supplierId);
        saveToFile();
    }
    
    @Override
    public Supplier findById(String supplierId) throws InventoryException {
        if (supplierId == null) {
            throw new InventoryException("Supplier ID cannot be null");
        }
        return supplierMap.get(supplierId);
    }
    
    @Override
    public List<Supplier> findAll() throws InventoryException {
        return new ArrayList<>(supplierMap.values());
    }
    
    @Override
    public List<Supplier> findByName(String name) throws InventoryException {
        if (name == null) {
            throw new InventoryException("Name cannot be null");
        }
        return supplierMap.values().stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean exists(String supplierId) throws InventoryException {
        if (supplierId == null) {
            throw new InventoryException("Supplier ID cannot be null");
        }
        return supplierMap.containsKey(supplierId);
    }
    
    private void loadFromFile() {
        try {
            Object data = FileHandler.readFromFile(DATA_FILE);
            if (data instanceof Map) {
                supplierMap = (Map<String, Supplier>) data;
            }
        } catch (Exception e) {
            supplierMap = new HashMap<>();
        }
    }
    
    private void saveToFile() throws InventoryException {
        try {
            FileHandler.writeToFile(DATA_FILE, supplierMap);
        } catch (Exception e) {
            throw new InventoryException("Error saving suppliers to file", e);
        }
    }
}
