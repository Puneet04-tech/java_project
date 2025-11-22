package com.inventory.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Transaction entity class for tracking inventory movements
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String productId;
    private TransactionType type;
    private int quantity;
    private double pricePerUnit;
    private double totalAmount;
    private String performedBy; // User ID
    private LocalDateTime transactionDate;
    private String remarks;
    private String supplierId; // For purchase transactions
    
    // Enum for transaction types
    public enum TransactionType {
        SALE, PURCHASE, ADJUSTMENT, RETURN
    }
    
    // Constructor
    public Transaction(String transactionId, String productId, TransactionType type,
                      int quantity, double pricePerUnit, String performedBy, String remarks) {
        this.transactionId = transactionId;
        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.totalAmount = quantity * pricePerUnit;
        this.performedBy = performedBy;
        this.transactionDate = LocalDateTime.now();
        this.remarks = remarks;
    }
    
    // Default constructor
    public Transaction() {
        this.transactionDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalAmount = quantity * pricePerUnit;
    }
    
    public double getPricePerUnit() {
        return pricePerUnit;
    }
    
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        this.totalAmount = quantity * pricePerUnit;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    
    // Business methods
    public void calculateTotalAmount() {
        this.totalAmount = this.quantity * this.pricePerUnit;
    }
    
    public boolean isInbound() {
        return type == TransactionType.PURCHASE || type == TransactionType.RETURN;
    }
    
    public boolean isOutbound() {
        return type == TransactionType.SALE;
    }
    
    @Override
    public String toString() {
        return String.format("Transaction[ID=%s, Product=%s, Type=%s, Qty=%d, Amount=%.2f, Date=%s]",
                transactionId, productId, type, quantity, totalAmount, 
                transactionDate.toString().substring(0, 19));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction transaction = (Transaction) obj;
        return transactionId != null && transactionId.equals(transaction.transactionId);
    }
    
    @Override
    public int hashCode() {
        return transactionId != null ? transactionId.hashCode() : 0;
    }
}
