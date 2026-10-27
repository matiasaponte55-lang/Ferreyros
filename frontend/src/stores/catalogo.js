// src/stores/catalogo.js - Store de Caché Reactiva y Datos Maestros Ferreyros
import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../api/client'

export const useCatalogoStore = defineStore('catalogo', () => {
  const sedes = ref([])
  const clientes = ref([])
  const metricasDashboard = ref(null)
  const ultimaCargaMetricas = ref(0)

  // Obtener Sedes con caché en memoria
  const obtenerSedes = async (forzar = false) => {
    if (!forzar && sedes.value.length > 0) {
      return sedes.value
    }
    try {
      const res = await api.get('/sedes')
      sedes.value = res.data || []
      return sedes.value
    } catch (e) {
      console.error('Error cargando sedes:', e)
      return sedes.value
    }
  }

  // Obtener Clientes con caché en memoria
  const obtenerClientes = async (forzar = false) => {
    if (!forzar && clientes.value.length > 0) {
      return clientes.value
    }
    try {
      const res = await api.get('/clientes')
      clientes.value = res.data || []
      return clientes.value
    } catch (e) {
      console.error('Error cargando clientes:', e)
      return clientes.value
    }
  }

  // Obtener Métricas del Dashboard con patrón SWR (Stale-While-Revalidate)
  const obtenerMetricas = async (callbackInmediato = null) => {
    const ahora = Date.now()
    // Si tenemos datos en caché, entregarlos de inmediato (0 ms)
    if (metricasDashboard.value && callbackInmediato) {
      callbackInmediato(metricasDashboard.value)
      // Si la caché tiene menos de 10 segundos, no re-consultar por red
      if (ahora - ultimaCargaMetricas.value < 10000) {
        return metricasDashboard.value
      }
    }

    try {
      const res = await api.get('/dashboard/metricas')
      metricasDashboard.value = res.data
      ultimaCargaMetricas.value = ahora
      return res.data
    } catch (e) {
      console.error('Error cargando métricas en store:', e)
      return metricasDashboard.value
    }
  }

  const invalidarDashboard = () => {
    ultimaCargaMetricas.value = 0
  }

  return {
    sedes,
    clientes,
    metricasDashboard,
    obtenerSedes,
    obtenerClientes,
    obtenerMetricas,
    invalidarDashboard
  }
})
