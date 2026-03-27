@echo off
echo Starting Restaurant Service with PostgreSQL...
echo.
echo Make sure PostgreSQL is running on localhost:5432
echo Database: ordersdb
echo User: postgres
echo Password: password
echo.

java -jar "target\restaurant-service-0.0.1-SNAPSHOT.jar"

echo.
echo Service stopped.
pause
