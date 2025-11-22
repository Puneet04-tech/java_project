package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.model.Transaction;
import com.inventory.model.Supplier;
import com.inventory.model.Alert;
import com.inventory.exception.InventoryException;
import com.inventory.util.FileHandler;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;

/**
 * Service class for generating reports and analytics
 */
public class ReportService {
    private static ReportService instance;
    private InventoryService inventoryService;
    private TransactionService transactionService;
    private SupplierService supplierService;
    private AlertService alertService;
    
    private static final DateTimeFormatter DATE_FORMAT = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private ReportService() {
        this.inventoryService = InventoryService.getInstance();
        this.transactionService = TransactionService.getInstance();
        this.supplierService = SupplierService.getInstance();
        this.alertService = AlertService.getInstance();
    }
    
    public static synchronized ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }
    
    /**
     * Generate inventory status report
     */
    public String generateInventoryReport() throws InventoryException {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("INVENTORY STATUS REPORT\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Product> products = inventoryService.getAllProducts();
        
        report.append(String.format("Total Products: %d\n", products.size()));
        report.append(String.format("Total Inventory Value: $%.2f\n\n", 
                inventoryService.getTotalInventoryValue()));
        
        report.append(String.format("%-10s %-25s %-15s %-10s %-10s %-12s\n",
                "ID", "Name", "Category", "Price", "Qty", "Status"));
        report.append("-".repeat(80)).append("\n");
        
        for (Product p : products) {
            String status = p.isOutOfStock() ? "OUT" : (p.isLowStock() ? "LOW" : "OK");
            report.append(String.format("%-10s %-25s %-15s $%-9.2f %-10d %-12s\n",
                    p.getProductId(),
                    truncate(p.getName(), 25),
                    truncate(p.getCategory(), 15),
                    p.getPrice(),
                    p.getQuantity(),
                    status));
        }
        
        return report.toString();
    }
    
    /**
     * Generate low stock report
     */
    public String generateLowStockReport() throws InventoryException {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("LOW STOCK ALERT REPORT\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Product> lowStockProducts = inventoryService.getLowStockProducts();
        
        report.append(String.format("Total Low Stock Items: %d\n\n", lowStockProducts.size()));
        
        report.append(String.format("%-10s %-30s %-10s %-10s %-12s\n",
                "ID", "Name", "Current", "Min Level", "Required"));
        report.append("-".repeat(80)).append("\n");
        
        for (Product p : lowStockProducts) {
            int required = Math.max(0, p.getMinStockLevel() - p.getQuantity() + 10);
            report.append(String.format("%-10s %-30s %-10d %-10d %-12d\n",
                    p.getProductId(),
                    truncate(p.getName(), 30),
                    p.getQuantity(),
                    p.getMinStockLevel(),
                    required));
        }
        
        return report.toString();
    }
    
    /**
     * Generate sales report
     */
    public String generateSalesReport(LocalDateTime start, LocalDateTime end) 
            throws InventoryException {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("SALES REPORT\n");
        report.append("Period: ").append(start.format(DATE_FORMAT)).append(" to ")
              .append(end.format(DATE_FORMAT)).append("\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Transaction> sales = transactionService.getTransactionsByDateRange(start, end).stream()
                .filter(t -> t.getType() == Transaction.TransactionType.SALE)
                .collect(java.util.stream.Collectors.toList());
        
        double totalSales = sales.stream().mapToDouble(Transaction::getTotalAmount).sum();
        int totalQuantity = sales.stream().mapToInt(Transaction::getQuantity).sum();
        
        report.append(String.format("Total Transactions: %d\n", sales.size()));
        report.append(String.format("Total Items Sold: %d\n", totalQuantity));
        report.append(String.format("Total Sales Amount: $%.2f\n\n", totalSales));
        
        report.append(String.format("%-20s %-12s %-10s %-12s %-15s\n",
                "Date", "Product ID", "Quantity", "Amount", "Performed By"));
        report.append("-".repeat(80)).append("\n");
        
        for (Transaction t : sales) {
            report.append(String.format("%-20s %-12s %-10d $%-11.2f %-15s\n",
                    t.getTransactionDate().format(DATE_FORMAT),
                    t.getProductId(),
                    t.getQuantity(),
                    t.getTotalAmount(),
                    truncate(t.getPerformedBy(), 15)));
        }
        
        return report.toString();
    }
    
    /**
     * Generate supplier report
     */
    public String generateSupplierReport() throws InventoryException {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("SUPPLIER PERFORMANCE REPORT\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        
        report.append(String.format("Total Suppliers: %d\n\n", suppliers.size()));
        
        report.append(String.format("%-10s %-25s %-15s %-10s %-12s %-10s\n",
                "ID", "Name", "Contact", "Phone", "Orders", "Rating"));
        report.append("-".repeat(80)).append("\n");
        
        for (Supplier s : suppliers) {
            report.append(String.format("%-10s %-25s %-15s %-10s %-12d %-10.2f\n",
                    s.getSupplierId(),
                    truncate(s.getName(), 25),
                    truncate(s.getContactPerson(), 15),
                    s.getPhone(),
                    s.getTotalOrders(),
                    s.getRating()));
        }
        
        return report.toString();
    }
    
    /**
     * Generate alert summary report
     */
    public String generateAlertReport() throws InventoryException {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(80)).append("\n");
        report.append("ALERT SUMMARY REPORT\n");
        report.append("Generated: ").append(LocalDateTime.now().format(DATE_FORMAT)).append("\n");
        report.append("=".repeat(80)).append("\n\n");
        
        List<Alert> alerts = alertService.getUnresolvedAlerts();
        
        report.append(String.format("Total Unresolved Alerts: %d\n\n", alerts.size()));
        
        report.append(String.format("%-12s %-15s %-12s %-40s\n",
                "Alert ID", "Type", "Priority", "Message"));
        report.append("-".repeat(80)).append("\n");
        
        for (Alert a : alerts) {
            report.append(String.format("%-12s %-15s %-12s %-40s\n",
                    a.getAlertId(),
                    a.getType(),
                    a.getPriority(),
                    truncate(a.getMessage(), 40)));
        }
        
        return report.toString();
    }
    
    /**
     * Save report to file
     */
    public void saveReportToFile(String reportContent, String filename) throws IOException {
        String fullPath = "reports/" + filename;
        FileHandler.writeTextToFile(fullPath, reportContent);
    }
    
    /**
     * Helper method to truncate strings
     */
    private String truncate(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
}
