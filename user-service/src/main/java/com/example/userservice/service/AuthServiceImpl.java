package com.example.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.AuthResponse;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        logger.info("Attempting login for username/email: {}", loginRequest.getUsername());
        
        try {
            // Find user by username or email
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseGet(() -> {
                        logger.debug("User not found by username, trying email: {}", loginRequest.getUsername());
                        return userRepository.findByEmail(loginRequest.getUsername())
                                .orElseThrow(() -> {
                                    logger.warn("User not found: {}", loginRequest.getUsername());
                                    return new RuntimeException("Invalid credentials");
                                });
                    });

            logger.debug("Found user: {}, checking password", user.getUsername());

            // Check password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                logger.warn("Password mismatch for user: {}", user.getUsername());
                throw new RuntimeException("Invalid credentials");
            }

            logger.info("Password validation successful for user: {}", user.getUsername());

            // Generate token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            logger.debug("Generated token for user: {}", user.getUsername());

            return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), 
                    user.getFirstName(), user.getLastName());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Login failed due to server error");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        logger.info("Attempting registration for username: {}, email: {}", registerRequest.getUsername(), registerRequest.getEmail());
        
        try {
            // Check if user already exists
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                logger.warn("Username already exists: {}", registerRequest.getUsername());
                throw new RuntimeException("Username already exists");
            }

            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                logger.warn("Email already exists: {}", registerRequest.getEmail());
                throw new RuntimeException("Email already exists");
            }

            // Create new user
            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .phone(registerRequest.getPhone())
                    .address(registerRequest.getAddress())
                    .build();

            logger.debug("Created user entity, saving to database");

            user = userRepository.save(user);
            logger.info("User saved successfully with ID: {}", user.getId());

            // Generate token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            logger.debug("Generated token for new user: {}", user.getUsername());

            return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), 
                    user.getFirstName(), user.getLastName());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during registration for user {}: {}", registerRequest.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Registration failed due to server error");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            return jwtUtil.validateToken(token);
        } catch (Exception e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Long extractUserIdFromToken(String token) {
        try {
            return jwtUtil.extractUserId(token);
        } catch (Exception e) {
            logger.error("Error extracting user ID from token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        }
    }
}
