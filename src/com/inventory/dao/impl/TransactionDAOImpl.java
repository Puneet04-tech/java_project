package com.inventory.dao.impl;

import com.inventory.dao.TransactionDAO;
import com.inventory.model.Transaction;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

/**
 * File-based implementation of TransactionDAO
 */
public class TransactionDAOImpl implements TransactionDAO {
    private static final String DATA_FILE = "data/transactions.dat";
    private static TransactionDAOImpl instance;
    private Map<String, Transaction> transactionMap;
    
    private TransactionDAOImpl() {
        transactionMap = new HashMap<>();
        loadFromFile();
    }
    
    public static synchronized TransactionDAOImpl getInstance() {
        if (instance == null) {
            instance = new TransactionDAOImpl();
        }
        return instance;
    }
    
    @Override
    public void save(Transaction transaction) throws InventoryException {
        if (transaction == null || transaction.getTransactionId() == null) {
            throw new InventoryException("Transaction or Transaction ID cannot be null");
        }
        if (transactionMap.containsKey(transaction.getTransactionId())) {
            throw new InventoryException("Transaction with ID " + transaction.getTransactionId() + " already exists");
        }
        transactionMap.put(transaction.getTransactionId(), transaction);
        saveToFile();
    }
    
    @Override
    public Transaction findById(String transactionId) throws InventoryException {
        if (transactionId == null) {
            throw new InventoryException("Transaction ID cannot be null");
        }
        return transactionMap.get(transactionId);
    }
    
    @Override
    public List<Transaction> findAll() throws InventoryException {
        return new ArrayList<>(transactionMap.values());
    }
    
    @Override
    public List<Transaction> findByProductId(String productId) throws InventoryException {
        if (productId == null) {
            throw new InventoryException("Product ID cannot be null");
        }
        return transactionMap.values().stream()
                .filter(t -> productId.equals(t.getProductId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Transaction> findByDateRange(LocalDateTime start, LocalDateTime end) throws InventoryException {
        if (start == null || end == null) {
            throw new InventoryException("Date range cannot be null");
        }
        return transactionMap.values().stream()
                .filter(t -> !t.getTransactionDate().isBefore(start) && !t.getTransactionDate().isAfter(end))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Transaction> findByType(Transaction.TransactionType type) throws InventoryException {
        if (type == null) {
            throw new InventoryException("Transaction type cannot be null");
        }
        return transactionMap.values().stream()
                .filter(t -> type.equals(t.getType()))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean exists(String transactionId) throws InventoryException {
        if (transactionId == null) {
            throw new InventoryException("Transaction ID cannot be null");
        }
        return transactionMap.containsKey(transactionId);
    }
    
    private void loadFromFile() {
        try {
            Object data = FileHandler.readFromFile(DATA_FILE);
            if (data instanceof Map) {
                transactionMap = (Map<String, Transaction>) data;
            }
        } catch (Exception e) {
            transactionMap = new HashMap<>();
        }
    }
    
    private void saveToFile() throws InventoryException {
        try {
            FileHandler.writeToFile(DATA_FILE, transactionMap);
        } catch (Exception e) {
            throw new InventoryException("Error saving transactions to file", e);
        }
    }
}
