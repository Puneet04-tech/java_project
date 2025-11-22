package com.inventory.service;

import com.inventory.model.Transaction;
import com.inventory.model.Product;
import com.inventory.dao.TransactionDAO;
import com.inventory.dao.impl.TransactionDAOImpl;
import com.inventory.exception.InventoryException;
import com.inventory.util.Logger;
import com.inventory.util.IDGenerator;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Service class for transaction management business logic
 */
public class TransactionService {
    private static TransactionService instance;
    private TransactionDAO transactionDAO;
    private InventoryService inventoryService;
    private SupplierService supplierService;
    
    private TransactionService() {
        this.transactionDAO = TransactionDAOImpl.getInstance();
        this.inventoryService = InventoryService.getInstance();
        this.supplierService = SupplierService.getInstance();
    }
    
    public static synchronized TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }
    
    /**
     * Record a sale transaction
     */
    public Transaction recordSale(String productId, int quantity, String performedBy, String remarks)
            throws InventoryException {
        // Validate product exists and has sufficient stock
        Product product = inventoryService.getProductById(productId);
        
        if (product.getQuantity() < quantity) {
            throw new InventoryException("Insufficient stock for sale. Available: " + 
                    product.getQuantity() + ", Requested: " + quantity);
        }
        
        // Create transaction
        String transactionId = IDGenerator.generateTransactionId();
        Transaction transaction = new Transaction(transactionId, productId, 
                Transaction.TransactionType.SALE, quantity, product.getPrice(), 
                performedBy, remarks);
        
        // Update inventory
        inventoryService.reduceStock(productId, quantity);
        
        // Save transaction
        transactionDAO.save(transaction);
        
        Logger.info(String.format("Sale recorded: %s - Product: %s, Qty: %d, Amount: %.2f", 
                transactionId, productId, quantity, transaction.getTotalAmount()));
        
        return transaction;
    }
    
    /**
     * Record a purchase transaction
     */
    public Transaction recordPurchase(String productId, int quantity, double pricePerUnit,
                                     String supplierId, String performedBy, String remarks)
            throws InventoryException {
        // Validate product exists
        Product product = inventoryService.getProductById(productId);
        
        // Validate supplier if provided
        if (supplierId != null && !supplierId.isEmpty()) {
            supplierService.getSupplierById(supplierId);
        }
        
        // Create transaction
        String transactionId = IDGenerator.generateTransactionId();
        Transaction transaction = new Transaction(transactionId, productId,
                Transaction.TransactionType.PURCHASE, quantity, pricePerUnit,
                performedBy, remarks);
        transaction.setSupplierId(supplierId);
        
        // Update inventory
        inventoryService.addStock(productId, quantity);
        
        // Update supplier order count
        if (supplierId != null && !supplierId.isEmpty()) {
            supplierService.incrementSupplierOrderCount(supplierId);
        }
        
        // Save transaction
        transactionDAO.save(transaction);
        
        Logger.info(String.format("Purchase recorded: %s - Product: %s, Qty: %d, Amount: %.2f",
                transactionId, productId, quantity, transaction.getTotalAmount()));
        
        return transaction;
    }
    
    /**
     * Record an adjustment transaction
     */
    public Transaction recordAdjustment(String productId, int quantity, String performedBy, 
                                       String remarks) throws InventoryException {
        Product product = inventoryService.getProductById(productId);
        
        String transactionId = IDGenerator.generateTransactionId();
        Transaction transaction = new Transaction(transactionId, productId,
                Transaction.TransactionType.ADJUSTMENT, Math.abs(quantity), 0.0,
                performedBy, remarks);
        
        // Adjust inventory based on positive or negative quantity
        if (quantity > 0) {
            inventoryService.addStock(productId, quantity);
        } else if (quantity < 0) {
            inventoryService.reduceStock(productId, Math.abs(quantity));
        }
        
        transactionDAO.save(transaction);
        Logger.info("Adjustment recorded: " + transactionId);
        
        return transaction;
    }
    
    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(String transactionId) throws InventoryException {
        Transaction transaction = transactionDAO.findById(transactionId);
        if (transaction == null) {
            throw new InventoryException("Transaction not found: " + transactionId);
        }
        return transaction;
    }
    
    /**
     * Get all transactions
     */
    public List<Transaction> getAllTransactions() throws InventoryException {
        return transactionDAO.findAll();
    }
    
    /**
     * Get transactions by product
     */
    public List<Transaction> getTransactionsByProduct(String productId) throws InventoryException {
        return transactionDAO.findByProductId(productId);
    }
    
    /**
     * Get transactions by date range
     */
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end)
            throws InventoryException {
        return transactionDAO.findByDateRange(start, end);
    }
    
    /**
     * Get transactions by type
     */
    public List<Transaction> getTransactionsByType(Transaction.TransactionType type)
            throws InventoryException {
        return transactionDAO.findByType(type);
    }
    
    /**
     * Calculate total sales amount
     */
    public double calculateTotalSales() throws InventoryException {
        List<Transaction> sales = getTransactionsByType(Transaction.TransactionType.SALE);
        return sales.stream()
                .mapToDouble(Transaction::getTotalAmount)
                .sum();
    }
    
    /**
     * Calculate total sales for a date range
     */
    public double calculateTotalSales(LocalDateTime start, LocalDateTime end) 
            throws InventoryException {
        List<Transaction> transactions = getTransactionsByDateRange(start, end);
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.SALE)
                .mapToDouble(Transaction::getTotalAmount)
                .sum();
    }
    
    /**
     * Get transaction count
     */
    public int getTransactionCount() throws InventoryException {
        return transactionDAO.findAll().size();
    }
}
