@echo off
echo ========================================
echo Restaurant Service - Final Fix
echo ========================================
echo.

echo ✅ ISSUE: Controller mapping conflicts
echo ✅ FIX: Removed duplicate RestaurantController
echo ✅ NOW: Only PublicRestaurantController and AdminRestaurantController
echo.

echo Step 1: Check PostgreSQL...
netstat -an | findstr :5432 >nul
if %errorlevel% neq 0 (
    echo Starting PostgreSQL...
    cd "c:\food delivery platform"
    docker-compose up -d postgres
    timeout /t 15 /nobreak >nul
) else (
    echo ✅ PostgreSQL running
)

echo.
echo Step 2: Build restaurant service...
cd "c:\food delivery platform\restaurant-service"
call mvn clean package -DskipTests

echo.
echo Step 3: Start Restaurant Service...
echo.
echo ========================================
echo SERVICE ENDPOINTS:
echo   Public:  http://localhost:8060/api/restaurants
echo   Admin:   http://localhost:8060/api/admin/restaurants
echo   Test:    http://localhost:8060/test
echo ========================================
echo.

java -jar "target\restaurant-service-0.0.1-SNAPSHOT.jar"

echo.
echo Service stopped.
pause
