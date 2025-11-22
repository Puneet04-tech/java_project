package com.inventory.controller;

import com.inventory.model.*;
import com.inventory.service.*;
import com.inventory.exception.*;
import com.inventory.util.Logger;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main controller for Inventory Management System CLI interface
 */
public class InventoryController {
    private Scanner scanner;
    private UserService userService;
    private InventoryService inventoryService;
    private SupplierService supplierService;
    private TransactionService transactionService;
    private AlertService alertService;
    private ReportService reportService;
    private User currentUser;
    
    public InventoryController() {
        this.scanner = new Scanner(System.in);
        this.userService = UserService.getInstance();
        this.inventoryService = InventoryService.getInstance();
        this.supplierService = SupplierService.getInstance();
        this.transactionService = TransactionService.getInstance();
        this.alertService = AlertService.getInstance();
        this.reportService = ReportService.getInstance();
    }
    
    /**
     * Start the application
     */
    public void start() {
        displayWelcome();
        
        if (!login()) {
            System.out.println("Exiting application. Goodbye!");
            return;
        }
        
        boolean running = true;
        while (running) {
            try {
                displayMainMenu();
                int choice = getIntInput("Enter your choice: ");
                
                switch (choice) {
                    case 1:
                        inventoryMenu();
                        break;
                    case 2:
                        supplierMenu();
                        break;
                    case 3:
                        transactionMenu();
                        break;
                    case 4:
                        reportMenu();
                        break;
                    case 5:
                        alertMenu();
                        break;
                    case 6:
                        if (currentUser.getRole() == User.UserRole.ADMIN) {
                            userMenu();
                        } else {
                            System.out.println("Access denied. Admin privileges required.");
                        }
                        break;
                    case 7:
                        userService.logout();
                        System.out.println("\nLogged out successfully. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                Logger.error("Error in main menu", (Exception) e);
            }
        }
        
        scanner.close();
    }
    
    /**
     * Display welcome message
     */
    private void displayWelcome() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     SMART INVENTORY MANAGEMENT SYSTEM");
        System.out.println("     Efficient Inventory Tracking Solution");
        System.out.println("=".repeat(60) + "\n");
    }
    
    /**
     * Handle user login
     */
    private boolean login() {
        System.out.println("Please login to continue:\n");
        
        for (int attempts = 0; attempts < 3; attempts++) {
            try {
                System.out.print("Username: ");
                String username = scanner.nextLine().trim();
                
                System.out.print("Password: ");
                String password = scanner.nextLine().trim();
                
                currentUser = userService.authenticate(username, password);
                System.out.println("\nLogin successful! Welcome, " + currentUser.getFullName());
                System.out.println("Role: " + currentUser.getRole());
                return true;
                
            } catch (AuthenticationException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Attempts remaining: " + (2 - attempts) + "\n");
            }
        }
        
        System.out.println("Maximum login attempts exceeded.");
        return false;
    }
    
    /**
     * Display main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Inventory Management");
        System.out.println("2. Supplier Management");
        System.out.println("3. Transaction Management");
        System.out.println("4. Reports & Analytics");
        System.out.println("5. Alert Management");
        System.out.println("6. User Management (Admin Only)");
        System.out.println("7. Logout");
        System.out.println("=".repeat(60));
    }
    
    /**
     * Inventory management menu
     */
    private void inventoryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- INVENTORY MANAGEMENT ---");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Search Product");
            System.out.println("6. View Low Stock Products");
            System.out.println("7. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        addProduct();
                        break;
                    case 2:
                        updateProduct();
                        break;
                    case 3:
                        deleteProduct();
                        break;
                    case 4:
                        viewAllProducts();
                        break;
                    case 5:
                        searchProduct();
                        break;
                    case 6:
                        viewLowStockProducts();
                        break;
                    case 7:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addProduct() throws Exception {
        System.out.println("\n--- ADD NEW PRODUCT ---");
        System.out.print("Product Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        
        double price = getDoubleInput("Price: ");
        int quantity = getIntInput("Initial Quantity: ");
        int minStock = getIntInput("Minimum Stock Level: ");
        
        System.out.print("Supplier ID (or press Enter to skip): ");
        String supplierId = scanner.nextLine().trim();
        
        System.out.print("Description: ");
        String description = scanner.nextLine().trim();
        
        Product product = inventoryService.addProduct(name, category, price, quantity,
                minStock, supplierId.isEmpty() ? null : supplierId, description);
        
        System.out.println("\nProduct added successfully!");
        System.out.println(product);
    }
    
    private void updateProduct() throws Exception {
        System.out.print("\nEnter Product ID to update: ");
        String productId = scanner.nextLine().trim();
        
        Product product = inventoryService.getProductById(productId);
        System.out.println("\nCurrent Details: " + product);
        
        System.out.print("New Name (Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) product.setName(name);
        
        System.out.print("New Price (0 to keep current): ");
        double price = getDoubleInput("");
        if (price > 0) product.setPrice(price);
        
        System.out.print("New Quantity (-1 to keep current): ");
        int quantity = getIntInput("");
        if (quantity >= 0) product.setQuantity(quantity);
        
        inventoryService.updateProduct(product);
        System.out.println("\nProduct updated successfully!");
    }
    
    private void deleteProduct() throws Exception {
        System.out.print("\nEnter Product ID to delete: ");
        String productId = scanner.nextLine().trim();
        
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            inventoryService.deleteProduct(productId);
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    private void viewAllProducts() throws Exception {
        List<Product> products = inventoryService.getAllProducts();
        
        System.out.println("\n--- ALL PRODUCTS ---");
        System.out.println(String.format("%-10s %-25s %-15s %-10s %-10s",
                "ID", "Name", "Category", "Price", "Quantity"));
        System.out.println("-".repeat(70));
        
        for (Product p : products) {
            System.out.println(String.format("%-10s %-25s %-15s $%-9.2f %-10d",
                    p.getProductId(),
                    truncate(p.getName(), 25),
                    truncate(p.getCategory(), 15),
                    p.getPrice(),
                    p.getQuantity()));
        }
        System.out.println("\nTotal Products: " + products.size());
    }
    
    private void searchProduct() throws Exception {
        System.out.print("\nEnter product name to search: ");
        String name = scanner.nextLine().trim();
        
        List<Product> products = inventoryService.searchProductsByName(name);
        
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("\n--- SEARCH RESULTS ---");
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }
    
    private void viewLowStockProducts() throws Exception {
        List<Product> products = inventoryService.getLowStockProducts();
        
        System.out.println("\n--- LOW STOCK PRODUCTS ---");
        if (products.isEmpty()) {
            System.out.println("No low stock products.");
        } else {
            for (Product p : products) {
                System.out.println(String.format("%s - %s: Current=%d, Min=%d",
                        p.getProductId(), p.getName(), p.getQuantity(), p.getMinStockLevel()));
            }
        }
    }
    
    /**
     * Supplier management menu
     */
    private void supplierMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SUPPLIER MANAGEMENT ---");
            System.out.println("1. Add Supplier");
            System.out.println("2. View All Suppliers");
            System.out.println("3. Update Supplier");
            System.out.println("4. Delete Supplier");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        addSupplier();
                        break;
                    case 2:
                        viewAllSuppliers();
                        break;
                    case 3:
                        updateSupplier();
                        break;
                    case 4:
                        deleteSupplier();
                        break;
                    case 5:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addSupplier() throws Exception {
        System.out.println("\n--- ADD NEW SUPPLIER ---");
        System.out.print("Supplier Name: ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Contact Person: ");
        String contact = scanner.nextLine().trim();
        
        System.out.print("Phone (10 digits): ");
        String phone = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.print("Address: ");
        String address = scanner.nextLine().trim();
        
        Supplier supplier = supplierService.addSupplier(name, contact, phone, email, address);
        System.out.println("\nSupplier added successfully!");
        System.out.println(supplier);
    }
    
    private void viewAllSuppliers() throws Exception {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        
        System.out.println("\n--- ALL SUPPLIERS ---");
        System.out.println(String.format("%-10s %-25s %-15s %-12s %-8s",
                "ID", "Name", "Contact", "Phone", "Rating"));
        System.out.println("-".repeat(70));
        
        for (Supplier s : suppliers) {
            System.out.println(String.format("%-10s %-25s %-15s %-12s %.2f",
                    s.getSupplierId(),
                    truncate(s.getName(), 25),
                    truncate(s.getContactPerson(), 15),
                    s.getPhone(),
                    s.getRating()));
        }
        System.out.println("\nTotal Suppliers: " + suppliers.size());
    }
    
    private void updateSupplier() throws Exception {
        System.out.print("\nEnter Supplier ID to update: ");
        String supplierId = scanner.nextLine().trim();
        
        Supplier supplier = supplierService.getSupplierById(supplierId);
        System.out.println("\nCurrent Details: " + supplier);
        
        System.out.print("New Name (Enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) supplier.setName(name);
        
        System.out.print("New Phone (Enter to keep current): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) supplier.setPhone(phone);
        
        supplierService.updateSupplier(supplier);
        System.out.println("\nSupplier updated successfully!");
    }
    
    private void deleteSupplier() throws Exception {
        System.out.print("\nEnter Supplier ID to delete: ");
        String supplierId = scanner.nextLine().trim();
        
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            supplierService.deleteSupplier(supplierId);
            System.out.println("Supplier deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    
    /**
     * Transaction management menu
     */
    private void transactionMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- TRANSACTION MANAGEMENT ---");
            System.out.println("1. Record Sale");
            System.out.println("2. Record Purchase");
            System.out.println("3. View All Transactions");
            System.out.println("4. View Sales Summary");
            System.out.println("5. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        recordSale();
                        break;
                    case 2:
                        recordPurchase();
                        break;
                    case 3:
                        viewAllTransactions();
                        break;
                    case 4:
                        viewSalesSummary();
                        break;
                    case 5:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void recordSale() throws Exception {
        System.out.println("\n--- RECORD SALE ---");
        System.out.print("Product ID: ");
        String productId = scanner.nextLine().trim();
        
        int quantity = getIntInput("Quantity: ");
        
        System.out.print("Remarks (optional): ");
        String remarks = scanner.nextLine().trim();
        
        Transaction transaction = transactionService.recordSale(productId, quantity,
                currentUser.getUserId(), remarks);
        
        System.out.println("\nSale recorded successfully!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Total Amount: $" + transaction.getTotalAmount());
    }
    
    private void recordPurchase() throws Exception {
        System.out.println("\n--- RECORD PURCHASE ---");
        System.out.print("Product ID: ");
        String productId = scanner.nextLine().trim();
        
        int quantity = getIntInput("Quantity: ");
        double price = getDoubleInput("Price per Unit: ");
        
        System.out.print("Supplier ID (optional): ");
        String supplierId = scanner.nextLine().trim();
        
        System.out.print("Remarks (optional): ");
        String remarks = scanner.nextLine().trim();
        
        Transaction transaction = transactionService.recordPurchase(productId, quantity, price,
                supplierId.isEmpty() ? null : supplierId, currentUser.getUserId(), remarks);
        
        System.out.println("\nPurchase recorded successfully!");
        System.out.println("Transaction ID: " + transaction.getTransactionId());
        System.out.println("Total Amount: $" + transaction.getTotalAmount());
    }
    
    private void viewAllTransactions() throws Exception {
        List<Transaction> transactions = transactionService.getAllTransactions();
        
        System.out.println("\n--- ALL TRANSACTIONS ---");
        System.out.println(String.format("%-25s %-12s %-8s %-8s %-12s",
                "Transaction ID", "Product", "Type", "Qty", "Amount"));
        System.out.println("-".repeat(70));
        
        for (Transaction t : transactions) {
            System.out.println(String.format("%-25s %-12s %-8s %-8d $%-11.2f",
                    t.getTransactionId(),
                    t.getProductId(),
                    t.getType(),
                    t.getQuantity(),
                    t.getTotalAmount()));
        }
        System.out.println("\nTotal Transactions: " + transactions.size());
    }
    
    private void viewSalesSummary() throws Exception {
        double totalSales = transactionService.calculateTotalSales();
        int transactionCount = transactionService.getTransactionsByType(
                Transaction.TransactionType.SALE).size();
        
        System.out.println("\n--- SALES SUMMARY ---");
        System.out.println("Total Sales Transactions: " + transactionCount);
        System.out.println("Total Sales Amount: $" + String.format("%.2f", totalSales));
    }
    
    /**
     * Report menu
     */
    private void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- REPORTS & ANALYTICS ---");
            System.out.println("1. Inventory Status Report");
            System.out.println("2. Low Stock Report");
            System.out.println("3. Sales Report");
            System.out.println("4. Supplier Performance Report");
            System.out.println("5. Alert Summary Report");
            System.out.println("6. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        displayInventoryReport();
                        break;
                    case 2:
                        displayLowStockReport();
                        break;
                    case 3:
                        displaySalesReport();
                        break;
                    case 4:
                        displaySupplierReport();
                        break;
                    case 5:
                        displayAlertReport();
                        break;
                    case 6:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void displayInventoryReport() throws Exception {
        String report = reportService.generateInventoryReport();
        System.out.println("\n" + report);
        
        System.out.print("\nSave to file? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            String filename = "inventory_report_" + System.currentTimeMillis() + ".txt";
            reportService.saveReportToFile(report, filename);
            System.out.println("Report saved to reports/" + filename);
        }
    }
    
    private void displayLowStockReport() throws Exception {
        String report = reportService.generateLowStockReport();
        System.out.println("\n" + report);
        
        System.out.print("\nSave to file? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            String filename = "lowstock_report_" + System.currentTimeMillis() + ".txt";
            reportService.saveReportToFile(report, filename);
            System.out.println("Report saved to reports/" + filename);
        }
    }
    
    private void displaySalesReport() throws Exception {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(30); // Last 30 days
        
        String report = reportService.generateSalesReport(start, end);
        System.out.println("\n" + report);
        
        System.out.print("\nSave to file? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            String filename = "sales_report_" + System.currentTimeMillis() + ".txt";
            reportService.saveReportToFile(report, filename);
            System.out.println("Report saved to reports/" + filename);
        }
    }
    
    private void displaySupplierReport() throws Exception {
        String report = reportService.generateSupplierReport();
        System.out.println("\n" + report);
        
        System.out.print("\nSave to file? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            String filename = "supplier_report_" + System.currentTimeMillis() + ".txt";
            reportService.saveReportToFile(report, filename);
            System.out.println("Report saved to reports/" + filename);
        }
    }
    
    private void displayAlertReport() throws Exception {
        String report = reportService.generateAlertReport();
        System.out.println("\n" + report);
    }
    
    /**
     * Alert menu
     */
    private void alertMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ALERT MANAGEMENT ---");
            System.out.println("1. View All Alerts");
            System.out.println("2. View Unresolved Alerts");
            System.out.println("3. Resolve Alert");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        viewAllAlerts();
                        break;
                    case 2:
                        viewUnresolvedAlerts();
                        break;
                    case 3:
                        resolveAlert();
                        break;
                    case 4:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void viewAllAlerts() throws Exception {
        List<Alert> alerts = alertService.getAllAlerts();
        displayAlerts(alerts, "ALL ALERTS");
    }
    
    private void viewUnresolvedAlerts() throws Exception {
        List<Alert> alerts = alertService.getUnresolvedAlerts();
        displayAlerts(alerts, "UNRESOLVED ALERTS");
    }
    
    private void displayAlerts(List<Alert> alerts, String title) {
        System.out.println("\n--- " + title + " ---");
        if (alerts.isEmpty()) {
            System.out.println("No alerts found.");
        } else {
            System.out.println(String.format("%-12s %-15s %-12s %-10s %-30s",
                    "Alert ID", "Type", "Priority", "Product", "Message"));
            System.out.println("-".repeat(80));
            
            for (Alert a : alerts) {
                System.out.println(String.format("%-12s %-15s %-12s %-10s %-30s",
                        a.getAlertId(),
                        a.getType(),
                        a.getPriority(),
                        a.getProductId(),
                        truncate(a.getMessage(), 30)));
            }
            System.out.println("\nTotal Alerts: " + alerts.size());
        }
    }
    
    private void resolveAlert() throws Exception {
        System.out.print("\nEnter Alert ID to resolve: ");
        String alertId = scanner.nextLine().trim();
        
        alertService.resolveAlert(alertId, currentUser.getUserId());
        System.out.println("Alert resolved successfully!");
    }
    
    /**
     * User management menu (Admin only)
     */
    private void userMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println("1. Add User");
            System.out.println("2. View All Users");
            System.out.println("3. Deactivate User");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Enter choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        viewAllUsers();
                        break;
                    case 3:
                        deactivateUser();
                        break;
                    case 4:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addUser() throws Exception {
        System.out.println("\n--- ADD NEW USER ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        System.out.println("Role: 1-ADMIN, 2-MANAGER, 3-CASHIER");
        int roleChoice = getIntInput("Select role: ");
        
        User.UserRole role;
        switch (roleChoice) {
            case 1:
                role = User.UserRole.ADMIN;
                break;
            case 2:
                role = User.UserRole.MANAGER;
                break;
            case 3:
                role = User.UserRole.CASHIER;
                break;
            default:
                throw new Exception("Invalid role selection");
        }
        
        User user = userService.addUser(username, password, fullName, email, role);
        System.out.println("\nUser added successfully!");
        System.out.println(user);
    }
    
    private void viewAllUsers() throws Exception {
        List<User> users = userService.getAllUsers();
        
        System.out.println("\n--- ALL USERS ---");
        System.out.println(String.format("%-10s %-15s %-25s %-12s %-8s",
                "ID", "Username", "Full Name", "Role", "Active"));
        System.out.println("-".repeat(70));
        
        for (User u : users) {
            System.out.println(String.format("%-10s %-15s %-25s %-12s %-8s",
                    u.getUserId(),
                    u.getUsername(),
                    truncate(u.getFullName(), 25),
                    u.getRole(),
                    u.isActive() ? "Yes" : "No"));
        }
        System.out.println("\nTotal Users: " + users.size());
    }
    
    private void deactivateUser() throws Exception {
        System.out.print("\nEnter User ID to deactivate: ");
        String userId = scanner.nextLine().trim();
        
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        
        if (confirm.equalsIgnoreCase("yes")) {
            userService.deactivateUser(userId);
            System.out.println("User deactivated successfully!");
        } else {
            System.out.println("Deactivation cancelled.");
        }
    }
    
    // Helper methods
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }
    
    private String truncate(String str, int length) {
        if (str == null) return "";
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
}
