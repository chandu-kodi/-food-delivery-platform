import React, { useState, useEffect } from 'react';
import { deliveryAPI } from '../services/api';

const DeliveryTracker = ({ orderId }) => {
  const [delivery, setDelivery] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    if (orderId) {
      fetchDelivery();
    }
  }, [orderId]);

  const fetchDelivery = async () => {
    try {
      setLoading(true);
      const deliveryData = await deliveryAPI.getDeliveryByOrder(orderId);
      setDelivery(deliveryData);
    } catch (err) {
      setError('Failed to fetch delivery information');
      console.error('Delivery fetch error:', err);
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'ASSIGNED':
        return 'bg-blue-100 text-blue-800';
      case 'PICKED_UP':
        return 'bg-yellow-100 text-yellow-800';
      case 'IN_TRANSIT':
        return 'bg-orange-100 text-orange-800';
      case 'DELIVERED':
        return 'bg-green-100 text-green-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getProgressPercentage = (status) => {
    switch (status) {
      case 'ASSIGNED':
        return 25;
      case 'PICKED_UP':
        return 50;
      case 'IN_TRANSIT':
        return 75;
      case 'DELIVERED':
        return 100;
      default:
        return 0;
    }
  };

  if (loading) {
    return (
      <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <div className="animate-pulse">
          <div className="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
          <div className="h-4 bg-gray-200 rounded w-1/2 mb-2"></div>
          <div className="h-4 bg-gray-200 rounded w-3/4"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <div className="p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      </div>
    );
  }

  if (!delivery) {
    return (
      <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
        <h2 className="text-2xl font-bold mb-6 text-gray-800">Delivery Tracking</h2>
        <div className="text-center py-8 text-gray-500">
          <p>No delivery information available for this order.</p>
          <p className="text-sm mt-2">Please complete payment first to assign delivery partner.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">Delivery Tracking</h2>
      
      <div className="mb-6">
        <div className="flex justify-between items-center mb-2">
          <span className="text-sm font-medium text-gray-600">Delivery ID</span>
          <span className="text-sm font-bold">#{delivery.id}</span>
        </div>
        <div className="flex justify-between items-center mb-2">
          <span className="text-sm font-medium text-gray-600">Order ID</span>
          <span className="text-sm font-bold">#{delivery.orderId}</span>
        </div>
        <div className="flex justify-between items-center mb-2">
          <span className="text-sm font-medium text-gray-600">Delivery Partner</span>
          <span className="text-sm font-bold">{delivery.partnerId}</span>
        </div>
        <div className="flex justify-between items-center mb-4">
          <span className="text-sm font-medium text-gray-600">Status</span>
          <span className={`px-3 py-1 rounded-full text-xs font-semibold ${getStatusColor(delivery.status)}`}>
            {delivery.status.replace('_', ' ')}
          </span>
        </div>

        {/* Progress Bar */}
        <div className="relative">
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div 
              className="bg-blue-500 h-2 rounded-full transition-all duration-300"
              style={{ width: `${getProgressPercentage(delivery.status)}%` }}
            ></div>
          </div>
        </div>
      </div>

      {/* Delivery Timeline */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold mb-3">Delivery Timeline</h3>
        
        <div className="flex items-start space-x-3">
          <div className={`w-4 h-4 rounded-full mt-1 ${delivery.status === 'ASSIGNED' || delivery.status === 'PICKED_UP' || delivery.status === 'IN_TRANSIT' || delivery.status === 'DELIVERED' ? 'bg-blue-500' : 'bg-gray-300'}`}></div>
          <div className="flex-1">
            <p className="font-medium">Partner Assigned</p>
            <p className="text-sm text-gray-600">Delivery partner has been assigned to your order</p>
            {delivery.assignedAt && (
              <p className="text-xs text-gray-500 mt-1">
                {new Date(delivery.assignedAt).toLocaleString()}
              </p>
            )}
          </div>
        </div>

        <div className="flex items-start space-x-3">
          <div className={`w-4 h-4 rounded-full mt-1 ${delivery.status === 'PICKED_UP' || delivery.status === 'IN_TRANSIT' || delivery.status === 'DELIVERED' ? 'bg-blue-500' : 'bg-gray-300'}`}></div>
          <div className="flex-1">
            <p className="font-medium">Order Picked Up</p>
            <p className="text-sm text-gray-600">Delivery partner has picked up your order</p>
          </div>
        </div>

        <div className="flex items-start space-x-3">
          <div className={`w-4 h-4 rounded-full mt-1 ${delivery.status === 'IN_TRANSIT' || delivery.status === 'DELIVERED' ? 'bg-blue-500' : 'bg-gray-300'}`}></div>
          <div className="flex-1">
            <p className="font-medium">In Transit</p>
            <p className="text-sm text-gray-600">Your order is on the way</p>
          </div>
        </div>

        <div className="flex items-start space-x-3">
          <div className={`w-4 h-4 rounded-full mt-1 ${delivery.status === 'DELIVERED' ? 'bg-blue-500' : 'bg-gray-300'}`}></div>
          <div className="flex-1">
            <p className="font-medium">Delivered</p>
            <p className="text-sm text-gray-600">Order has been successfully delivered</p>
            {delivery.deliveredAt && (
              <p className="text-xs text-gray-500 mt-1">
                {new Date(delivery.deliveredAt).toLocaleString()}
              </p>
            )}
          </div>
        </div>
      </div>

      <div className="mt-6 flex space-x-3">
        <button
          onClick={fetchDelivery}
          className="flex-1 py-2 px-4 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600"
        >
          Refresh Status
        </button>
        <button
          className="flex-1 py-2 px-4 bg-gray-500 text-white font-semibold rounded-md hover:bg-gray-600"
        >
          Contact Partner
        </button>
      </div>
    </div>
  );
};

export default DeliveryTracker;
