package com.inventory.dao;

import com.inventory.model.Transaction;
import com.inventory.exception.InventoryException;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Data Access Object interface for Transaction operations
 */
public interface TransactionDAO {
    void save(Transaction transaction) throws InventoryException;
    Transaction findById(String transactionId) throws InventoryException;
    List<Transaction> findAll() throws InventoryException;
    List<Transaction> findByProductId(String productId) throws InventoryException;
    List<Transaction> findByDateRange(LocalDateTime start, LocalDateTime end) throws InventoryException;
    List<Transaction> findByType(Transaction.TransactionType type) throws InventoryException;
    boolean exists(String transactionId) throws InventoryException;
}
