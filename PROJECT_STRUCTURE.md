# Food Delivery Platform - Project Structure

## ✅ Essential Files (Required for Application)
- `PROJECT_SUMMARY.md` - Complete project overview and architecture
- `SETUP_GUIDE.md` - Setup instructions for developers
- `RUN_INSTRUCTIONS.md` - How to run the application
- `init-db.sql` - Database initialization script
- `docker-compose.yml` - Docker setup for all services

## ✅ Service Directories (Required)
- `admin-frontend/` - Admin panel React app (Port 3001)
- `frontend/` - User frontend React app (Port 3000)
- `user-service/` - User authentication microservice (Port 8070)
- `restaurant-service/` - Restaurant management microservice (Port 8060)
- `order-service/` - Order processing microservice (Port 8080)
- `payment-service/` - Payment handling microservice (Port 8090)
- `delivery-service/` - Delivery tracking microservice (Port 8091)

## ❌ Files to Remove (Not Required)
All `.bat`, `.html`, temporary `.sql` files, and debug documentation files are unnecessary for running the application.

## 🚀 How to Run
1. Setup database: `mysql -u root -p123456 < init-db.sql`
2. Start services: `docker-compose up`
3. Run frontends: 
   - Admin: `cd admin-frontend && npm start`
   - User: `cd frontend && npm start`

## 📝 Clean Structure Status
The project has been cleaned to contain only essential files for running the application.
