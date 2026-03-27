import React, { createContext, useContext, useState, useEffect } from 'react';
import { adminRestaurantAPI } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('adminToken');
    if (token) {
      // Validate token and set user
      setUser({ username: 'admin', role: 'ADMIN' });
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      // Simple admin login - in production, this should call an actual API
      if (username === 'admin' && password === 'admin123') {
        const token = 'admin-token-' + Date.now();
        localStorage.setItem('adminToken', token);
        setUser({ username: 'admin', role: 'ADMIN' });
        return true;
      }
      throw new Error('Invalid credentials');
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('adminToken');
    setUser(null);
  };

  const value = {
    user,
    login,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
