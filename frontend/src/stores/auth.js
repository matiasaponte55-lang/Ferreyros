// src/stores/auth.js - Gestión de Estado Global con Pinia (RF-01 / RNF-05)
import { defineStore } from 'pinia'
import api from '../api/client'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('servtech_token') || null,
    user: JSON.parse(localStorage.getItem('servtech_user') || 'null'),
    currentSede: localStorage.getItem('servtech_sede') || 'Centro de Reparación de Componentes Arequipa - La Joya'
  }),

  getters: {
    isAuthenticated: (state) => !!state.token && !!state.user,
    rol: (state) => state.user?.rol || '',
    nombreCompleto: (state) => state.user ? `${state.user.nombres} ${state.user.apellidos}` : 'Usuario No Autenticado',
    isAdmin: (state) => state.user?.rol === 'ROLE_ADMIN',
    isPlanificador: (state) => state.user?.rol === 'ROLE_PLANIFICADOR' || state.user?.rol === 'ROLE_ADMIN',
    isSupervisor: (state) => state.user?.rol === 'ROLE_SUPERVISOR' || state.user?.rol === 'ROLE_ADMIN',
    isTecnico: (state) => state.user?.rol === 'ROLE_TECNICO'
  },

  actions: {
    /**
     * Autenticación estricta contra el Backend y PostgreSQL (Supabase).
     * Si las credenciales no son válidas o el usuario no existe, arroja error y no concede acceso.
     */
    async login(username, password, rol = null) {
      const response = await api.post('/auth/login', { 
        username: username.trim(), 
        password: password.trim(), 
        rol: rol 
      })

      if (!response.data || !response.data.token) {
        throw new Error('Respuesta de autenticación inválida del servidor')
      }

      this.token = response.data.token
      this.user = response.data.user
      
      localStorage.setItem('servtech_token', this.token)
      localStorage.setItem('servtech_user', JSON.stringify(this.user))
      
      return { success: true }
    },

    cambiarSede(nuevaSede) {
      this.currentSede = nuevaSede
      localStorage.setItem('servtech_sede', nuevaSede)
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.removeItem('servtech_token')
      localStorage.removeItem('servtech_user')
      localStorage.removeItem('servtech_sede')
    }
  }
})
