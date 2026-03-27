@echo off
echo ========================================
echo Restaurant Service - Fixed Version
echo ========================================
echo.

echo ✅ ISSUE IDENTIFIED AND FIXED:
echo    - Duplicate controller mappings caused Spring initialization failure
echo    - Removed duplicate RestaurantController.java
echo    - PublicRestaurantController handles /api/restaurants
echo    - AdminRestaurantController handles /api/admin/restaurants
echo.

echo Step 1: Verify PostgreSQL is running...
netstat -an | findstr :5432 >nul
if %errorlevel% neq 0 (
    echo ❌ PostgreSQL not found, starting via Docker...
    cd "c:\food delivery platform"
    docker-compose up -d postgres
    echo Waiting for PostgreSQL...
    timeout /t 15 /nobreak >nul
) else (
    echo ✅ PostgreSQL is running on port 5432
)

echo.
echo Step 2: Build restaurant service...
cd "c:\food delivery platform\restaurant-service"
mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo ❌ Build failed
    pause
    exit /b 1
)

echo.
echo Step 3: Start Restaurant Service...
echo Service will be available at:
echo   - Public API: http://localhost:8060/api/restaurants
echo   - Admin API:  http://localhost:8060/api/admin/restaurants
echo   - Test:       http://localhost:8060/test
echo.

java -jar "target\restaurant-service-0.0.1-SNAPSHOT.jar"

echo.
echo Service stopped.
pause
