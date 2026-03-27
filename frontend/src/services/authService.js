import api from './api';

const authService = {
  login: (email, password) => {
    return api.post('/api/auth/login', { email, password });
  },

  register: (userData) => {
    return api.post('/api/auth/register', userData);
  },

  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  setCurrentUser: (user) => {
    localStorage.setItem('user', JSON.stringify(user));
  },

  getToken: () => {
    return localStorage.getItem('authToken');
  },

  setToken: (token) => {
    localStorage.setItem('authToken', token);
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },
};

export default authService;
