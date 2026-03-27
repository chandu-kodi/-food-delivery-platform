import axios from 'axios';

const ORDER_API_URL = process.env.REACT_APP_ORDER_API_URL || 'http://localhost:8080';
const PAYMENT_API_URL = process.env.REACT_APP_PAYMENT_API_URL || 'http://localhost:8090';
const DELIVERY_API_URL = process.env.REACT_APP_DELIVERY_API_URL || 'http://localhost:8091';
const USER_API_URL = process.env.REACT_APP_USER_API_URL || 'http://localhost:8071';
const RESTAURANT_API_URL = process.env.REACT_APP_RESTAURANT_API_URL || 'http://localhost:8060';

const orderApi = axios.create({
  baseURL: ORDER_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const paymentApi = axios.create({
  baseURL: PAYMENT_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const deliveryApi = axios.create({
  baseURL: DELIVERY_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const userApi = axios.create({
  baseURL: USER_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const restaurantApi = axios.create({
  baseURL: RESTAURANT_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
const addAuthHeader = (config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

orderApi.interceptors.request.use(addAuthHeader, (error) => Promise.reject(error));
paymentApi.interceptors.request.use(addAuthHeader, (error) => Promise.reject(error));
deliveryApi.interceptors.request.use(addAuthHeader, (error) => Promise.reject(error));

// Auth API
export const authAPI = {
  login: async (credentials) => {
    const response = await userApi.post('/api/auth/login', credentials);
    return response.data;
  },
  
  register: async (userData) => {
    const response = await userApi.post('/api/auth/register', userData);
    return response.data;
  },
  
  validateToken: async (token) => {
    const response = await userApi.post('/api/auth/validate', null, { 
      params: { token } 
    });
    return response.data;
  },
};

// Restaurant API
export const restaurantAPI = {
  getAllRestaurants: async () => {
    const response = await restaurantApi.get('/api/restaurants');
    return response.data;
  },
  
  getRestaurantById: async (id) => {
    const response = await restaurantApi.get(`/api/restaurants/${id}`);
    return response.data;
  },
  
  getRestaurantMenu: async (restaurantId) => {
    const response = await restaurantApi.get(`/api/restaurants/${restaurantId}/menu`);
    return response.data;
  },
  
  searchRestaurants: async (cuisine) => {
    const response = await restaurantApi.get('/api/restaurants/search', {
      params: { cuisine }
    });
    return response.data;
  },
};

// Order API
export const orderAPI = {
  createOrder: async (orderData) => {
    const response = await orderApi.post('/api/orders', orderData);
    return response.data;
  },
  
  getOrder: async (orderId) => {
    const response = await orderApi.get(`/api/orders/${orderId}`);
    return response.data;
  },
  
  getUserOrders: async (userId) => {
    const response = await orderApi.get(`/api/orders/user/${userId}`);
    return response.data;
  },
};

// Payment API
export const paymentAPI = {
  createPayment: async (paymentData) => {
    const response = await paymentApi.post('/api/payments', paymentData);
    return response.data;
  },
  
  getPayment: async (paymentId) => {
    const response = await paymentApi.get(`/api/payments/${paymentId}`);
    return response.data;
  },
};

// Delivery API
export const deliveryAPI = {
  getDelivery: async (deliveryId) => {
    const response = await deliveryApi.get(`/api/deliveries/${deliveryId}`);
    return response.data;
  },
  
  getDeliveryByOrder: async (orderId) => {
    const response = await deliveryApi.get(`/api/deliveries/order/${orderId}`);
    return response.data;
  },
};

export default orderApi;
