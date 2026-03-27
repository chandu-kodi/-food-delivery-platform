# Food Delivery Platform - Complete Setup Guide

## 🚀 Overview
A complete microservices-based food delivery platform with user authentication, restaurant browsing, and order management.

## 📋 Services Architecture
- **User Service** (Port 8070): Authentication and user management
- **Restaurant Service** (Port 8060): Restaurant and menu management  
- **Order Service** (Port 8080): Order processing
- **Payment Service** (Port 8090): Payment handling
- **Delivery Service** (Port 8091): Delivery tracking
- **Frontend** (Port 3000): React application

## 🛠️ Prerequisites
- Docker and Docker Compose
- Node.js 16+ and npm
- Maven 3.6+
- Java 17+

## 🗄️ Database Setup
The database is automatically initialized with:
- Users table for authentication
- Restaurants table with sample data
- Menu items and categories
- Orders, payments, and delivery tables

## 🚀 Quick Start

### Option 1: Docker Compose (Recommended)
```bash
# Start all backend services
docker-compose up -d

# Start frontend (in separate terminal)
cd frontend
npm install
npm start
```

### Option 2: Manual Development Setup
```bash
# Terminal 1 - User Service
cd user-service
mvn spring-boot:run

# Terminal 2 - Restaurant Service  
cd restaurant-service
mvn spring-boot:run

# Terminal 3 - Order Service
cd order-service
mvn spring-boot:run

# Terminal 4 - Payment Service
cd payment-service
mvn spring-boot:run

# Terminal 5 - Delivery Service
cd delivery-service
mvn spring-boot:run

# Terminal 6 - Frontend
cd frontend
npm install
npm start
```

## 🌐 Access Points
- **Frontend**: http://localhost:3000
- **User Service**: http://localhost:8070
- **Restaurant Service**: http://localhost:8060
- **Order Service**: http://localhost:8080
- **Payment Service**: http://localhost:8090
- **Delivery Service**: http://localhost:8091

## 📱 Application Flow

### 1. Authentication
- Users can register or login
- JWT tokens are generated and stored
- Protected routes require authentication

### 2. Dashboard
After login, users see:
- **Restaurants Tab**: Browse and search restaurants
- **My Orders Tab**: View order history and status

### 3. Restaurant Browsing
- View all available restaurants
- Filter by cuisine type
- Search by name or cuisine
- View restaurant details and ratings

### 4. Order Flow (Extended from previous version)
- Select items from restaurant menu
- Process payment
- Track delivery status

## 🔑 Sample Users
The system comes with sample restaurants but no pre-created users. You need to register a new account.

## 🍽️ Sample Restaurants
- **Pizza Palace** - Italian cuisine
- **Burger Barn** - American cuisine  
- **Sushi Station** - Japanese cuisine
- **Taco Town** - Mexican cuisine

## 🛡️ Security Features
- JWT-based authentication
- Password hashing with BCrypt
- CORS configuration for cross-origin requests
- Input validation on all endpoints

## 📊 API Endpoints

### Authentication (Port 8070)
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/validate
```

### Restaurants (Port 8060)
```
GET /api/restaurants
GET /api/restaurants/{id}
GET /api/restaurants/{id}/menu
GET /api/restaurants/search?cuisine={type}
```

### Orders (Port 8080)
```
POST /api/orders
GET /api/orders/{id}
```

### Payments (Port 8090)
```
POST /api/payments
GET /api/payments/{id}
```

### Delivery (Port 8091)
```
GET /api/deliveries/{id}
GET /api/deliveries/order/{orderId}
```

## 🔧 Troubleshooting

### Database Issues
```bash
# Reset database
docker-compose down -v
docker-compose up -d
```

### Port Conflicts
Ensure ports 3000, 8060-8091 are available.

### Frontend Issues
```bash
# Clear node modules and reinstall
cd frontend
rm -rf node_modules package-lock.json
npm install
npm start
```

## 🎯 Next Steps
- Implement restaurant menu ordering
- Add real-time order tracking
- Implement user profiles
- Add restaurant reviews
- Implement admin dashboard

## 📝 Notes
- All services compile successfully
- Frontend is fully responsive
- Database is pre-populated with sample data
- Authentication flow is complete
- Restaurant browsing is functional
