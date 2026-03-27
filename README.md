# Food Delivery Platform - Clean Project Structure

## ✅ Current Essential Files
- `PROJECT_SUMMARY.md` - Complete project overview and architecture
- `SETUP_GUIDE.md` - Setup instructions for developers  
- `RUN_INSTRUCTIONS.md` - How to run the application
- `init-db.sql` - Database initialization script
- `docker-compose.yml` - Docker setup for all services

## ✅ Service Directories
- `admin-frontend/` - Admin panel React app (Port 3001)
- `frontend/` - User frontend React app (Port 3000)
- `user-service/` - User authentication microservice (Port 8070)
- `restaurant-service/` - Restaurant management microservice (Port 8060)
- `order-service/` - Order processing microservice (Port 8080)
- `payment-service/` - Payment handling microservice (Port 8090)
- `delivery-service/` - Delivery tracking microservice (Port 8091)

## ⚠️ Note
There are still some temporary files remaining in the project directory. For a completely clean project, you can manually delete:
- All `.bat` files (temporary scripts)
- All `.html` files (debug/test files)
- All `.sql` files except `init-db.sql`
- All debug `.md` files except the essential ones listed above

## 🚀 Quick Start
1. **Setup Database**: `mysql -u root -p123456 < init-db.sql`
2. **Start Services**: `docker-compose up`
3. **Run Frontends**:
   - Admin Panel: `cd admin-frontend && npm start`
   - User App: `cd frontend && npm start`

## 📝 Architecture
This is a microservices-based food delivery platform with separate services for:
- User management & authentication
- Restaurant & menu management
- Order processing & tracking
- Payment handling (COD)
- Delivery tracking

Each service runs independently and communicates via REST APIs.
