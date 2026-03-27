@echo off
echo PostgreSQL Setup for Restaurant Service
echo =====================================
echo.

echo Checking for PostgreSQL services...
sc query | findstr /i postgres
echo.

echo If you see PostgreSQL service above, start it with:
echo net start postgresql-x64-14
echo (or whatever version number appears)
echo.

echo If no PostgreSQL service found, you need to:
echo 1. Install PostgreSQL from https://www.postgresql.org/download/windows/
echo 2. Create a database named "ordersdb"
echo 3. Create a user "postgres" with password "password"
echo 4. Start the PostgreSQL service
echo.

echo After PostgreSQL is running, test the connection:
echo run-with-postgresql.bat
echo.

pause
