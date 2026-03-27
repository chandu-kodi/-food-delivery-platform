import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { adminRestaurantAPI } from '../services/api';
import RestaurantList from './RestaurantList';
import RestaurantForm from './RestaurantForm';
import MenuManagement from './MenuManagement';

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState('restaurants');
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editingRestaurant, setEditingRestaurant] = useState(null);
  const [showRestaurantForm, setShowRestaurantForm] = useState(false);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const { user, logout } = useAuth();

  useEffect(() => {
    fetchRestaurants();
  }, []);

  const fetchRestaurants = async () => {
    try {
      setLoading(true);
      console.log('Fetching restaurants from API...');
      const data = await adminRestaurantAPI.getAllRestaurants();
      console.log('Restaurants fetched:', data);
      setRestaurants(data || []);
    } catch (error) {
      console.error('Failed to fetch restaurants:', error);
      console.error('Error details:', error.response?.data || error.message);
      // Show error message but don't clear existing restaurants
      alert('Failed to fetch restaurants. Please check if the restaurant service is running on port 8060.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddRestaurant = () => {
    setEditingRestaurant(null);
    setShowRestaurantForm(true);
  };

  const handleEditRestaurant = (restaurant) => {
    setEditingRestaurant(restaurant);
    setShowRestaurantForm(true);
  };

  const handleRestaurantSaved = () => {
    setShowRestaurantForm(false);
    setEditingRestaurant(null);
    fetchRestaurants();
  };

  const handleDeleteRestaurant = async (id) => {
    if (window.confirm('Are you sure you want to delete this restaurant?')) {
      try {
        await adminRestaurantAPI.deleteRestaurant(id);
        fetchRestaurants();
      } catch (error) {
        console.error('Failed to delete restaurant:', error);
        alert('Failed to delete restaurant');
      }
    }
  };

  const handleManageMenu = (restaurant) => {
    setSelectedRestaurant(restaurant);
    setActiveTab('menu');
  };

  const renderContent = () => {
    if (showRestaurantForm) {
      return (
        <RestaurantForm
          restaurant={editingRestaurant}
          onSave={handleRestaurantSaved}
          onCancel={() => setShowRestaurantForm(false)}
        />
      );
    }

    switch (activeTab) {
      case 'restaurants':
        return (
          <RestaurantList
            restaurants={restaurants}
            loading={loading}
            onAdd={handleAddRestaurant}
            onEdit={handleEditRestaurant}
            onDelete={handleDeleteRestaurant}
            onManageMenu={handleManageMenu}
          />
        );
      case 'menu':
        return selectedRestaurant ? (
          <MenuManagement
            restaurant={selectedRestaurant}
            onBack={() => setActiveTab('restaurants')}
          />
        ) : null;
      default:
        return null;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
              <p className="text-gray-600">Welcome, {user?.username}!</p>
            </div>
            <button
              onClick={logout}
              className="bg-red-500 hover:bg-red-600 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
            >
              Logout
            </button>
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
              onClick={() => selectedRestaurant && setActiveTab('menu')}
              disabled={!selectedRestaurant}
              className={`${
                activeTab === 'menu'
                  ? 'border-blue-500 text-blue-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              } whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm disabled:text-gray-300 disabled:cursor-not-allowed`}
            >
              Menu Management
            </button>
          </nav>
        </div>
      </div>

      {/* Content */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {renderContent()}
      </main>
    </div>
  );
};

export default Dashboard;
