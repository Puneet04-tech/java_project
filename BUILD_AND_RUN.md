# Build and Run Instructions

## Prerequisites
- Java Development Kit (JDK) 8 or higher installed
- Windows PowerShell or Command Prompt

## Compilation Steps

### Option 1: Compile All at Once
```powershell
# Navigate to project directory
cd c:\Users\rupes\OneDrive\Desktop\java_project

# Create output directory
New-Item -ItemType Directory -Force -Path bin

# Compile all Java files
javac -d bin -sourcepath src src\com\inventory\Main.java
```

### Option 2: Compile with Classpath
```powershell
# Compile all source files
javac -cp src -d bin src\com\inventory\*.java src\com\inventory\model\*.java src\com\inventory\dao\*.java src\com\inventory\dao\impl\*.java src\com\inventory\service\*.java src\com\inventory\controller\*.java src\com\inventory\util\*.java src\com\inventory\exception\*.java
```

## Running the Application

### From compiled classes:
```powershell
# Run from project directory
java -cp bin com.inventory.Main
```

### Alternative: Compile and Run in One Command
```powershell
# Navigate to src directory
cd src

# Compile and run
javac com\inventory\Main.java ; java com.inventory.Main
```

## Default Login Credentials
- **Username**: admin
- **Password**: admin123

## Directory Structure After Compilation
```
java_project/
├── bin/                    # Compiled .class files
├── src/                    # Source code
├── data/                   # Auto-created data storage
├── logs/                   # Auto-created application logs
├── reports/                # Auto-created report files
└── docs/                   # Documentation
```

## Troubleshooting

### If compilation fails:
1. Verify JDK installation: `java -version`
2. Check JAVA_HOME environment variable
3. Ensure all source files are present
4. Check for syntax errors in the output

### If runtime errors occur:
1. Check logs in `logs/application.log`
2. Ensure proper permissions for data directory
3. Verify all dependencies are compiled

## Creating Data Directories
The application will auto-create required directories, but you can create them manually:
```powershell
New-Item -ItemType Directory -Force -Path data
New-Item -ItemType Directory -Force -Path logs
New-Item -ItemType Directory -Force -Path reports
```

## Testing the Application

1. **Login**: Use default credentials
2. **Add a Supplier**: Navigate to Supplier Management
3. **Add Products**: Navigate to Inventory Management
4. **Record Purchase**: To add initial stock
5. **Record Sale**: To test transaction processing
6. **Generate Reports**: View inventory status
7. **Check Alerts**: See low stock notifications

## Notes
- All data is stored in the `data/` directory
- Logs are written to `logs/application.log`
- Reports are saved in `reports/` directory
- First run creates default admin user
