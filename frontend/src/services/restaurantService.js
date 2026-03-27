import api from './api';

const restaurantService = {
  getAllRestaurants: () => {
    return api.get('/api/restaurants');
  },

  getRestaurantById: (id) => {
    return api.get(`/api/restaurants/${id}`);
  },

  getRestaurantMenu: (restaurantId) => {
    return api.get(`/api/restaurants/${restaurantId}/menu`);
  },

  searchRestaurants: (query) => {
    return api.get(`/api/restaurants/search?q=${query}`);
  },

  getRestaurantsByCategory: (category) => {
    return api.get(`/api/restaurants/category/${category}`);
  },
};

export default restaurantService;
