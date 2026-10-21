// src/api/client.js - Cliente Axios con Interceptores de Seguridad JWT (Sección 10.2)
import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' }
})

// Inyección automática del Token JWT Bearer en cada petición
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('servtech_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
}, (error) => Promise.reject(error))

// Interceptor de respuesta para manejo de sesión
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('servtech_token')
      localStorage.removeItem('servtech_user')
      if (window.location.pathname !== '/login') {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default api
