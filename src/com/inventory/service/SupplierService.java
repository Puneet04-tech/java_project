package com.inventory.service;

import com.inventory.model.Supplier;
import com.inventory.dao.SupplierDAO;
import com.inventory.dao.impl.SupplierDAOImpl;
import com.inventory.exception.InventoryException;
import com.inventory.exception.ValidationException;
import com.inventory.util.Validator;
import com.inventory.util.Logger;
import com.inventory.util.IDGenerator;
import java.util.List;

/**
 * Service class for supplier management business logic
 */
public class SupplierService {
    private static SupplierService instance;
    private SupplierDAO supplierDAO;
    
    private SupplierService() {
        this.supplierDAO = SupplierDAOImpl.getInstance();
    }
    
    public static synchronized SupplierService getInstance() {
        if (instance == null) {
            instance = new SupplierService();
        }
        return instance;
    }
    
    /**
     * Add a new supplier
     */
    public Supplier addSupplier(String name, String contactPerson, String phone, 
                               String email, String address) 
            throws InventoryException, ValidationException {
        // Validate inputs
        Validator.validateNotEmpty(name, "Supplier name");
        Validator.validateNotEmpty(contactPerson, "Contact person");
        Validator.validatePhone(phone);
        Validator.validateEmail(email);
        Validator.validateNotEmpty(address, "Address");
        
        // Create supplier
        String supplierId = IDGenerator.generateSupplierId();
        Supplier supplier = new Supplier(supplierId, name, contactPerson, phone, email, address);
        
        // Save supplier
        supplierDAO.save(supplier);
        Logger.info("Supplier added: " + name + " [ID: " + supplierId + "]");
        
        return supplier;
    }
    
    /**
     * Update an existing supplier
     */
    public void updateSupplier(Supplier supplier) throws InventoryException, ValidationException {
        Validator.validateNotEmpty(supplier.getName(), "Supplier name");
        Validator.validateNotEmpty(supplier.getContactPerson(), "Contact person");
        Validator.validatePhone(supplier.getPhone());
        Validator.validateEmail(supplier.getEmail());
        
        supplierDAO.update(supplier);
        Logger.info("Supplier updated: " + supplier.getSupplierId());
    }
    
    /**
     * Delete a supplier
     */
    public void deleteSupplier(String supplierId) throws InventoryException {
        Supplier supplier = supplierDAO.findById(supplierId);
        if (supplier == null) {
            throw new InventoryException("Supplier not found: " + supplierId);
        }
        
        supplierDAO.delete(supplierId);
        Logger.info("Supplier deleted: " + supplierId);
    }
    
    /**
     * Get supplier by ID
     */
    public Supplier getSupplierById(String supplierId) throws InventoryException {
        Supplier supplier = supplierDAO.findById(supplierId);
        if (supplier == null) {
            throw new InventoryException("Supplier not found: " + supplierId);
        }
        return supplier;
    }
    
    /**
     * Get all suppliers
     */
    public List<Supplier> getAllSuppliers() throws InventoryException {
        return supplierDAO.findAll();
    }
    
    /**
     * Search suppliers by name
     */
    public List<Supplier> searchSuppliersByName(String name) throws InventoryException {
        return supplierDAO.findByName(name);
    }
    
    /**
     * Update supplier rating
     */
    public void updateSupplierRating(String supplierId, double rating) 
            throws InventoryException, ValidationException {
        Validator.validateRange(rating, "Rating", 0.0, 5.0);
        
        Supplier supplier = getSupplierById(supplierId);
        supplier.updateRating(rating);
        supplierDAO.update(supplier);
        
        Logger.info(String.format("Updated rating for supplier %s to %.2f", supplierId, rating));
    }
    
    /**
     * Increment supplier order count
     */
    public void incrementSupplierOrderCount(String supplierId) throws InventoryException {
        Supplier supplier = getSupplierById(supplierId);
        supplier.incrementOrderCount();
        supplierDAO.update(supplier);
        
        Logger.info("Incremented order count for supplier: " + supplierId);
    }
    
    /**
     * Deactivate supplier
     */
    public void deactivateSupplier(String supplierId) throws InventoryException {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setActive(false);
        supplierDAO.update(supplier);
        
        Logger.info("Supplier deactivated: " + supplierId);
    }
    
    /**
     * Activate supplier
     */
    public void activateSupplier(String supplierId) throws InventoryException {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setActive(true);
        supplierDAO.update(supplier);
        
        Logger.info("Supplier activated: " + supplierId);
    }
    
    /**
     * Check if supplier exists
     */
    public boolean supplierExists(String supplierId) throws InventoryException {
        return supplierDAO.exists(supplierId);
    }
}
