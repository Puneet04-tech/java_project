# Smart Inventory Management System

## Project Overview
The Smart Inventory Management System is a comprehensive Java-based solution designed for small to medium-sized retail businesses to efficiently manage their product inventory, suppliers, and sales transactions. The system provides real-time inventory tracking, automated alerts for low stock, supplier management, and detailed reporting capabilities.

## Problem Statement
Small retail businesses often struggle with manual inventory management, leading to:
- Stock-outs causing lost sales opportunities
- Overstocking resulting in capital wastage
- Lack of supplier performance tracking
- Difficulty in generating business insights and reports
- Manual errors in stock counting and transaction recording

## Features

### Core Functional Modules:
1. **Inventory Management Module**
   - Add, update, delete, and search products
   - Real-time stock level tracking
   - Product categorization
   - Automatic low-stock alerts
   - Batch operations for bulk updates

2. **Supplier Management Module**
   - Maintain supplier information
   - Track supplier performance ratings
   - Link products to suppliers
   - Supplier contact management
   - Order history tracking

3. **Transaction Management Module**
   - Record sales transactions (SALE)
   - Record purchase/restock transactions (PURCHASE)
   - Automatic inventory adjustment
   - Transaction history and audit trail
   - Return and adjustment handling

4. **Reporting & Analytics Module**
   - Generate inventory status reports
   - Sales analytics and trends
   - Low stock alerts and notifications
   - Supplier performance reports
   - Export reports to text files

5. **User Management Module**
   - Role-based access control (ADMIN, MANAGER, CASHIER)
   - User authentication
   - Activity logging
   - User profile management

## Technologies & Tools Used
- **Language:** Java 8+
- **Architecture:** Layered Architecture (MVC Pattern)
- **Data Persistence:** File-based storage (CSV/Serialization)
- **Design Patterns:** Singleton, Factory, DAO, Observer
- **Version Control:** Git & GitHub
- **Build Tool:** Manual compilation / Maven compatible structure

## Non-Functional Requirements
1. **Performance:** System should handle up to 10,000 products and 100,000 transactions
2. **Security:** Role-based access control with encrypted password storage
3. **Usability:** Intuitive CLI interface with clear menu navigation
4. **Reliability:** Automatic data backup and recovery mechanisms
5. **Maintainability:** Modular code structure with comprehensive documentation
6. **Error Handling:** Graceful error handling with detailed logging
7. **Data Integrity:** ACID properties for all transactions
8. **Scalability:** Designed to easily migrate to database backend

## Project Structure
```
java_project/
├── src/
│   └── com/
│       └── inventory/
│           ├── model/          # Entity classes (Product, Supplier, Transaction, User, Alert)
│           ├── dao/            # Data Access Objects (interfaces & implementations)
│           ├── service/        # Business logic layer
│           ├── controller/     # Application controllers
│           ├── util/           # Utility classes (Logger, Validator, FileHandler)
│           ├── exception/      # Custom exception classes
│           └── Main.java       # Application entry point
├── data/                       # Data storage files
├── logs/                       # Application logs
├── reports/                    # Generated reports
├── docs/                       # UML diagrams and documentation
├── README.md
└── statement.md
```

## Installation & Setup

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Command line terminal (PowerShell/CMD for Windows)

### Steps to Install and Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/smart-inventory-system.git
   cd smart-inventory-system
   ```

2. **Navigate to the Project Directory**
   ```powershell
   cd c:\Users\rupes\OneDrive\Desktop\java_project
   ```

3. **Compile the Java Files**
   ```powershell
   javac -d bin -sourcepath src src/com/inventory/Main.java
   ```

4. **Run the Application**
   ```powershell
   java -cp bin com.inventory.Main
   ```

5. **Default Login Credentials**
   - Username: `admin`
   - Password: `admin123`

## How to Use

### Main Menu Options:
1. **Inventory Management** - Manage products (add, update, delete, search, view all)
2. **Supplier Management** - Manage suppliers and their details
3. **Transaction Management** - Record sales and purchases
4. **Reports & Analytics** - Generate various business reports
5. **Alert Management** - View low stock alerts and notifications
6. **User Management** - Manage system users (admin only)
7. **Logout** - Exit the application

### Example Workflow:
1. Login with credentials
2. Add suppliers first
3. Add products linked to suppliers
4. Record purchase transactions to stock inventory
5. Record sales transactions when items are sold
6. View reports to analyze business performance
7. Check alerts for low stock items

## Testing Instructions

### Manual Testing:
1. Test user authentication with valid/invalid credentials
2. Add sample products and verify data persistence
3. Record transactions and verify inventory updates
4. Generate reports and verify accuracy
5. Test all CRUD operations for each module
6. Test error handling with invalid inputs
7. Test low stock alert generation

### Test Scenarios Included:
- Boundary value testing for stock quantities
- Input validation for all data fields
- Transaction rollback on failures
- Concurrent transaction handling
- Data persistence and recovery

## Screenshots
*(Screenshots can be added showing the CLI interface, menu options, and sample outputs)*

## Future Enhancements
- GUI interface using JavaFX or Swing
- Database integration (MySQL/PostgreSQL)
- Barcode scanning integration
- Multi-store management
- Cloud-based deployment
- Mobile app for inventory checks
- Advanced analytics with charts
- Email/SMS notifications for alerts

## Contributors
- [Your Name] - Developer

## License
This project is created for educational purposes as part of VITyarthi coursework.

## Contact
For any queries or suggestions, please contact: [your.email@example.com]
