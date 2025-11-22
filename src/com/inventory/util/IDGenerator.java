package com.inventory.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating unique IDs
 */
public class IDGenerator {
    private static AtomicInteger productCounter = new AtomicInteger(1);
    private static AtomicInteger supplierCounter = new AtomicInteger(1);
    private static AtomicInteger transactionCounter = new AtomicInteger(1);
    private static AtomicInteger userCounter = new AtomicInteger(2); // Start from 2 (admin is U001)
    private static AtomicInteger alertCounter = new AtomicInteger(1);
    
    /**
     * Generate product ID
     */
    public static synchronized String generateProductId() {
        return String.format("P%04d", productCounter.getAndIncrement());
    }
    
    /**
     * Generate supplier ID
     */
    public static synchronized String generateSupplierId() {
        return String.format("S%04d", supplierCounter.getAndIncrement());
    }
    
    /**
     * Generate transaction ID
     */
    public static synchronized String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return String.format("T%s-%04d", timestamp, transactionCounter.getAndIncrement());
    }
    
    /**
     * Generate user ID
     */
    public static synchronized String generateUserId() {
        return String.format("U%03d", userCounter.getAndIncrement());
    }
    
    /**
     * Generate alert ID
     */
    public static synchronized String generateAlertId() {
        return String.format("A%06d", alertCounter.getAndIncrement());
    }
    
    /**
     * Reset counters (for testing purposes)
     */
    public static synchronized void resetCounters() {
        productCounter.set(1);
        supplierCounter.set(1);
        transactionCounter.set(1);
        userCounter.set(2);
        alertCounter.set(1);
    }
}
