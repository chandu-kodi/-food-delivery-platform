import axios from 'axios';

const RESTAURANT_API_URL = process.env.REACT_APP_RESTAURANT_API_URL || 'http://localhost:8060';

const restaurantApi = axios.create({
  baseURL: RESTAURANT_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
const addAuthHeader = (config) => {
  const token = localStorage.getItem('adminToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
};

restaurantApi.interceptors.request.use(addAuthHeader, (error) => Promise.reject(error));

// Admin Restaurant API
export const adminRestaurantAPI = {
  // Restaurants
  getAllRestaurants: async () => {
    try {
      console.log('Making request to:', `${RESTAURANT_API_URL}/api/admin/restaurants`);
      const response = await restaurantApi.get('/api/admin/restaurants');
      console.log('API Response:', response);
      return response.data;
    } catch (error) {
      console.error('API Error Details:', {
        message: error.message,
        code: error.code,
        config: error.config,
        response: error.response?.data,
        status: error.response?.status
      });
      throw error;
    }
  },
  
  createRestaurant: async (restaurantData) => {
    const response = await restaurantApi.post('/api/admin/restaurants', restaurantData);
    return response.data;
  },
  
  updateRestaurant: async (id, restaurantData) => {
    const response = await restaurantApi.put(`/api/admin/restaurants/${id}`, restaurantData);
    return response.data;
  },
  
  deleteRestaurant: async (id) => {
    const response = await restaurantApi.delete(`/api/admin/restaurants/${id}`);
    return response.data;
  },
  
  getRestaurantById: async (id) => {
    const response = await restaurantApi.get(`/api/admin/restaurants/${id}`);
    return response.data;
  },

  // Menu Categories
  getMenuCategories: async (restaurantId) => {
    const response = await restaurantApi.get(`/api/admin/restaurants/${restaurantId}/categories`);
    return response.data;
  },
  
  createMenuCategory: async (restaurantId, categoryData) => {
    const response = await restaurantApi.post(`/api/admin/restaurants/${restaurantId}/categories`, categoryData);
    return response.data;
  },
  
  updateMenuCategory: async (restaurantId, categoryId, categoryData) => {
    const response = await restaurantApi.put(`/api/admin/restaurants/${restaurantId}/categories/${categoryId}`, categoryData);
    return response.data;
  },
  
  deleteMenuCategory: async (restaurantId, categoryId) => {
    const response = await restaurantApi.delete(`/api/admin/restaurants/${restaurantId}/categories/${categoryId}`);
    return response.data;
  },

  // Menu Items
  getMenuItems: async (restaurantId) => {
    const response = await restaurantApi.get(`/api/admin/restaurants/${restaurantId}/menu-items`);
    return response.data;
  },
  
  createMenuItem: async (restaurantId, menuItemData) => {
    const response = await restaurantApi.post(`/api/admin/restaurants/${restaurantId}/menu-items`, menuItemData);
    return response.data;
  },
  
  updateMenuItem: async (restaurantId, menuItemId, menuItemData) => {
    const response = await restaurantApi.put(`/api/admin/restaurants/${restaurantId}/menu-items/${menuItemId}`, menuItemData);
    return response.data;
  },
  
  deleteMenuItem: async (restaurantId, menuItemId) => {
    const response = await restaurantApi.delete(`/api/admin/restaurants/${restaurantId}/menu-items/${menuItemId}`);
    return response.data;
  },
};

export default restaurantApi;
