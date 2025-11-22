# Design Documentation: Smart Inventory Management System

## 1. System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                          │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │        CLI Interface (InventoryController)                │  │
│  │     - User Input/Output                                   │  │
│  │     - Menu Navigation                                     │  │
│  │     - Data Display                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │ Inventory   │  │  Supplier   │  │Transaction  │           │
│  │  Service    │  │   Service   │  │  Service    │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │   User      │  │   Alert     │  │   Report    │           │
│  │  Service    │  │  Service    │  │  Service    │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
│                                                                  │
│  Business Logic, Validation, Transaction Management            │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                     DAO (Data Access) LAYER                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐           │
│  │  Product    │  │  Supplier   │  │Transaction  │           │
│  │    DAO      │  │    DAO      │  │    DAO      │           │
│  └─────────────┘  └─────────────┘  └─────────────┘           │
│  ┌─────────────┐  ┌─────────────┐                             │
│  │   User      │  │   Alert     │                             │
│  │    DAO      │  │    DAO      │                             │
│  └─────────────┘  └─────────────┘                             │
│                                                                  │
│  Data Persistence, CRUD Operations                             │
└─────────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────────┐
│                    DATA PERSISTENCE LAYER                       │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │            File-based Storage                             │  │
│  │  - products.dat  - suppliers.dat  - transactions.dat     │  │
│  │  - users.dat     - alerts.dat                            │  │
│  │                                                           │  │
│  │  Serialization/Deserialization using Java I/O            │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

CROSS-CUTTING CONCERNS:
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│   Logger     │  │  Validator   │  │ IDGenerator  │
│   Utility    │  │   Utility    │  │   Utility    │
└──────────────┘  └──────────────┘  └──────────────┘
┌──────────────┐  ┌──────────────────────────────────┐
│ FileHandler  │  │    Exception Handling            │
│   Utility    │  │  (Custom Exception Classes)      │
└──────────────┘  └──────────────────────────────────┘
```

## 2. Use Case Diagram

```
                    Smart Inventory Management System
                    
┌────────────────┐                                    ┌────────────────┐
│                │                                    │                │
│     Admin      │                                    │    Manager     │
│                │                                    │                │
└────────┬───────┘                                    └────────┬───────┘
         │                                                     │
         │                                                     │
         ├──── Manage Users                                   │
         │                                                     │
         ├──── View All Reports ────────────────────────────── ┤
         │                                                     │
         ├──── Manage Products ─────────────────────────────── ┤
         │                                                     │
         ├──── Manage Suppliers ────────────────────────────── ┤
         │                                                     │
         ├──── Record Purchases ────────────────────────────── ┤
         │                                                     │
         ├──── Record Sales ────────────────────────────────── ┤
         │                                                     │
         ├──── View Alerts ─────────────────────────────────── ┤
         │                                                     │
         ├──── Resolve Alerts ──────────────────────────────── ┤
         │                                                     │
         └──── Generate Reports ────────────────────────────── ┤
                                                               │
                                                               │
                                                    ┌──────────┴───────┐
                                                    │                  │
                                                    │     Cashier      │
                                                    │                  │
                                                    └──────────┬───────┘
                                                               │
                                                               │
                                                               ├──── Record Sales
                                                               │
                                                               ├──── View Products
                                                               │
                                                               └──── Search Products

SYSTEM FUNCTIONS:
• Authentication & Authorization
• Inventory Tracking
• Stock Level Monitoring
• Automatic Alert Generation
• Transaction Logging
• Report Generation
• Data Persistence
```

## 3. Class Diagram (Core Classes)

```
┌─────────────────────────┐         ┌─────────────────────────┐
│       Product           │         │       Supplier          │
├─────────────────────────┤         ├─────────────────────────┤
│ - productId: String     │         │ - supplierId: String    │
│ - name: String          │         │ - name: String          │
│ - category: String      │         │ - contactPerson: String │
│ - price: double         │         │ - phone: String         │
│ - quantity: int         │◄────────┤ - email: String         │
│ - minStockLevel: int    │         │ - address: String       │
│ - supplierId: String    │         │ - rating: double        │
│ - description: String   │         │ - totalOrders: int      │
│ - createdDate: DateTime │         │ - active: boolean       │
├─────────────────────────┤         ├─────────────────────────┤
│ + isLowStock(): boolean │         │ + updateRating()        │
│ + addStock(int)         │         │ + incrementOrderCount() │
│ + reduceStock(int)      │         │ + getPerformanceLevel() │
│ + getTotalValue()       │         └─────────────────────────┘
└─────────────────────────┘

┌─────────────────────────┐         ┌─────────────────────────┐
│      Transaction        │         │         Alert           │
├─────────────────────────┤         ├─────────────────────────┤
│ - transactionId: String │         │ - alertId: String       │
│ - productId: String     │         │ - type: AlertType       │
│ - type: TransactionType │◄────────┤ - priority: Priority    │
│ - quantity: int         │         │ - productId: String     │
│ - pricePerUnit: double  │         │ - message: String       │
│ - totalAmount: double   │         │ - resolved: boolean     │
│ - performedBy: String   │         │ - createdDate: DateTime │
│ - transactionDate: Date │         ├─────────────────────────┤
│ - remarks: String       │         │ + resolve(userId)       │
├─────────────────────────┤         │ + isUrgent(): boolean   │
│ + calculateTotalAmount()│         └─────────────────────────┘
│ + isInbound(): boolean  │
│ + isOutbound(): boolean │
└─────────────────────────┘

┌─────────────────────────┐
│          User           │
├─────────────────────────┤
│ - userId: String        │
│ - username: String      │
│ - passwordHash: String  │
│ - fullName: String      │
│ - email: String         │
│ - role: UserRole        │
│ - active: boolean       │
│ - lastLoginDate: Date   │
├─────────────────────────┤
│ + verifyPassword(pwd)   │
│ + changePassword()      │
│ + hasPermission(perm)   │
│ + updateLastLogin()     │
└─────────────────────────┘
```

## 4. Sequence Diagram: Record Sale Transaction

```
User → Controller → TransactionService → InventoryService → ProductDAO → Database

1. User enters sale details
   │
   ├─→ Controller.recordSale(productId, quantity)
       │
       ├─→ TransactionService.recordSale(productId, quantity, userId, remarks)
           │
           ├─→ InventoryService.getProductById(productId)
           │   │
           │   ├─→ ProductDAO.findById(productId)
           │   │   │
           │   │   ├─→ Read from products.dat
           │   │   │
           │   │   └─→ Return Product
           │   │
           │   └─→ Return Product
           │
           ├─→ Validate stock availability
           │
           ├─→ Create Transaction object
           │
           ├─→ InventoryService.reduceStock(productId, quantity)
           │   │
           │   ├─→ ProductDAO.update(product)
           │   │   │
           │   │   └─→ Write to products.dat
           │   │
           │   └─→ Check stock level and create alert if needed
           │       │
           │       └─→ AlertDAO.save(alert)
           │
           ├─→ TransactionDAO.save(transaction)
           │   │
           │   └─→ Write to transactions.dat
           │
           └─→ Return Transaction
       │
       └─→ Display success message to User
```

## 5. Workflow Diagram: Inventory Management Process

```
                        START
                          │
                          ▼
                   ┌─────────────┐
                   │   Login     │
                   └──────┬──────┘
                          │
                          ▼
                   ┌─────────────┐
                   │ Authenticate│
                   └──────┬──────┘
                          │
                   ┌──────┴──────┐
                   │             │
             Valid │             │ Invalid
                   │             │
                   ▼             ▼
            ┌──────────┐    ┌────────┐
            │Main Menu │    │ Denied │
            └────┬─────┘    └────────┘
                 │
      ┌──────────┼──────────┬────────────┬──────────┐
      │          │          │            │          │
      ▼          ▼          ▼            ▼          ▼
  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
  │Inventory│ │Supplier│ │Trans-  │ │Reports │ │Alerts │
  │ Mgmt   │ │ Mgmt   │ │action  │ │        │ │       │
  └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬───┘
      │          │          │          │          │
      ▼          ▼          ▼          ▼          ▼
  ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
  │• Add   │ │• Add   │ │• Sale  │ │•Invent │ │• View  │
  │• Update│ │• Update│ │• Purchase│ │•Sales │ │• Resolve│
  │• Delete│ │• Delete│ │• View  │ │•Low Stk│ │        │
  │• Search│ │• View  │ │        │ │•Supplier│ │       │
  └───┬────┘ └───┬────┘ └───┬────┘ └───┬────┘ └───┬───┘
      │          │          │          │          │
      └──────────┴──────────┴──────────┴──────────┘
                          │
                          ▼
                   ┌─────────────┐
                   │Update Database│
                   └──────┬──────┘
                          │
                          ▼
                   ┌─────────────┐
                   │Generate Logs │
                   └──────┬──────┘
                          │
                          ▼
                   ┌─────────────┐
                   │Check Alerts  │
                   └──────┬──────┘
                          │
                          ▼
                        END
```

## 6. ER Diagram (Entity Relationships)

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│   PRODUCT   │         │  SUPPLIER   │         │    USER     │
├─────────────┤         ├─────────────┤         ├─────────────┤
│ productId PK│◄───┐    │supplierId PK│         │  userId PK  │
│ name        │    │    │ name        │         │  username   │
│ category    │    └────┤ products    │         │  password   │
│ price       │         │ rating      │         │  role       │
│ quantity    │         │ totalOrders │         │  active     │
│ supplierId  │         └─────────────┘         └──────┬──────┘
└──────┬──────┘                                        │
       │                                               │
       │ 1:N                                           │ 1:N
       │                                               │
       ▼                                               ▼
┌─────────────┐                              ┌─────────────┐
│ TRANSACTION │                              │    ALERT    │
├─────────────┤                              ├─────────────┤
│transactionId│                              │  alertId PK │
│ productId FK│                              │ productId FK│
│ type        │                              │  type       │
│ quantity    │                              │  priority   │
│ amount      │                              │  message    │
│ performedBy │◄─────────────────────────────┤ resolvedBy  │
│ date        │                              │  resolved   │
└─────────────┘                              └─────────────┘

Relationships:
• One Product belongs to One Supplier (1:1)
• One Product can have Many Transactions (1:N)
• One Product can have Many Alerts (1:N)
• One User can perform Many Transactions (1:N)
• One User can resolve Many Alerts (1:N)
```

## 7. Component Diagram

```
┌────────────────────────────────────────────────────────┐
│                  Application Layer                     │
│  ┌──────────────────────────────────────────────────┐ │
│  │            Main Application                       │ │
│  │         (com.inventory.Main)                      │ │
│  └──────────────────┬───────────────────────────────┘ │
└────────────────────┼────────────────────────────────────┘
                     │
┌────────────────────┼────────────────────────────────────┐
│                    ▼       Controller Layer             │
│  ┌──────────────────────────────────────────────────┐ │
│  │         InventoryController                       │ │
│  │  - Menu Navigation                                │ │
│  │  - Input Validation                               │ │
│  │  - Output Formatting                              │ │
│  └──────────────────┬───────────────────────────────┘ │
└────────────────────┼────────────────────────────────────┘
                     │
┌────────────────────┼────────────────────────────────────┐
│                    ▼       Service Layer                │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │
│  │Inventory │ │Supplier  │ │Transaction││  User    │ │
│  │ Service  │ │ Service  │ │ Service  │ │ Service  │ │
│  └─────┬────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘ │
│        │           │            │            │        │
│  ┌─────┴────┐ ┌────┴─────┐ ┌────┴─────┐              │
│  │ Alert    │ │ Report   │ │          │              │
│  │ Service  │ │ Service  │ │          │              │
│  └──────────┘ └──────────┘ └──────────┘              │
└────────────────────┼────────────────────────────────────┘
                     │
┌────────────────────┼────────────────────────────────────┐
│                    ▼         DAO Layer                  │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │
│  │ Product  │ │ Supplier │ │Transaction││  User    │ │
│  │   DAO    │ │   DAO    │ │   DAO    │ │   DAO    │ │
│  └─────┬────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘ │
│        │           │            │            │        │
│  ┌─────┴────┐                                │        │
│  │  Alert   │                                │        │
│  │   DAO    │                                │        │
│  └──────────┘                                │        │
└────────────────────┼────────────────────────┼─────────┘
                     │                        │
┌────────────────────┼────────────────────────┼─────────┐
│                    ▼         Utility Layer   ▼         │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ │
│  │  Logger  │ │Validator │ │FileHandler││IDGenerator│ │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘ │
└─────────────────────────────────────────────────────────┘
```

---

## Design Patterns Used:

1. **Singleton Pattern**: Service classes, DAO implementations
2. **DAO Pattern**: Data access abstraction
3. **MVC Pattern**: Controller-Service-DAO separation
4. **Factory Pattern**: ID generation
5. **Layered Architecture**: Clear separation of concerns

## Technology Stack:

- **Language**: Java 8+
- **Architecture**: Layered (4-tier)
- **Persistence**: File-based serialization
- **Interface**: Command Line Interface (CLI)
- **Logging**: Custom file-based logger
- **Security**: SHA-256 password hashing
