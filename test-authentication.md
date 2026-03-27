# Authentication API Test Script

## Test the User Service Authentication

### 1. Health Check
```bash
curl -X GET http://localhost:8070/api/health
```

### 2. Register a New User
```bash
curl -X POST http://localhost:8070/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User",
    "phone": "1234567890",
    "address": "123 Test Street"
  }'
```

### 3. Login with the Created User
```bash
curl -X POST http://localhost:8070/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 4. Validate Token (replace TOKEN with the token from login response)
```bash
curl -X POST "http://localhost:8070/api/auth/validate?token=YOUR_JWT_TOKEN"
```

### 5. Get User ID from Token (replace TOKEN with the token from login response)
```bash
curl -X GET "http://localhost:8070/api/auth/user-id?token=YOUR_JWT_TOKEN"
```

## Common Issues and Solutions

### Issue 1: "Connection refused" or "Service unavailable"
**Solution**: Make sure the PostgreSQL database is running on localhost:5432 and the user-service is started.

### Issue 2: "Invalid credentials"
**Solution**: Check that the user exists in the database and the password is correct.

### Issue 3: "Username already exists"
**Solution**: Use a different username or delete the existing user from the database.

### Issue 4: Database connection errors
**Solution**: Verify PostgreSQL is running and the connection details in application.properties are correct.

## Database Setup

If you don't have PostgreSQL running, you can start it with Docker:
```bash
docker run -d --name postgres-food-delivery \
  -e POSTGRES_DB=ordersdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  postgres:15-alpine
```

## Starting the User Service

```bash
cd user-service
mvn spring-boot:run
```

The service will be available at http://localhost:8070
