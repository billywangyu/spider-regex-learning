export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: { DEFAULT: '#667eea', foreground: '#ffffff' },
        background: '#f8fafc',
        foreground: '#1e293b',
        card: '#ffffff',
        muted: '#e2e8f0',
        accent: '#f97316',
        border: '#e2e8f0'
      },
      fontFamily: { sans: ['Inter', 'system-ui', 'sans-serif'] }
    }
  },
  plugins: []
}
