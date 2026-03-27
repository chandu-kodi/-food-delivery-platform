import React, { useState } from 'react';
import { paymentAPI } from '../services/api';

const PaymentForm = ({ order, onPaymentCompleted }) => {
  const [paymentData, setPaymentData] = useState({
    orderId: order?.id || '',
    amount: order?.totalAmount || 0,
    paymentMethod: 'CREDIT_CARD',
    cardNumber: '',
    cardHolder: '',
    expiryDate: '',
    cvv: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPaymentData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await paymentAPI.createPayment(paymentData);
      onPaymentCompleted(response);
    } catch (err) {
      setError('Payment failed. Please try again.');
      console.error('Payment error:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">Payment Details</h2>
      
      {order && (
        <div className="mb-6 p-4 bg-gray-50 rounded-md">
          <h3 className="font-semibold mb-2">Order Summary</h3>
          <p className="text-sm text-gray-600">Order ID: {order.id}</p>
          <p className="text-sm text-gray-600">Customer: {order.customerName}</p>
          <p className="text-lg font-bold">Amount: ${order.totalAmount?.toFixed(2) || '0.00'}</p>
        </div>
      )}

      {error && (
        <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Payment Method
          </label>
          <select
            name="paymentMethod"
            value={paymentData.paymentMethod}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="CREDIT_CARD">Credit Card</option>
            <option value="DEBIT_CARD">Debit Card</option>
            <option value="PAYPAL">PayPal</option>
            <option value="CASH">Cash on Delivery</option>
          </select>
        </div>

        {paymentData.paymentMethod.includes('CARD') && (
          <>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Card Number
              </label>
              <input
                type="text"
                name="cardNumber"
                value={paymentData.cardNumber}
                onChange={handleChange}
                placeholder="1234 5678 9012 3456"
                maxLength="19"
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Card Holder Name
              </label>
              <input
                type="text"
                name="cardHolder"
                value={paymentData.cardHolder}
                onChange={handleChange}
                placeholder="John Doe"
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Expiry Date
                </label>
                <input
                  type="text"
                  name="expiryDate"
                  value={paymentData.expiryDate}
                  onChange={handleChange}
                  placeholder="MM/YY"
                  maxLength="5"
                  required
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  CVV
                </label>
                <input
                  type="text"
                  name="cvv"
                  value={paymentData.cvv}
                  onChange={handleChange}
                  placeholder="123"
                  maxLength="4"
                  required
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>
          </>
        )}

        <div className="border-t pt-4">
          <div className="flex justify-between items-center">
            <span className="text-lg font-semibold">Total Amount:</span>
            <span className="text-lg font-bold">${paymentData.amount.toFixed(2)}</span>
          </div>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full py-2 px-4 bg-green-500 text-white font-semibold rounded-md hover:bg-green-600 disabled:bg-gray-400 disabled:cursor-not-allowed"
        >
          {loading ? 'Processing Payment...' : 'Complete Payment'}
        </button>
      </form>
    </div>
  );
};

export default PaymentForm;
