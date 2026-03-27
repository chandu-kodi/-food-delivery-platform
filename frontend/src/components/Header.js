import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';

const Header = () => {
  const { user, logout, isAuthenticated } = useAuth();
  const { getCartCount } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="bg-white shadow-sm sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
        <div className="flex items-center gap-8">
          <Link to="/" className="text-2xl font-bold text-primary">
            FoodHub
          </Link>

          {isAuthenticated && (
            <nav className="hidden md:flex gap-6">
              <Link to="/restaurants" className="hover:text-primary transition">
                Restaurants
              </Link>
              <Link to="/orders" className="hover:text-primary transition">
                My Orders
              </Link>
            </nav>
          )}
        </div>

        <div className="flex items-center gap-4">
          {isAuthenticated ? (
            <>
              <Link
                to="/cart"
                className="relative bg-primary text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
              >
                🛒 Cart
                {getCartCount() > 0 && (
                  <span className="absolute top-0 right-0 bg-red-700 text-white rounded-full w-5 h-5 flex items-center justify-center text-xs -mt-2 -mr-2">
                    {getCartCount()}
                  </span>
                )}
              </Link>

              <div className="flex items-center gap-3">
                <span className="hidden sm:inline text-sm text-gray-700">
                  {user?.name}
                </span>
                <button
                  onClick={handleLogout}
                  className="bg-gray-200 text-gray-800 px-4 py-2 rounded-lg hover:bg-gray-300 transition"
                >
                  Logout
                </button>
              </div>
            </>
          ) : (
            <div className="flex gap-2">
              <Link
                to="/login"
                className="text-primary hover:text-red-600 transition font-semibold"
              >
                Login
              </Link>
              <Link
                to="/register"
                className="bg-primary text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
              >
                Sign Up
              </Link>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
