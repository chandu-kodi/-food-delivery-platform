@echo off
echo ========================================
echo Restaurant Service Startup Script
echo ========================================
echo.

echo Step 1: Check PostgreSQL status...
netstat -an | findstr :5432 >nul
if %errorlevel% neq 0 (
    echo ❌ PostgreSQL is not running on port 5432
    echo Starting PostgreSQL via Docker...
    docker-compose up -d postgres
    echo Waiting for PostgreSQL to start...
    timeout /t 20 /nobreak >nul
) else (
    echo ✅ PostgreSQL is running on port 5432
)

echo.
echo Step 2: Build the application...
mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Build failed
    pause
    exit /b 1
)

echo.
echo Step 3: Start Restaurant Service...
echo Service will be available at: http://localhost:8060
echo Health check: http://localhost:8060/actuator/health
echo Test endpoint: http://localhost:8060/test
echo.
echo Press Ctrl+C to stop the service
echo.

java -jar "target\restaurant-service-0.0.1-SNAPSHOT.jar"

echo.
echo Restaurant service stopped.
pause
