import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Configuración de Vite 5 para Ferreyros ServTech 2026
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    host: true,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
