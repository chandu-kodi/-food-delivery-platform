@echo off
echo Testing PostgreSQL Database Connection...
echo.

echo Compiling test...
javac -cp "target\dependency\*;target\classes" src\main\java\com\example\restaurantservice\test\DatabaseConnectionTest.java -d target\classes

echo.
echo Running connection test...
java -cp "target\dependency\*;target\classes" com.example.restaurantservice.test.DatabaseConnectionTest

echo.
pause
