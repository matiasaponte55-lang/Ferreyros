// src/stores/catalogo.js - Store de Caché Reactiva y Datos Maestros Ferreyros
import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../api/client'

export const useCatalogoStore = defineStore('catalogo', () => {
  const sedes = ref([])
  const clientes = ref([])
  const ordenes = ref([])
  const tecnicos = ref([])
  const metricasDashboard = ref(null)
  const ultimaCargaMetricas = ref(0)
  const ultimaCargaOrdenes = ref(0)
  const ultimaCargaTecnicos = ref(0)

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

  // Obtener Órdenes con caché en memoria SWR (30s)
  const obtenerOrdenes = async (forzar = false) => {
    const ahora = Date.now()
    if (!forzar && ordenes.value.length > 0 && (ahora - ultimaCargaOrdenes.value < 30000)) {
      return ordenes.value
    }
    try {
      const res = await api.get('/ordenes-servicio')
      ordenes.value = res.data || []
      ultimaCargaOrdenes.value = ahora
      return ordenes.value
    } catch (e) {
      console.error('Error cargando órdenes:', e)
      return ordenes.value
    }
  }

  // Obtener Técnicos con caché en memoria SWR (30s)
  const obtenerTecnicos = async (forzar = false) => {
    const ahora = Date.now()
    if (!forzar && tecnicos.value.length > 0 && (ahora - ultimaCargaTecnicos.value < 30000)) {
      return tecnicos.value
    }
    try {
      const res = await api.get('/tecnicos')
      tecnicos.value = res.data || []
      ultimaCargaTecnicos.value = ahora
      return tecnicos.value
    } catch (e) {
      console.error('Error cargando técnicos:', e)
      return tecnicos.value
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
    ultimaCargaOrdenes.value = 0
  }

  return {
    sedes,
    clientes,
    ordenes,
    tecnicos,
    metricasDashboard,
    obtenerSedes,
    obtenerClientes,
    obtenerOrdenes,
    obtenerTecnicos,
    obtenerMetricas,
    invalidarDashboard
  }
})
