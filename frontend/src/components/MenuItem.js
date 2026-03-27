import React, { useState } from 'react';
import { useCart } from '../context/CartContext';

const MenuItem = ({ item, restaurantId }) => {
  const { addToCart } = useCart();
  const [addedAnimation, setAddedAnimation] = useState(false);

  const handleAddToCart = () => {
    addToCart(item, restaurantId);
    setAddedAnimation(true);
    setTimeout(() => setAddedAnimation(false), 500);
  };

  return (
    <div className="card-item">
      {item.image && (
        <div className="relative overflow-hidden rounded-lg mb-3 h-40">
          <img
            src={item.image}
            alt={item.name}
            className="w-full h-full object-cover hover:scale-105 transition duration-300"
          />
          {item.veg !== undefined && (
            <div
              className={`absolute top-2 left-2 w-4 h-4 rounded border-2 ${
                item.veg
                  ? 'border-green-600 bg-green-100'
                  : 'border-red-600 bg-red-100'
              }`}
            />
          )}
        </div>
      )}

      <div className="mb-2">
        <h3 className="font-bold text-gray-800">{item.name}</h3>
        <p className="text-sm text-gray-600 line-clamp-2">{item.description}</p>
      </div>

      <div className="flex justify-between items-center">
        <div className="flex items-center gap-2">
          {item.oldPrice && (
            <span className="text-gray-400 line-through text-sm">
              ₹{item.oldPrice}
            </span>
          )}
          <span className="text-lg font-bold text-gray-800">₹{item.price}</span>
        </div>

        <button
          onClick={handleAddToCart}
          className={`px-4 py-2 rounded-lg text-white transition duration-300 ${
            addedAnimation
              ? 'bg-green-500 scale-110'
              : 'bg-primary hover:bg-red-600'
          }`}
        >
          {addedAnimation ? '✓' : '+'}
        </button>
      </div>
    </div>
  );
};

export default MenuItem;
