# Quick Reference Guide - Smart Inventory Management System

## Quick Start

### Compile
```powershell
cd c:\Users\rupes\OneDrive\Desktop\java_project
javac -d bin -sourcepath src src\com\inventory\Main.java
```

### Run
```powershell
java -cp bin com.inventory.Main
```

### Default Login
- Username: `admin`
- Password: `admin123`

---

## Common Operations

### 1. Add Product
```
Main Menu → 1 (Inventory Management) → 1 (Add Product)
Required: Name, Category, Price, Quantity, Min Stock Level
Optional: Supplier ID, Description
```

### 2. Record Sale
```
Main Menu → 3 (Transaction Management) → 1 (Record Sale)
Required: Product ID, Quantity
Optional: Remarks
```

### 3. Record Purchase
```
Main Menu → 3 (Transaction Management) → 2 (Record Purchase)
Required: Product ID, Quantity, Price per Unit
Optional: Supplier ID, Remarks
```

### 4. Generate Report
```
Main Menu → 4 (Reports & Analytics) → Select Report Type
Options to save to file (reports/ directory)
```

### 5. View Alerts
```
Main Menu → 5 (Alert Management) → 2 (View Unresolved Alerts)
```

---

## File Locations

### Data Files
- `data/products.dat` - Product inventory
- `data/suppliers.dat` - Supplier information
- `data/transactions.dat` - All transactions
- `data/users.dat` - User accounts
- `data/alerts.dat` - System alerts

### Logs
- `logs/application.log` - System logs

### Reports
- `reports/*.txt` - Generated reports

---

## User Roles & Permissions

### Admin (Full Access)
- All inventory operations
- All supplier operations
- All transaction operations
- All reports
- Alert management
- **User management**

### Manager
- All inventory operations
- All supplier operations
- All transaction operations
- All reports
- Alert management
- ❌ User management

### Cashier
- View products
- Search products
- Record sales
- ❌ Cannot modify inventory
- ❌ Cannot manage suppliers
- ❌ Cannot view all reports

---

## Menu Structure

```
Main Menu
├── 1. Inventory Management
│   ├── 1. Add Product
│   ├── 2. Update Product
│   ├── 3. Delete Product
│   ├── 4. View All Products
│   ├── 5. Search Product
│   ├── 6. View Low Stock Products
│   └── 7. Back
│
├── 2. Supplier Management
│   ├── 1. Add Supplier
│   ├── 2. View All Suppliers
│   ├── 3. Update Supplier
│   ├── 4. Delete Supplier
│   └── 5. Back
│
├── 3. Transaction Management
│   ├── 1. Record Sale
│   ├── 2. Record Purchase
│   ├── 3. View All Transactions
│   ├── 4. View Sales Summary
│   └── 5. Back
│
├── 4. Reports & Analytics
│   ├── 1. Inventory Status Report
│   ├── 2. Low Stock Report
│   ├── 3. Sales Report
│   ├── 4. Supplier Performance Report
│   ├── 5. Alert Summary Report
│   └── 6. Back
│
├── 5. Alert Management
│   ├── 1. View All Alerts
│   ├── 2. View Unresolved Alerts
│   ├── 3. Resolve Alert
│   └── 4. Back
│
├── 6. User Management (Admin Only)
│   ├── 1. Add User
│   ├── 2. View All Users
│   ├── 3. Deactivate User
│   └── 4. Back
│
└── 7. Logout
```

---

## Troubleshooting

### Problem: Compilation Error
**Solution**: Ensure JDK is installed and JAVA_HOME is set
```powershell
java -version
javac -version
```

### Problem: Class Not Found
**Solution**: Verify classpath and bin directory exists
```powershell
java -cp bin com.inventory.Main
```

### Problem: Data Not Persisting
**Solution**: Check data directory permissions and logs
```powershell
Get-Content logs\application.log
```

### Problem: Login Failed
**Solution**: Use default credentials or check logs
- Username: admin
- Password: admin123

### Problem: Alert Not Generating
**Solution**: Check product minimum stock level settings
- Alerts generate when quantity <= minStockLevel

---

## ID Format Reference

- Product ID: `P0001`, `P0002`, etc.
- Supplier ID: `S0001`, `S0002`, etc.
- Transaction ID: `T20251123143000-0001` (timestamp-based)
- User ID: `U001`, `U002`, etc.
- Alert ID: `A000001`, `A000002`, etc.

---

## Validation Rules

### Product
- Name: Required, non-empty
- Price: Must be positive
- Quantity: Non-negative integer
- Min Stock Level: Non-negative integer

### Supplier
- Name: Required, non-empty
- Phone: 10 digits exactly
- Email: Valid email format
- Contact Person: Required

### User
- Username: Required, unique
- Password: Minimum 6 characters
- Email: Valid email format

---

## Tips & Best Practices

1. **Regularly Check Alerts**: Low stock alerts help prevent stock-outs
2. **Generate Reports Weekly**: Track business performance
3. **Backup Data Files**: Copy data/*.dat files regularly
4. **Review Logs**: Check logs/application.log for issues
5. **Use Descriptive Remarks**: Add meaningful notes to transactions
6. **Update Supplier Ratings**: Keep supplier performance current
7. **Set Realistic Min Stock Levels**: Based on sales velocity

---

## Support & Documentation

- **Full Documentation**: README.md
- **Project Report**: PROJECT_REPORT.md
- **Design Docs**: docs/DESIGN_DOCUMENTATION.md
- **Build Instructions**: BUILD_AND_RUN.md

---

## Sample Workflow

### Initial Setup
1. Login as admin
2. Add suppliers
3. Add products with supplier links
4. Create additional user accounts

### Daily Operations
1. Check unresolved alerts
2. Record purchases for low stock items
3. Record sales as they occur
4. View inventory status

### Weekly Review
1. Generate sales report
2. Review supplier performance
3. Check low stock report
4. Generate inventory status report

---

**Version**: 1.0  
**Last Updated**: 2025-11-23
