/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cat: {
          yellow: '#FFCD11',
          gold: '#F59E0B',
          dark: '#0B1320',
          card: '#111827',
          border: '#1F2937',
          gray: '#374151',
          accent: '#10B981',
          danger: '#EF4444'
        }
      }
    },
  },
  plugins: [],
}
