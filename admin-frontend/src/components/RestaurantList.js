import React from 'react';

const RestaurantList = ({ restaurants, loading, onAdd, onEdit, onDelete, onManageMenu }) => {
  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return (
    <div>
      {/* Header */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-bold text-gray-900">Restaurants</h2>
        <button
          onClick={onAdd}
          className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
        >
          Add Restaurant
        </button>
      </div>

      {/* Restaurant Grid */}
      {restaurants.length === 0 ? (
        <div className="text-center py-12">
          <p className="text-gray-500 text-lg">No restaurants found</p>
          <p className="text-gray-400 mt-2">Get started by adding your first restaurant</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {restaurants.map(restaurant => (
            <RestaurantCard
              key={restaurant.id}
              restaurant={restaurant}
              onEdit={onEdit}
              onDelete={onDelete}
              onManageMenu={onManageMenu}
            />
          ))}
        </div>
      )}
    </div>
  );
};

const RestaurantCard = ({ restaurant, onEdit, onDelete, onManageMenu }) => {
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

        <div className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mb-3 ${
          restaurant.isActive 
            ? 'bg-green-100 text-green-800' 
            : 'bg-red-100 text-red-800'
        }`}>
          {restaurant.isActive ? 'Active' : 'Inactive'}
        </div>
        
        <div className="flex space-x-2">
          <button
            onClick={() => onManageMenu(restaurant)}
            className="flex-1 bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-3 rounded text-sm transition duration-150 ease-in-out"
          >
            Manage Menu
          </button>
          <button
            onClick={() => onEdit(restaurant)}
            className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-3 rounded text-sm transition duration-150 ease-in-out"
          >
            Edit
          </button>
          <button
            onClick={() => onDelete(restaurant.id)}
            className="bg-red-600 hover:bg-red-700 text-white font-medium py-2 px-3 rounded text-sm transition duration-150 ease-in-out"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestaurantList;
