<template>
  <div class="space-y-6">
    <!-- ENCABEZADO -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Flujo Kanban</span>
          <span class="text-xs text-yellow-400 font-semibold">Regla de Retrabajo Mandatorio (RF-07 / RF-14)</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">TABLERO KANBAN OPERATIVO - CICLO DE VIDA DE SERVICIOS</h1>
        <p class="text-xs sm:text-sm text-gray-400">Control visual de bahías de trabajo, transición de estados y auditoría inmutable de reprocesos</p>
      </div>

      <button 
        @click="cargarOrdenes" 
        class="bg-gray-800 hover:bg-gray-700 text-yellow-400 border border-gray-700 font-bold px-3 py-1.5 rounded-lg text-xs transition flex items-center space-x-1.5 self-start md:self-auto">
        <span>🔄</span>
        <span>Actualizar Tablero</span>
      </button>
    </div>

    <!-- COLUMNAS KANBAN -->
    <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-3.5">
      
      <div 
        v-for="col in columnas" 
        :key="col.estado" 
        class="bg-[#111827] border border-gray-800 rounded-xl flex flex-col min-h-[550px] shadow-lg">
        
        <!-- ENCABEZADO DE COLUMNA -->
        <div class="p-3 border-b border-gray-800 flex justify-between items-center bg-[#161F30] rounded-t-xl">
          <span class="font-extrabold text-xs text-gray-200 tracking-wide">{{ col.titulo }}</span>
          <span class="text-xs font-mono font-bold px-2 py-0.5 rounded-full" :class="col.badgeClass">
            {{ obtenerOtsPorEstado(col.estado).length }}
          </span>
        </div>

        <!-- LISTA DE TARJETAS DE LA COLUMNA -->
        <div class="p-2.5 flex-1 space-y-2.5 overflow-y-auto max-h-[700px]">
          <div 
            v-for="ot in obtenerOtsPorEstado(col.estado)" 
            :key="ot.idOrden"
            class="bg-[#1F2937] border border-gray-700/80 rounded-lg p-3 shadow hover:border-yellow-400 transition text-xs">
            
            <!-- HEADER DE TARJETA -->
            <div class="flex justify-between items-start mb-1.5">
              <span class="font-mono font-black text-[#FFCD11]">{{ ot.codigoOt }}</span>
              <span class="text-[10px] font-bold px-1.5 py-0.5 rounded uppercase" :class="obtenerBadgePrioridad(ot.prioridad)">
                {{ ot.prioridad?.replace('_DOWN_MACHINE', '') }}
              </span>
            </div>

            <!-- CUERPO DE TARJETA -->
            <div class="text-white font-bold">{{ ot.maquinaria?.codigoFlota || '-' }}</div>
            <div class="text-[11px] text-gray-400 truncate">{{ ot.tipoMantenimiento }}</div>
            <div class="text-[10px] text-gray-500 mt-1 line-clamp-2">{{ ot.fallaReportada }}</div>

            <!-- ACCIONES DE TRANSICIÓN -->
            <div class="mt-3 pt-2 border-t border-gray-700/60 flex items-center justify-between">
              
              <!-- BOTÓN DE RECHAZO A RETRABAJO (QC A REPARACIÓN) -->
              <button 
                v-if="ot.estado === 'CONTROL_CALIDAD' || ot.estado === 'EN_REPARACION'"
                @click="abrirModalRetrabajo(ot)"
                class="text-[10px] bg-red-900/40 hover:bg-red-900/80 text-red-300 border border-red-700/50 px-2 py-1 rounded font-bold transition">
                ↺ Rechazar QC
              </button>
              <span v-else></span>

              <!-- BOTÓN AVANZAR DE ESTADO -->
              <button 
                v-if="col.siguiente"
                @click="avanzarEstado(ot, col.siguiente)"
                class="text-[10px] bg-yellow-500 hover:bg-yellow-400 text-black px-2 py-1 rounded font-black transition flex items-center space-x-1 shadow">
                <span>{{ col.siguienteLabel }}</span>
                <span>→</span>
              </button>

              <span v-else class="text-[10px] text-emerald-400 font-bold">
                ✓ Concluida
              </span>

            </div>

          </div>

          <div v-if="obtenerOtsPorEstado(col.estado).length === 0" class="text-center py-8 text-gray-500 text-xs">
            Sin órdenes
          </div>
        </div>

      </div>

    </div>

    <!-- MODAL MANDATORIO DE RECHAZO POR RETRABAJO (RF-07 / RF-14) -->
    <div v-if="otParaRetrabajo" class="fixed inset-0 z-50 bg-black/80 flex items-center justify-center p-4">
      <div class="bg-[#111827] border-2 border-red-500/80 rounded-xl max-w-md w-full p-6 shadow-2xl relative">
        <div class="flex items-center space-x-2 text-red-400 font-black text-base mb-2">
          <span>⚠️</span>
          <span>RECHAZO DE CALIDAD / RETRABAJO MANDATORIO</span>
        </div>
        <p class="text-xs text-gray-300 mb-4">
          Orden: <span class="text-yellow-400 font-mono font-bold">{{ otParaRetrabajo.codigoOt }}</span> ({{ otParaRetrabajo.maquinaria?.codigoFlota }}).
          Debe ingresar el motivo técnico del rechazo (mínimo 10 caracteres) para su registro en la bitácora inmutable de auditoría (RF-14).
        </p>

        <form @submit.prevent="confirmarRetrabajo" class="space-y-4">
          <div>
            <label class="block text-xs font-semibold text-gray-300 mb-1">
              Justificación Técnica del Retrabajo *
            </label>
            <textarea 
              v-model="motivoRetrabajo" 
              rows="3" 
              placeholder="Ej: Fuga persistente de aceite en manguera de alta presión tras prueba de presión en vacío..."
              required
              class="w-full bg-[#1F2937] border border-gray-700 rounded-lg p-2.5 text-xs text-white focus:outline-none focus:border-red-400"
            ></textarea>
            <div class="flex justify-between text-[11px] mt-1">
              <span :class="motivoRetrabajo.trim().length >= 10 ? 'text-emerald-400' : 'text-red-400'">
                Longitud: {{ motivoRetrabajo.trim().length }} / 10 caracteres mín.
              </span>
              <span class="text-gray-500">Obligatorio según Pauta CAT</span>
            </div>
          </div>

          <div class="flex justify-end space-x-2 pt-2 border-t border-gray-800">
            <button 
              type="button" 
              @click="otParaRetrabajo = null" 
              class="px-3.5 py-1.5 bg-gray-800 text-gray-300 rounded text-xs font-semibold">
              Cancelar
            </button>
            <button 
              type="submit" 
              :disabled="motivoRetrabajo.trim().length < 10 || procesandoRetrabajo"
              class="px-4 py-1.5 bg-red-600 hover:bg-red-500 disabled:bg-gray-700 disabled:text-gray-500 text-white font-bold rounded text-xs shadow transition">
              {{ procesandoRetrabajo ? 'Registrando...' : 'Confirmar Retrabajo en Auditoría' }}
            </button>
          </div>
        </form>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/client'

const ordenes = ref([])
const otParaRetrabajo = ref(null)
const motivoRetrabajo = ref('')
const procesandoRetrabajo = ref(false)

const columnas = [
  { estado: 'SOLICITADA', titulo: 'SOLICITADA', badgeClass: 'bg-gray-850 text-gray-300', siguiente: 'ASIGNADA', siguienteLabel: 'Asignar' },
  { estado: 'ASIGNADA', titulo: 'ASIGNADA', badgeClass: 'bg-blue-900/60 text-blue-300', siguiente: 'EN_DIAGNOSTICO', siguienteLabel: 'Iniciar ATS' },
  { estado: 'EN_DIAGNOSTICO', titulo: 'DIAGNÓSTICO', badgeClass: 'bg-yellow-900/60 text-yellow-300', siguiente: 'EN_REPARACION', siguienteLabel: 'A Reparar' },
  { estado: 'EN_REPARACION', titulo: 'REPARACIÓN', badgeClass: 'bg-orange-900/60 text-orange-300', siguiente: 'CONTROL_CALIDAD', siguienteLabel: 'A Control QC' },
  { estado: 'CONTROL_CALIDAD', titulo: 'CONTROL QC', badgeClass: 'bg-purple-900/60 text-purple-300', siguiente: 'CERRADA', siguienteLabel: 'Aprobar QC' },
  { estado: 'CERRADA', titulo: 'CERRADA', badgeClass: 'bg-emerald-900/60 text-emerald-300', siguiente: null, siguienteLabel: '' }
]

const cargarOrdenes = async () => {
  try {
    const res = await api.get('/ordenes-servicio')
    ordenes.value = res.data
  } catch (err) {
    console.error('Error cargando kanban:', err)
  }
}

const obtenerOtsPorEstado = (estado) => {
  return ordenes.value.filter(o => o.estado === estado)
}

const avanzarEstado = async (ot, nuevoEstado) => {
  if (ot.estado === 'SOLICITADA' && nuevoEstado !== 'ASIGNADA') {
    alert('La orden debe ser asignada a una cuadrilla técnica antes de iniciar diagnóstico.')
    return
  }
  try {
    await api.patch(`/ordenes-servicio/${ot.idOrden}/estado`, {
      nuevoEstado: nuevoEstado,
      motivoRetrabajo: 'Avance estándar en tablero Kanban'
    })
    ot.estado = nuevoEstado
  } catch (err) {
    alert('Error al avanzar orden: ' + (err.response?.data?.mensaje || err.message))
  }
}

const abrirModalRetrabajo = (ot) => {
  otParaRetrabajo.value = ot
  motivoRetrabajo.value = ''
}

const confirmarRetrabajo = async () => {
  if (motivoRetrabajo.value.trim().length < 10) return
  procesandoRetrabajo.value = true
  try {
    // Mandamos a estado EN_REPARACION con comentario de retrabajo registrado en auditoría (RF-14)
    await api.patch(`/ordenes-servicio/${otParaRetrabajo.value.idOrden}/estado`, {
      nuevoEstado: 'EN_REPARACION',
      motivoRetrabajo: 'RETRABAJO QC: ' + motivoRetrabajo.value
    })
    otParaRetrabajo.value.estado = 'EN_REPARACION'
    otParaRetrabajo.value = null
    await cargarOrdenes()
  } catch (err) {
    alert('Error registrando retrabajo: ' + (err.response?.data?.mensaje || err.message))
  } finally {
    procesandoRetrabajo.value = false
  }
}

const obtenerBadgePrioridad = (p) => {
  if (p?.includes('CRITICA')) return 'bg-red-500/20 text-red-400'
  if (p === 'ALTA') return 'bg-amber-500/20 text-amber-400'
  return 'bg-blue-500/20 text-blue-400'
}

onMounted(() => {
  cargarOrdenes()
})
</script>
