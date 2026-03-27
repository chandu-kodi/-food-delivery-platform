/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#FF5757',
        secondary: '#F4F4F4',
      },
      fontFamily: {
        sans: ['poppins', 'sans-serif'],
      },
    },
  },
  plugins: [],
}
