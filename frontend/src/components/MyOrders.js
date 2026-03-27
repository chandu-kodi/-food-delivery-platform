import React, { useState, useEffect } from 'react';
import { orderAPI, deliveryAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';

const MyOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [trackingOrder, setTrackingOrder] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    fetchOrders();
  }, [user]);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      if (!user || !user.userId) {
        setError('User not authenticated');
        return;
      }
      
      // Fetch actual user orders
      const userOrders = await orderAPI.getUserOrders(user.userId);
      setOrders(userOrders);
    } catch (err) {
      setError('Failed to fetch orders');
      console.error('Error fetching orders:', err);
    } finally {
      setLoading(false);
    }
  };

  const trackOrder = async (orderId) => {
    try {
      const orderDetails = await orderAPI.getOrderById(orderId);
      setTrackingOrder(orderDetails);
    } catch (err) {
      console.error('Error tracking order:', err);
      setError('Failed to track order');
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'CONFIRMED':
        return 'bg-blue-100 text-blue-800';
      case 'PREPARING':
        return 'bg-purple-100 text-purple-800';
      case 'READY_FOR_PICKUP':
        return 'bg-indigo-100 text-indigo-800';
      case 'IN_TRANSIT':
        return 'bg-orange-100 text-orange-800';
      case 'DELIVERED':
        return 'bg-green-100 text-green-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
        {error}
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="text-center py-12">
        <div className="text-gray-400 text-6xl mb-4">🍽️</div>
        <h3 className="text-lg font-medium text-gray-900 mb-2">No orders yet</h3>
        <p className="text-gray-500">Start ordering from your favorite restaurants!</p>
        <button className="mt-4 bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md">
          Browse Restaurants
        </button>
      </div>
    );
  }

  return (
    <div>
      <h2 className="text-2xl font-bold text-gray-900 mb-6">My Orders</h2>
      
      <div className="space-y-4">
        {orders.map(order => (
          <OrderCard 
            key={order.id} 
            order={order} 
            trackOrder={trackOrder}
            formatDate={formatDate}
            getStatusColor={getStatusColor}
          />
        ))}
      </div>

      {/* Tracking Modal */}
      {trackingOrder && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-semibold mb-4">Order Tracking</h3>
            <div className="space-y-3">
              <p><strong>Order #:</strong> {trackingOrder.orderNumber}</p>
              <p><strong>Status:</strong> 
                <span className={`ml-2 px-2 py-1 rounded text-xs ${getStatusColor(trackingOrder.status)}`}>
                  {trackingOrder.status.replace('_', ' ')}
                </span>
              </p>
              <p><strong>Restaurant:</strong> {trackingOrder.restaurantName}</p>
              <p><strong>Total:</strong> ${trackingOrder.totalAmount?.toFixed(2)}</p>
              <p><strong>Delivery Address:</strong> {trackingOrder.deliveryAddress}</p>
              <p><strong>Phone:</strong> {trackingOrder.phoneNumber}</p>
              <p><strong>Estimated Delivery:</strong> {trackingOrder.estimatedDeliveryTime ? 
                new Date(trackingOrder.estimatedDeliveryTime).toLocaleString() : 'Calculating...'}</p>
            </div>
            <button 
              onClick={() => setTrackingOrder(null)}
              className="mt-4 w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

const OrderCard = ({ order, trackOrder, formatDate, getStatusColor }) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden">
      <div className="p-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h3 className="text-lg font-semibold text-gray-900">Order #{order.id}</h3>
            <p className="text-gray-600">{order.restaurantName}</p>
            <p className="text-sm text-gray-500">{formatDate(order.createdAt)}</p>
          </div>
          <div className="text-right">
            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-medium ${getStatusColor(order.status)}`}>
              {order.status.replace('_', ' ')}
            </span>
            <p className="text-lg font-bold text-gray-900 mt-2">${order.totalAmount.toFixed(2)}</p>
          </div>
        </div>

        <div className="border-t pt-4">
          <h4 className="text-sm font-medium text-gray-900 mb-2">Order Items:</h4>
          <div className="space-y-2">
            {order.orderItems && order.orderItems.map((item, index) => (
              <div key={index} className="flex justify-between text-sm">
                <span className="text-gray-700">
                  {item.quantity}x {item.menuItemName}
                </span>
                <span className="text-gray-900">${(item.unitPrice * item.quantity).toFixed(2)}</span>
              </div>
            ))}
            {(!order.orderItems || order.orderItems.length === 0) && (
              <div className="text-sm text-gray-500">No items found</div>
            )}
          </div>
        </div>

        <div className="mt-4 flex space-x-3">
          <button 
            onClick={() => trackOrder(order.id)}
            className="flex-1 bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md text-sm"
          >
            Track Order
          </button>
          <button className="flex-1 bg-gray-500 hover:bg-gray-600 text-white font-medium py-2 px-4 rounded-md text-sm">
            Reorder
          </button>
        </div>
      </div>
    </div>
  );
};

export default MyOrders;
