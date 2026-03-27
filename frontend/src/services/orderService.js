import api from './api';

const orderService = {
  createOrder: (orderData) => {
    return api.post('/api/orders', orderData);
  },

  getMyOrders: () => {
    return api.get('/api/orders');
  },

  getOrderById: (id) => {
    return api.get(`/api/orders/${id}`);
  },

  trackOrder: (id) => {
    return api.get(`/api/orders/${id}/track`);
  },

  cancelOrder: (id) => {
    return api.put(`/api/orders/${id}/cancel`);
  },

  updateOrderStatus: (id, status) => {
    return api.put(`/api/orders/${id}/status`, { status });
  },
};

export default orderService;
