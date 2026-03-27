import React, { useState, useEffect } from 'react';
import { restaurantAPI } from '../services/api';
import { useCart } from '../context/CartContext';

const RestaurantMenu = ({ restaurant, onBack }) => {
  const [menuData, setMenuData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const { addToCart } = useCart();

  useEffect(() => {
    fetchMenu();
  }, [restaurant.id]);

  const fetchMenu = async () => {
    try {
      setLoading(true);
      const data = await restaurantAPI.getRestaurantMenu(restaurant.id);
      setMenuData(data);
      if (data.categories && data.categories.length > 0) {
        setSelectedCategory(data.categories[0].id);
      }
    } catch (error) {
      console.error('Failed to fetch menu:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = (menuItem) => {
    addToCart(menuItem, restaurant.id);
  };

  const getItemsForCategory = () => {
    if (!menuData || !selectedCategory) return [];
    
    const items = menuData.itemsByCategory[selectedCategory] || [];
    // Filter by is_available (backend field name) not isAvailable
    return items.filter(item => item.is_available !== false);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  if (!menuData) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500 text-lg">Menu not available</p>
      </div>
    );
  }

  return (
    <div>
      {/* Restaurant Header */}
      <div className="mb-6">
        <button
          onClick={onBack}
          className="text-blue-600 hover:text-blue-800 font-medium mb-4"
        >
          ← Back to Restaurants
        </button>
        
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-start space-x-6">
            {restaurant.imageUrl ? (
              <img 
                src={restaurant.imageUrl} 
                alt={restaurant.name}
                className="w-32 h-32 object-cover rounded-lg"
              />
            ) : (
              <div className="w-32 h-32 bg-gray-200 rounded-lg flex items-center justify-center">
                <span className="text-gray-400">No Image</span>
              </div>
            )}
            
            <div className="flex-1">
              <h1 className="text-3xl font-bold text-gray-900 mb-2">{restaurant.name}</h1>
              <p className="text-gray-600 mb-2">{restaurant.cuisineType}</p>
              <p className="text-gray-700 mb-4">{restaurant.description}</p>
              
              <div className="flex items-center space-x-4 text-sm text-gray-500">
                <div className="flex items-center">
                  <span className="text-yellow-400">★</span>
                  <span className="ml-1">{restaurant.rating || 'N/A'}</span>
                </div>
                <span>📍 {restaurant.address}</span>
                <span>📞 {restaurant.phone}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Categories */}
      <div className="mb-6">
        <div className="flex space-x-2 overflow-x-auto pb-2">
          {menuData.categories.map(category => (
            <button
              key={category.id}
              onClick={() => setSelectedCategory(category.id)}
              className={`px-4 py-2 rounded-full whitespace-nowrap ${
                selectedCategory === category.id
                  ? 'bg-blue-600 text-white'
                  : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
              }`}
            >
              {category.name}
            </button>
          ))}
        </div>
      </div>

      {/* Menu Items */}
      <div className="space-y-4">
        {getItemsForCategory().map(item => (
          <MenuItemCard 
            key={item.id} 
            item={item} 
            onAddToCart={() => handleAddToCart(item)}
          />
        ))}
      </div>

      {getItemsForCategory().length === 0 && (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">No items available in this category</p>
        </div>
      )}
    </div>
  );
};

const MenuItemCard = ({ item, onAddToCart }) => {
  return (
    <div className="bg-white rounded-lg shadow-md p-4 hover:shadow-lg transition-shadow duration-300">
      <div className="flex items-start space-x-4">
        {item.imageUrl ? (
          <img 
            src={item.imageUrl} 
            alt={item.name}
            className="w-24 h-24 object-cover rounded-lg"
          />
        ) : (
          <div className="w-24 h-24 bg-gray-200 rounded-lg flex items-center justify-center">
            <span className="text-gray-400 text-xs">No Image</span>
          </div>
        )}
        
        <div className="flex-1">
          <div className="flex justify-between items-start mb-2">
            <h3 className="text-lg font-semibold text-gray-900">{item.name}</h3>
            <span className="text-lg font-bold text-green-600">${item.price}</span>
          </div>
          
          {item.description && (
            <p className="text-gray-600 text-sm mb-2">{item.description}</p>
          )}
          
          {item.specialInstructions && (
            <p className="text-gray-500 text-xs italic mb-2">{item.specialInstructions}</p>
          )}
          
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2 text-sm text-gray-500">
              {item.is_vegetarian && (
                <span className="bg-green-100 text-green-800 px-2 py-1 rounded-full text-xs">
                  Vegetarian
                </span>
              )}
              {item.preparation_time && (
                <span>⏱️ {item.preparation_time} mins</span>
              )}
            </div>
            
            <button
              onClick={onAddToCart}
              className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md text-sm transition duration-150 ease-in-out"
            >
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RestaurantMenu;
