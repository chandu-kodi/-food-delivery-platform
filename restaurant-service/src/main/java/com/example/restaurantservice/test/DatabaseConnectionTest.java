package com.example.restaurantservice.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ordersdb";
        String username = "postgres";
        String password = "password";
        
        System.out.println("Testing PostgreSQL connection...");
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver loaded successfully");
            
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful!");
            
            connection.close();
            System.out.println("Connection closed successfully");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
        }
    }
}
