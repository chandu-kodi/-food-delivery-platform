import React, { useState } from 'react';
import { restaurantAPI } from '../services/api';

const RestaurantList = ({ restaurants, loading }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCuisine, setSelectedCuisine] = useState('');
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);

  const filteredRestaurants = restaurants.filter(restaurant => {
    const matchesSearch = restaurant.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         restaurant.cuisineType.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCuisine = !selectedCuisine || restaurant.cuisineType === selectedCuisine;
    return matchesSearch && matchesCuisine;
  });

  const cuisineTypes = [...new Set(restaurants.map(r => r.cuisineType))];

  if (selectedRestaurant) {
    const RestaurantMenu = require('./RestaurantMenu').default;
    return (
      <RestaurantMenu 
        restaurant={selectedRestaurant} 
        onBack={() => setSelectedRestaurant(null)} 
      />
    );
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div>
      {/* Search and Filter */}
      <div className="mb-6 flex flex-col sm:flex-row gap-4">
        <div className="flex-1">
          <input
            type="text"
            placeholder="Search restaurants or cuisines..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <select
          value={selectedCuisine}
          onChange={(e) => setSelectedCuisine(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="">All Cuisines</option>
          {cuisineTypes.map(cuisine => (
            <option key={cuisine} value={cuisine}>{cuisine}</option>
          ))}
        </select>
      </div>

      {/* Restaurant Grid */}
      {filteredRestaurants.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">No restaurants found</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredRestaurants.map(restaurant => (
            <RestaurantCard 
              key={restaurant.id} 
              restaurant={restaurant} 
              onViewMenu={() => setSelectedRestaurant(restaurant)}
            />
          ))}
        </div>
      )}
    </div>
  );
};

const RestaurantCard = ({ restaurant, onViewMenu }) => {
  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
      {restaurant.imageUrl ? (
        <img 
          src={restaurant.imageUrl} 
          alt={restaurant.name}
          className="w-full h-48 object-cover"
        />
      ) : (
        <div className="w-full h-48 bg-gray-200 flex items-center justify-center">
          <span className="text-gray-400">No Image</span>
        </div>
      )}
      
      <div className="p-4">
        <div className="flex justify-between items-start mb-2">
          <h3 className="text-lg font-semibold text-gray-900">{restaurant.name}</h3>
          <div className="flex items-center">
            <span className="text-yellow-400">★</span>
            <span className="ml-1 text-sm text-gray-600">{restaurant.rating || 'N/A'}</span>
          </div>
        </div>
        
        <p className="text-gray-600 text-sm mb-2">{restaurant.cuisineType}</p>
        <p className="text-gray-700 text-sm mb-3 line-clamp-2">{restaurant.description}</p>
        
        <div className="flex items-center text-gray-500 text-xs mb-3">
          <span>📍 {restaurant.address}</span>
        </div>
        
        <button
          onClick={onViewMenu}
          className="w-full bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
        >
          View Menu
        </button>
      </div>
    </div>
  );
};

export default RestaurantList;
