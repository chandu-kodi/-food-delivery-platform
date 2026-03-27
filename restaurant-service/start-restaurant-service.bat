@echo off
echo ========================================
echo Restaurant Service Setup with PostgreSQL
echo ========================================
echo.

echo Step 1: Starting PostgreSQL via Docker...
docker-compose up -d postgres
echo.

echo Step 2: Waiting for PostgreSQL to start...
timeout /t 15 /nobreak >nul
echo.

echo Step 3: Checking if PostgreSQL is running on port 5432...
netstat -an | findstr :5432
if %errorlevel% neq 0 (
    echo ❌ PostgreSQL is not running on port 5432
    echo Please check Docker installation and try again
    pause
    exit /b 1
) else (
    echo ✅ PostgreSQL is running on port 5432
)
echo.

echo Step 4: Building restaurant service...
mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Build failed
    pause
    exit /b 1
) else (
    echo ✅ Build successful
)
echo.

echo Step 5: Starting restaurant service...
echo The service will start on http://localhost:8060
echo Press Ctrl+C to stop the service
echo.

java -jar "target\restaurant-service-0.0.1-SNAPSHOT.jar"

echo.
echo Restaurant service stopped.
pause
