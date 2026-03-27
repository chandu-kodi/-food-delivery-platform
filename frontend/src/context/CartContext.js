import React, { createContext, useContext, useState, useEffect } from 'react';
import { useAuth } from './AuthContext';

const CartContext = createContext();

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within CartProvider');
  }
  return context;
};

export const CartProvider = ({ children }) => {
  const [cartItems, setCartItems] = useState([]);
  const { user, isAuthenticated } = useAuth();

  // Get user-specific cart key
  const getCartKey = () => {
    return user ? `cart_${user.userId || user.username}` : 'cart_guest';
  };

  // Load cart from localStorage on mount or when user changes
  useEffect(() => {
    // Only load cart if user is available or if we're explicitly loading guest cart
    if (user || isAuthenticated === false) {
      const cartKey = getCartKey();
      const savedCart = localStorage.getItem(cartKey);
      if (savedCart) {
        try {
          setCartItems(JSON.parse(savedCart));
        } catch (err) {
          console.error('Error loading cart:', err);
          setCartItems([]);
        }
      } else {
        setCartItems([]);
      }
    }
  }, [user, isAuthenticated]); // Reload cart when user changes or auth state updates

  // Save cart to localStorage whenever it changes
  useEffect(() => {
    const cartKey = getCartKey();
    if (cartItems.length > 0 || localStorage.getItem(cartKey)) {
      localStorage.setItem(cartKey, JSON.stringify(cartItems));
    }
  }, [cartItems, user]);

  // Note: We don't clear cart on logout anymore to preserve user's cart

  const addToCart = (item, restaurantId) => {
    setCartItems((prev) => {
      const existingItem = prev.find((i) => i.id === item.id && i.restaurantId === restaurantId);

      if (existingItem) {
        return prev.map((i) =>
          i.id === item.id && i.restaurantId === restaurantId
            ? { ...i, quantity: i.quantity + 1 }
            : i
        );
      }

      return [...prev, { ...item, restaurantId, quantity: 1 }];
    });
  };

  const removeFromCart = (itemId, restaurantId) => {
    setCartItems((prev) => prev.filter((i) => !(i.id === itemId && i.restaurantId === restaurantId)));
  };

  const updateQuantity = (itemId, restaurantId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(itemId, restaurantId);
    } else {
      setCartItems((prev) =>
        prev.map((i) =>
          i.id === itemId && i.restaurantId === restaurantId
            ? { ...i, quantity }
            : i
        )
      );
    }
  };

  const clearCart = () => {
    setCartItems([]);
    // Also clear from localStorage
    const cartKey = getCartKey();
    localStorage.removeItem(cartKey);
  };

  const getCartTotal = () => {
    return cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
  };

  const getCartCount = () => {
    return cartItems.reduce((count, item) => count + item.quantity, 0);
  };

  const getCartByRestaurant = (restaurantId) => {
    return cartItems.filter((item) => item.restaurantId === restaurantId);
  };

  const value = {
    cartItems,
    addToCart,
    removeFromCart,
    updateQuantity,
    clearCart,
    getCartTotal,
    getCartCount,
    getCartByRestaurant,
  };

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
};

export default CartContext;
