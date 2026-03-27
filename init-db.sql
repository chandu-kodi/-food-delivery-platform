-- Create additional databases for payment and delivery services
CREATE DATABASE paymentsdb;
CREATE DATABASE deliverydb;

-- Grant permissions to postgres user
GRANT ALL PRIVILEGES ON DATABASE paymentsdb TO postgres;
GRANT ALL PRIVILEGES ON DATABASE deliverydb TO postgres;

-- Create tables in ordersdb
\c ordersdb;

-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Restaurants table
CREATE TABLE restaurants (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    cuisine_type VARCHAR(50) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    rating DECIMAL(3,2) DEFAULT 0.0,
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu categories table
CREATE TABLE menu_categories (
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER REFERENCES restaurants(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Menu items table
CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER REFERENCES restaurants(id) ON DELETE CASCADE,
    category_id INTEGER REFERENCES menu_categories(id) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    is_vegetarian BOOLEAN DEFAULT FALSE,
    preparation_time INTEGER DEFAULT 15, -- in minutes
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Update orders table to include user_id
ALTER TABLE orders ADD COLUMN IF NOT EXISTS user_id INTEGER REFERENCES users(id);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS restaurant_id INTEGER REFERENCES restaurants(id);
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PENDING';

-- Insert sample data
INSERT INTO restaurants (name, description, cuisine_type, address, phone, email, rating) VALUES
('Pizza Palace', 'Authentic Italian pizza with fresh ingredients', 'Italian', '123 Main St, Downtown', '555-0101', 'info@pizzapalace.com', 4.5),
('Burger Barn', 'Gourmet burgers and fries', 'American', '456 Oak Ave, Midtown', '555-0102', 'hello@burgerbarn.com', 4.2),
('Sushi Station', 'Fresh Japanese sushi and rolls', 'Japanese', '789 Pine Rd, Uptown', '555-0103', 'contact@sushistation.com', 4.8),
('Taco Town', 'Mexican street food and tacos', 'Mexican', '321 Elm St, Westside', '555-0104', 'info@tacotown.com', 4.3);

INSERT INTO menu_categories (restaurant_id, name, description, display_order) VALUES
(1, 'Pizza', 'Our famous pizzas', 1),
(1, 'Appetizers', 'Start your meal right', 2),
(2, 'Burgers', 'Signature burgers', 1),
(2, 'Sides', 'Perfect accompaniments', 2),
(3, 'Sushi Rolls', 'Fresh sushi rolls', 1),
(3, 'Sashimi', 'Premium sashimi', 2),
(4, 'Tacos', 'Traditional tacos', 1),
(4, 'Burritos', 'Large burritos', 2);

INSERT INTO menu_items (restaurant_id, category_id, name, description, price, is_vegetarian, preparation_time) VALUES
(1, 1, 'Margherita Pizza', 'Fresh tomatoes, mozzarella, basil', 12.99, TRUE, 20),
(1, 1, 'Pepperoni Pizza', 'Classic pepperoni with mozzarella', 14.99, FALSE, 20),
(1, 2, 'Garlic Bread', 'Toasted bread with garlic butter', 6.99, TRUE, 10),
(2, 1, 'Classic Burger', 'Beef patty, lettuce, tomato, onion', 10.99, FALSE, 15),
(2, 1, 'Cheese Burger', 'Classic burger with cheddar cheese', 12.99, FALSE, 15),
(2, 2, 'French Fries', 'Crispy golden fries', 4.99, TRUE, 10),
(3, 1, 'California Roll', 'Crab, avocado, cucumber', 8.99, FALSE, 15),
(3, 1, 'Spicy Tuna Roll', 'Tuna, spicy mayo, cucumber', 10.99, FALSE, 15),
(4, 1, 'Beef Taco', 'Seasoned beef, lettuce, cheese', 3.99, FALSE, 10),
(4, 1, 'Chicken Taco', 'Grilled chicken, salsa, avocado', 4.99, FALSE, 10);

\c postgres;
-- Return to default database
