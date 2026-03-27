import React from 'react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import OrderForm from './OrderForm';

const Cart = ({ onClose }) => {
  const { cartItems, getCartTotal, removeFromCart, updateQuantity, clearCart } = useCart();
  const { user } = useAuth();
  const [showOrderForm, setShowOrderForm] = React.useState(false);

  const handleQuantityChange = (itemId, restaurantId, quantity) => {
    updateQuantity(itemId, restaurantId, parseInt(quantity) || 0);
  };

  const handleRemoveItem = (itemId, restaurantId) => {
    removeFromCart(itemId, restaurantId);
  };

  const handleCheckout = () => {
    if (!user) {
      alert('Please login to place an order');
      return;
    }
    setShowOrderForm(true);
  };

  if (showOrderForm) {
    return (
      <OrderForm
        onClose={() => {
          setShowOrderForm(false);
          onClose();
        }}
      />
    );
  }

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-black bg-opacity-50" onClick={onClose}></div>
      
      <div className="absolute right-0 top-0 h-full w-full max-w-md bg-white shadow-xl">
        <div className="flex flex-col h-full">
          {/* Header */}
          <div className="flex items-center justify-between p-4 border-b">
            <h2 className="text-lg font-semibold">Your Cart</h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600"
            >
              ✕
            </button>
          </div>

          {/* Cart Items */}
          <div className="flex-1 overflow-y-auto p-4">
            {cartItems.length === 0 ? (
              <div className="text-center py-8">
                <p className="text-gray-500">Your cart is empty</p>
              </div>
            ) : (
              <div className="space-y-4">
                {cartItems.map((item) => (
                  <CartItem
                    key={`${item.id}-${item.restaurantId}`}
                    item={item}
                    onQuantityChange={handleQuantityChange}
                    onRemove={handleRemoveItem}
                  />
                ))}
              </div>
            )}
          </div>

          {/* Footer */}
          {cartItems.length > 0 && (
            <div className="border-t p-4">
              <div className="flex justify-between items-center mb-4">
                <span className="text-lg font-semibold">Total:</span>
                <span className="text-lg font-bold text-green-600">
                  ${getCartTotal().toFixed(2)}
                </span>
              </div>
              
              <div className="space-y-2">
                <button
                  onClick={handleCheckout}
                  className="w-full bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
                >
                  Proceed to Checkout
                </button>
                
                <button
                  onClick={clearCart}
                  className="w-full bg-gray-200 hover:bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
                >
                  Clear Cart
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

const CartItem = ({ item, onQuantityChange, onRemove }) => {
  return (
    <div className="bg-gray-50 rounded-lg p-3">
      <div className="flex items-start space-x-3">
        {item.imageUrl ? (
          <img 
            src={item.imageUrl} 
            alt={item.name}
            className="w-16 h-16 object-cover rounded"
          />
        ) : (
          <div className="w-16 h-16 bg-gray-200 rounded flex items-center justify-center">
            <span className="text-gray-400 text-xs">No Image</span>
          </div>
        )}
        
        <div className="flex-1">
          <h4 className="font-medium text-gray-900">{item.name}</h4>
          <p className="text-sm text-gray-500">{item.restaurantName}</p>
          <p className="text-sm font-semibold text-green-600">${item.price}</p>
          
          <div className="flex items-center justify-between mt-2">
            <div className="flex items-center space-x-2">
              <button
                onClick={() => onQuantityChange(item.id, item.restaurantId, item.quantity - 1)}
                className="w-8 h-8 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center"
              >
                -
              </button>
              <span className="w-8 text-center">{item.quantity}</span>
              <button
                onClick={() => onQuantityChange(item.id, item.restaurantId, item.quantity + 1)}
                className="w-8 h-8 rounded-full bg-gray-200 hover:bg-gray-300 flex items-center justify-center"
              >
                +
              </button>
            </div>
            
            <button
              onClick={() => onRemove(item.id, item.restaurantId)}
              className="text-red-500 hover:text-red-700 text-sm"
            >
              Remove
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;
