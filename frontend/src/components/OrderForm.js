import React, { useState } from 'react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { orderAPI, paymentAPI } from '../services/api';

const OrderForm = ({ onClose }) => {
  const { cartItems, getCartTotal, clearCart } = useCart();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [orderData, setOrderData] = useState({
    deliveryAddress: user?.address || '',
    phoneNumber: user?.phone || '',
    specialInstructions: ''
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setOrderData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      // Validate form
      if (!orderData.deliveryAddress.trim()) {
        setError('Delivery address is required');
        return;
      }
      if (!orderData.phoneNumber.trim()) {
        setError('Phone number is required');
        return;
      }

      // Get first restaurant ID (all items should be from same restaurant)
      const restaurantId = cartItems[0]?.restaurantId;
      console.log('Cart items:', cartItems);
      console.log('First item restaurantId:', cartItems[0]?.restaurantId);
      
      if (!restaurantId) {
        setError('Restaurant information not available. Please add items from a restaurant menu first.');
        return;
      }

      // Create order request
      const orderRequest = {
        userId: user.userId || user.id,
        restaurantId: restaurantId,
        deliveryAddress: orderData.deliveryAddress,
        phoneNumber: orderData.phoneNumber,
        specialInstructions: orderData.specialInstructions,
        orderItems: cartItems.map(item => ({
          menuItemId: item.id,
          menuItemName: item.name,
          quantity: item.quantity,
          unitPrice: item.price,
          specialInstructions: item.specialInstructions || ''
        }))
      };

      // Create order
      const orderResponse = await orderAPI.createOrder(orderRequest);

      // Create payment (COD)
      const paymentRequest = {
        orderId: orderResponse.id,
        userId: user.userId || user.id,
        restaurantId: restaurantId,
        orderNumber: orderResponse.orderNumber,
        paymentMethod: 'CASH_ON_DELIVERY',
        amount: getCartTotal(),
        deliveryAddress: orderData.deliveryAddress,
        phoneNumber: orderData.phoneNumber,
        specialInstructions: orderData.specialInstructions
      };

      // Process payment asynchronously (don't block user experience)
      try {
        await paymentAPI.createPayment(paymentRequest);
      } catch (paymentError) {
        console.warn('Payment processing failed, but order was created:', paymentError);
        // Don't fail the entire order process for payment issues
      }

      // Clear any previous errors
      setError('');
      
      // Clear cart and close form
      clearCart();
      
      // Show success message
      setSuccess(`Order placed successfully! Order #${orderResponse.orderNumber || 'placed'} is being processed.`);
      
      // Close form after a short delay to show success message
      setTimeout(() => {
        onClose();
        setSuccess('');
      }, 2000);

    } catch (error) {
      console.error('Order creation failed:', error);
      console.error('Error response:', error.response);
      console.error('Error status:', error.response?.status);
      console.error('Error data:', error.response?.data);
      
      let errorMessage = 'Failed to place order. Please try again.';
      
      // Handle network errors more gracefully
      if (error.code === 'ERR_NETWORK' || error.response?.status === 0) {
        errorMessage = 'Order may have been placed but there was a network issue. Please check "My Orders" to confirm.';
      } else if (error.response?.status === 401) {
        errorMessage = 'Please login to place an order.';
      } else if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.message) {
        errorMessage = error.message;
      }
      
      setError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const subtotal = getCartTotal();
  const deliveryFee = 2.99; // Fixed delivery fee
  const total = subtotal + deliveryFee;

  return (
    <div className="fixed inset-0 z-50 overflow-hidden">
      <div className="absolute inset-0 bg-black bg-opacity-50" onClick={onClose}></div>
      
      <div className="absolute right-0 top-0 h-full w-full max-w-md bg-white shadow-xl overflow-y-auto">
        <div className="p-6">
          {/* Header */}
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-xl font-semibold">Checkout</h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600"
            >
              ✕
            </button>
          </div>

          {/* Order Summary */}
          <div className="mb-6">
            <h3 className="font-medium mb-3">Order Summary</h3>
            <div className="bg-gray-50 rounded-lg p-4 space-y-2">
              {cartItems.map((item) => (
                <div key={`${item.id}-${item.restaurantId}`} className="flex justify-between text-sm">
                  <span>{item.quantity}x {item.name}</span>
                  <span>${(item.price * item.quantity).toFixed(2)}</span>
                </div>
              ))}
              <div className="border-t pt-2 mt-2">
                <div className="flex justify-between text-sm">
                  <span>Subtotal</span>
                  <span>${subtotal.toFixed(2)}</span>
                </div>
                <div className="flex justify-between text-sm">
                  <span>Delivery Fee</span>
                  <span>${deliveryFee.toFixed(2)}</span>
                </div>
                <div className="flex justify-between font-semibold mt-2">
                  <span>Total</span>
                  <span className="text-green-600">${total.toFixed(2)}</span>
                </div>
              </div>
            </div>
          </div>

          {/* Delivery Information */}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Delivery Address *
              </label>
              <textarea
                name="deliveryAddress"
                value={orderData.deliveryAddress}
                onChange={handleInputChange}
                required
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter your delivery address"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Phone Number *
              </label>
              <input
                type="tel"
                name="phoneNumber"
                value={orderData.phoneNumber}
                onChange={handleInputChange}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Enter your phone number"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Special Instructions (Optional)
              </label>
              <textarea
                name="specialInstructions"
                value={orderData.specialInstructions}
                onChange={handleInputChange}
                rows={2}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Any special instructions for delivery"
              />
            </div>

            {/* Payment Method */}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <h4 className="font-medium text-blue-900 mb-2">Payment Method</h4>
              <div className="flex items-center">
                <span className="text-2xl mr-2">💵</span>
                <div>
                  <p className="font-medium text-blue-900">Cash on Delivery</p>
                  <p className="text-sm text-blue-700">Pay when you receive your order</p>
                </div>
              </div>
            </div>

            {error && (
              <div className="bg-red-50 border border-red-200 rounded-lg p-3">
                <p className="text-red-800 text-sm">{error}</p>
              </div>
            )}

            {success && (
              <div className="bg-green-50 border border-green-200 rounded-lg p-3">
                <p className="text-green-800 text-sm">{success}</p>
              </div>
            )}

            <div className="flex space-x-3 pt-4">
              <button
                type="button"
                onClick={onClose}
                className="flex-1 bg-gray-200 hover:bg-gray-300 text-gray-700 font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading}
                className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded-md transition duration-150 ease-in-out disabled:bg-blue-400 disabled:cursor-not-allowed"
              >
                {loading ? 'Placing Order...' : 'Place Order'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default OrderForm;
