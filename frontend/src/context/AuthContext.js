import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const userInfo = localStorage.getItem('userInfo');
    
    if (token && userInfo) {
      try {
        const user = JSON.parse(userInfo);
        setUser(user);
        setLoading(false);
      } catch (error) {
        console.error('Error parsing user info:', error);
        localStorage.removeItem('authToken');
        localStorage.removeItem('userInfo');
        setLoading(false);
      }
    } else {
      setLoading(false);
    }
  }, []);

  const validateToken = async (token) => {
    try {
      const isValid = await authAPI.validateToken(token);
      if (isValid) {
        // For now, we'll store minimal user info
        // You can enhance this with a getUserInfo endpoint
        setUser({ token });
      } else {
        localStorage.removeItem('authToken');
      }
    } catch (error) {
      console.error('Token validation failed:', error);
      localStorage.removeItem('authToken');
    } finally {
      setLoading(false);
    }
  };

  const login = async (username, password) => {
    try {
      setError(null);
      const response = await authAPI.login({ username, password });
      
      // Store complete user info in localStorage
      const userInfo = {
        token: response.token,
        userId: response.userId,
        username: response.username,
        email: response.email,
        firstName: response.firstName,
        lastName: response.lastName,
      };
      
      localStorage.setItem('authToken', response.token);
      localStorage.setItem('userInfo', JSON.stringify(userInfo));
      setUser(userInfo);

      return response;
    } catch (err) {
      const message = err.response?.data?.message || 'Login failed';
      setError(message);
      throw err;
    }
  };

  const register = async (userData) => {
    try {
      setError(null);
      const response = await authAPI.register(userData);
      
      // Don't auto-login after registration, just return success
      // User will need to login separately
      return response;
    } catch (err) {
      const message = err.response?.data?.message || 'Registration failed';
      setError(message);
      throw err;
    }
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userInfo');
    setUser(null);
    setError(null);
  };

  const value = {
    user,
    loading,
    error,
    login,
    register,
    logout,
    isAuthenticated: !!user,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthContext;
