<template>
  <div class="space-y-6">
    <!-- BARRA SUPERIOR CON RETORNO Y BOTÓN DE APERTURA DE OT -->
    <div class="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
      <div>
        <router-link to="/catalogo" class="text-xs text-yellow-400 hover:text-yellow-300 font-semibold flex items-center space-x-1 mb-2">
          <span>← Volver al Catálogo de Maquinarias</span>
        </router-link>
        <h1 class="text-xl sm:text-2xl font-black text-white flex items-center space-x-3">
          <span>FICHA TÉCNICA 360°: {{ maquinaria?.modelo?.nombreComercial || maquinaria?.modeloCat || '-' }}</span>
          <span v-if="maquinaria?.codigoFlota" class="text-xs font-mono bg-yellow-400/20 text-yellow-400 px-2.5 py-1 rounded border border-yellow-400/40">
            {{ maquinaria?.codigoFlota }}
          </span>
        </h1>
        <p class="text-xs text-gray-400">Expediente técnico centralizado, telemetría MineStar y trazabilidad del ciclo de vida del activo</p>
      </div>

      <router-link 
        :to="`/ordenes/nueva?pin=${maquinaria?.numeroSeriePin}`"
        class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold px-4 py-2.5 rounded-lg text-sm shadow-lg hover:shadow-yellow-500/20 transition flex items-center space-x-2">
        <span>➕</span>
        <span>Aperturar Nueva OT para este Equipo</span>
      </router-link>
    </div>

    <!-- PANELES SUPERIORES: ESPECIFICACIONES DE INGENIERÍA + TELEMETRÍA EN VIVO -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      
      <!-- 1. ESPECIFICACIONES DE INGENIERÍA CAT -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg">
        <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider mb-4 border-b border-gray-800 pb-2">
          1. Especificaciones de Ingeniería CAT
        </h2>

        <div class="space-y-3 text-xs sm:text-sm">
          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Número de Serie PIN:</span>
            <span class="font-mono font-bold text-white">{{ maquinaria?.numeroSeriePin || '-' }}</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Modelo / Familia CAT:</span>
            <span class="font-semibold text-white">{{ maquinaria?.modeloCat || maquinaria?.familia || '-' }}</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Cliente Titular:</span>
            <span class="font-semibold text-white">{{ maquinaria?.cliente?.razonSocial || '-' }}</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Ubicación / Sede:</span>
            <span class="font-semibold text-yellow-400">{{ maquinaria?.sedeActual?.nombre || '-' }}</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Contrato MARC:</span>
            <span class="font-semibold text-emerald-400">{{ maquinaria?.contratoMarc || '-' }}</span>
          </div>

          <div class="flex justify-between py-1">
            <span class="text-gray-400">Horómetro Verificado:</span>
            <span class="font-mono font-extrabold text-yellow-400 text-base">
              {{ Number(maquinaria?.horometroAcumulado || 0).toLocaleString('es-PE', { minimumFractionDigits: 1 }) }} hrs
            </span>
          </div>
        </div>
      </div>

      <!-- 2. PARÁMETROS TELEMETRÍA MINESTAR / VIMS EN VIVO -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg">
        <div class="flex items-center justify-between mb-4 border-b border-gray-800 pb-2">
          <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider">
            2. Parámetros Telemetría MineStar en Vivo
          </h2>
          <span 
            class="inline-flex items-center px-2 py-0.5 rounded text-[11px] font-bold"
            :class="maquinaria?.telemetriaActiva ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/40' : 'bg-gray-800 text-gray-400 border border-gray-700'">
            <span v-if="maquinaria?.telemetriaActiva" class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse mr-1"></span>
            {{ maquinaria?.telemetriaActiva ? 'ONLINE - Señal Satelital' : 'OFFLINE - Sin Señal' }}
          </span>
        </div>

        <div v-if="maquinaria?.telemetriaActiva" class="space-y-3 text-xs sm:text-sm">
          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Estado Operativo en Mina:</span>
            <span class="font-bold text-emerald-400">{{ maquinaria?.estadoOperativo || 'OPERATIVO' }}</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">SLA Contractual Garantizado:</span>
            <span class="font-mono font-bold text-yellow-400">{{ maquinaria?.slaGarantizado ?? 92.0 }}%</span>
          </div>

          <div class="flex justify-between py-1 border-b border-gray-800/60">
            <span class="text-gray-400">Horómetro en Central ECM:</span>
            <span class="font-mono font-bold text-white">{{ Number(maquinaria?.horometroAcumulado || 0).toFixed(1) }} hrs</span>
          </div>

          <div class="flex justify-between py-1">
            <span class="text-gray-400">Última Transmisión VIMS:</span>
            <span class="text-gray-300 font-mono text-xs">
              {{ maquinaria?.actualizadoEn ? new Date(maquinaria.actualizadoEn).toLocaleString('es-PE') : 'Sincronizado' }}
            </span>
          </div>
        </div>
        <div v-else class="text-center py-8 text-gray-500 text-xs">
          Telemetría no activa para esta unidad.
        </div>
      </div>

    </div>

    <!-- 3. HISTORIAL DE ÓRDENES DE TRABAJO ANTERIORES -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg">
      <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider mb-4 border-b border-gray-800 pb-2">
        3. Historial Auditado de Órdenes de Trabajo del Activo
      </h2>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs sm:text-sm text-gray-300">
          <thead class="bg-[#161F30] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-800">
            <tr>
              <th class="py-3 px-3">Código OT</th>
              <th class="py-3 px-3">Tipo Servicio</th>
              <th class="py-3 px-3">Horómetro Registro</th>
              <th class="py-3 px-3">Falla / Rutina</th>
              <th class="py-3 px-3 text-center">Estado</th>
              <th class="py-3 px-3 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="historialOts.length === 0">
              <td colspan="6" class="text-center py-6 text-gray-400">No hay órdenes registradas para esta maquinaria.</td>
            </tr>
            <tr v-for="ot in historialOts" :key="ot.idOrden" class="hover:bg-gray-800/40 transition">
              <td class="py-3 px-3 font-mono font-bold text-[#FFCD11]">{{ ot.codigoOt }}</td>
              <td class="py-3 px-3 text-white font-semibold">{{ ot.tipoMantenimiento }}</td>
              <td class="py-3 px-3 font-mono text-gray-200">{{ ot.horometroRegistro }} hrs</td>
              <td class="py-3 px-3 text-gray-400 max-w-xs truncate">{{ ot.fallaReportada }}</td>
              <td class="py-3 px-3 text-center">
                <span class="px-2 py-0.5 rounded text-[11px] font-bold uppercase bg-blue-500/20 text-blue-300">
                  {{ ot.estado }}
                </span>
              </td>
              <td class="py-3 px-3 text-center space-x-1">
                <router-link :to="`/inspeccion/${ot.idOrden}`" class="text-xs text-yellow-400 hover:underline">ATS</router-link>
                <span class="text-gray-600">|</span>
                <router-link :to="`/liquidacion/${ot.idOrden}`" class="text-xs text-emerald-400 hover:underline">Acta PDF</router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import api from '../api/client'

const route = useRoute()
const maquinaria = ref(null)
const historialOts = ref([])
const cargando = ref(true)

const cargarDatos = async () => {
  const id = route.params.id
  cargando.value = true
  try {
    const resMaq = await api.get(`/maquinarias/${id}`)
    maquinaria.value = resMaq.data

    const resOts = await api.get(`/ordenes-servicio/maquinaria/${id}`)
    historialOts.value = resOts.data
  } catch (err) {
    console.error('Error cargando ficha técnica:', err)
  } finally {
    cargando.value = false
  }
}

onMounted(() => {
  cargarDatos()
})
</script>
