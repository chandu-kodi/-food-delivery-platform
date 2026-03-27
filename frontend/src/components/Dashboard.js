import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { restaurantAPI } from '../services/api';
import RestaurantList from './RestaurantList';
import MyOrders from './MyOrders';
import Cart from './Cart';

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState('restaurants');
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCart, setShowCart] = useState(false);
  const { user, logout } = useAuth();
  const { getCartCount, getCartTotal } = useCart();

  useEffect(() => {
    if (activeTab === 'restaurants') {
      fetchRestaurants();
    }
  }, [activeTab]);

  const fetchRestaurants = async () => {
    try {
      setLoading(true);
      const data = await restaurantAPI.getAllRestaurants();
      setRestaurants(data);
    } catch (error) {
      console.error('Failed to fetch restaurants:', error);
    } finally {
      setLoading(false);
    }
  };

  const renderContent = () => {
    switch (activeTab) {
      case 'restaurants':
        return <RestaurantList restaurants={restaurants} loading={loading} />;
      case 'orders':
        return <MyOrders />;
      default:
        return <RestaurantList restaurants={restaurants} loading={loading} />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">Food Delivery</h1>
              <p className="text-gray-600">Welcome back, {user?.firstName || user?.username}!</p>
            </div>
            <div className="flex items-center space-x-4">
              {/* Cart Button */}
              <button
                onClick={() => setShowCart(true)}
                className="relative bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
              >
                🛒 Cart
                {getCartCount() > 0 && (
                  <span className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full h-6 w-6 flex items-center justify-center">
                    {getCartCount()}
                  </span>
                )}
              </button>
              
              <button
                onClick={logout}
                className="bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation Tabs */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-8">
        <div className="border-b border-gray-200">
          <nav className="-mb-px flex space-x-8">
            <button
              onClick={() => setActiveTab('restaurants')}
              className={`${
                activeTab === 'restaurants'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm`}
            >
              Restaurants
            </button>
            <button
              onClick={() => setActiveTab('orders')}
              className={`${
                activeTab === 'orders'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm`}
            >
              My Orders
            </button>
          </nav>
        </div>
      </div>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {renderContent()}
      </main>

      {/* Cart Sidebar */}
      {showCart && (
        <Cart onClose={() => setShowCart(false)} />
      )}
    </div>
  );
};

export default Dashboard;
