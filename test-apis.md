# API Testing Guide

## Service Ports
- Order Service: http://localhost:8080
- Payment Service: http://localhost:8090  
- Delivery Service: http://localhost:8091

## Test Endpoints

### Order Service
1. **Create Order**
   ```bash
   POST http://localhost:8080/api/orders
   Content-Type: application/json
   
   {
     "customerName": "John Doe",
     "customerEmail": "john@example.com",
     "items": [
       {
         "name": "Pizza",
         "quantity": 2,
         "price": 15.99
       }
     ],
     "deliveryAddress": "123 Main St, City, State",
     "totalAmount": 31.98
   }
   ```

2. **Get Order**
   ```bash
   GET http://localhost:8080/api/orders/{id}
   ```

### Payment Service
1. **Create Payment**
   ```bash
   POST http://localhost:8090/api/payments
   Content-Type: application/json
   
   {
     "orderId": 1,
     "amount": 31.98,
     "paymentMethod": "CREDIT_CARD",
     "cardNumber": "4111111111111111",
     "cardHolder": "John Doe",
     "expiryDate": "12/25",
     "cvv": "123"
   }
   ```

2. **Get Payment**
   ```bash
   GET http://localhost:8090/api/payments/{id}
   ```

### Delivery Service
1. **Get Delivery by ID**
   ```bash
   GET http://localhost:8091/api/deliveries/{id}
   ```

2. **Get Delivery by Order ID**
   ```bash
   GET http://localhost:8091/api/deliveries/order/{orderId}
   ```

## Running Services

### Using Docker Compose
```bash
docker-compose up -d
```

### Using Maven (for development)
```bash
# Order Service
cd order-service && mvn spring-boot:run

# Payment Service  
cd payment-service && mvn spring-boot:run

# Delivery Service
cd delivery-service && mvn spring-boot:run
```

## Frontend
```bash
cd frontend
npm start
```

The frontend will be available at http://localhost:3000 and will automatically connect to the backend services.
