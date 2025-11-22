# Smart Inventory Management System - Setup Script

Write-Host ""
Write-Host "Smart Inventory Management System - Project Setup" -ForegroundColor Cyan
Write-Host "---------------------------------------------------" -ForegroundColor Cyan
Write-Host ""

Set-Location -Path "c:\Users\rupes\OneDrive\Desktop\java_project"

Write-Host "[1/6] Creating directory structure..." -ForegroundColor Yellow
New-Item -ItemType Directory -Force -Path "bin" | Out-Null
New-Item -ItemType Directory -Force -Path "data" | Out-Null
New-Item -ItemType Directory -Force -Path "logs" | Out-Null
New-Item -ItemType Directory -Force -Path "reports" | Out-Null
Write-Host "  [OK] Directories created" -ForegroundColor Green
Write-Host ""

Write-Host "[2/6] Checking Java installation..." -ForegroundColor Yellow
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if ($javaCheck) {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "  [OK] Java is installed: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "  [FAIL] Java not found. Please install JDK 8+" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "[3/6] Compiling Java source files..." -ForegroundColor Yellow
javac -d bin -sourcepath src src\com\inventory\Main.java 2>&1 | Out-Null
if ($LASTEXITCODE -eq 0) {
    Write-Host "  [OK] Compilation successful" -ForegroundColor Green
} else {
    Write-Host "  [FAIL] Compilation failed" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "[4/6] Verifying compiled classes..." -ForegroundColor Yellow
if (Test-Path "bin\com\inventory\Main.class") {
    Write-Host "  [OK] Main.class found" -ForegroundColor Green
} else {
    Write-Host "  [FAIL] Main.class not found" -ForegroundColor Red
    exit 1
}
Write-Host ""

Write-Host "[5/6] Project structure:" -ForegroundColor Yellow
Write-Host "  src/       - Source code (25+ files)" -ForegroundColor Gray
Write-Host "  bin/       - Compiled classes" -ForegroundColor Gray
Write-Host "  data/      - Data storage" -ForegroundColor Gray
Write-Host "  logs/      - Application logs" -ForegroundColor Gray
Write-Host "  reports/   - Generated reports" -ForegroundColor Gray
Write-Host ""

Write-Host "[6/6] Getting file counts..." -ForegroundColor Yellow
$javaFiles = (Get-ChildItem -Path "src" -Filter "*.java" -Recurse).Count
$docFiles = (Get-ChildItem -Path "." -Filter "*.md").Count
Write-Host "  [OK] Java files: $javaFiles" -ForegroundColor Green
Write-Host "  [OK] Documentation files: $docFiles" -ForegroundColor Green
Write-Host ""

Write-Host "---------------------------------------------------" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Green
Write-Host "---------------------------------------------------" -ForegroundColor Cyan
Write-Host ""
Write-Host "To run:" -ForegroundColor Yellow
Write-Host "  java -cp bin com.inventory.Main" -ForegroundColor Cyan
Write-Host ""
Write-Host "Default login: admin / admin123" -ForegroundColor White
Write-Host ""
Write-Host "Documentation: README.md, QUICK_REFERENCE.md" -ForegroundColor Gray
Write-Host ""
