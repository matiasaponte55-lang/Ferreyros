<template>
  <div class="space-y-6">
    <!-- ENCABEZADO -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Mesa de Control</span>
          <span class="text-xs text-yellow-400 font-semibold">Trazabilidad End-to-End (RF-05 / RF-06)</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">BANDEJA GENERAL Y CONTROL DE ÓRDENES DE TRABAJO</h1>
        <p class="text-xs sm:text-sm text-gray-400">Supervisión centralizada del flujo operativo de servicios preventivos y correctivos</p>
      </div>

      <router-link 
        to="/ordenes/nueva" 
        class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold px-4 py-2 rounded-lg text-sm shadow transition flex items-center space-x-1.5 self-start md:self-auto">
        <span>➕</span>
        <span>Crear Nueva OT</span>
      </router-link>
    </div>

    <!-- FILTROS MULTIFACTORIALES -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 bg-[#111827] p-4 rounded-xl border border-gray-800">
      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Buscar por Código, Máquina o Cliente</label>
        <input 
          v-model="filtroBuscar" 
          type="text" 
          placeholder="Ej: OT-2026, 797F, Cerro Verde..." 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]"
        />
      </div>

      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Estado de Ciclo de Vida</label>
        <select 
          v-model="filtroEstado" 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todas">Todos los Estados</option>
          <option value="SOLICITADA">Solicitada (Pendiente)</option>
          <option value="ASIGNADA">Asignada en Matriz</option>
          <option value="EN_DIAGNOSTICO">En Diagnóstico / ATS</option>
          <option value="EN_REPARACION">En Reparación</option>
          <option value="CONTROL_CALIDAD">Control de Calidad (QC)</option>
          <option value="CERRADA">Cerrada / Liquidada</option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Prioridad / Severidad</label>
        <select 
          v-model="filtroPrioridad" 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todas">Todas las Prioridades</option>
          <option value="CRITICA_DOWN_MACHINE">⚡ Crítica (Down Machine)</option>
          <option value="ALTA">Alta</option>
          <option value="MEDIA">Media</option>
          <option value="BAJA">Baja</option>
        </select>
      </div>
    </div>

    <!-- TABLA DE ÓRDENES -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-lg">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-300">
          <thead class="bg-[#161F30] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-800 tracking-wider">
            <tr>
              <th class="py-3.5 px-4">Nro OT</th>
              <th class="py-3.5 px-4">Maquinaria CAT</th>
              <th class="py-3.5 px-4">Cliente Titular</th>
              <th class="py-3.5 px-4">Tipo Servicio</th>
              <th class="py-3.5 px-4 text-center">Severidad</th>
              <th class="py-3.5 px-4 text-center">Estado</th>
              <th class="py-3.5 px-4 text-center">Acciones Operativas</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="cargando">
              <td colspan="7" class="text-center py-8 text-gray-400">Cargando bandeja de órdenes...</td>
            </tr>
            <tr v-else-if="ordenesFiltradas.length === 0">
              <td colspan="7" class="text-center py-8 text-gray-400">No hay órdenes que coincidan con los filtros.</td>
            </tr>
            <tr v-for="ot in ordenesFiltradas" :key="ot.idOrden" class="hover:bg-gray-800/40 transition">
              
              <!-- NRO OT -->
              <td class="py-3.5 px-4 font-mono font-bold text-[#FFCD11] whitespace-nowrap">
                {{ ot.codigoOt }}
              </td>

              <!-- MAQUINARIA -->
              <td class="py-3.5 px-4">
                <div class="text-white font-semibold">{{ ot.maquinaria?.codigoFlota || '-' }}</div>
                <div class="text-[11px] text-gray-400 font-mono">{{ ot.maquinaria?.numeroSeriePin || '-' }}</div>
              </td>

              <!-- CLIENTE -->
              <td class="py-3.5 px-4 text-xs text-gray-300">
                {{ ot.maquinaria?.cliente?.razonSocial || '-' }}
              </td>

              <!-- TIPO -->
              <td class="py-3.5 px-4 text-xs font-semibold text-gray-200">
                {{ ot.tipoMantenimiento }}
              </td>

              <!-- SEVERIDAD -->
              <td class="py-3.5 px-4 text-center">
                <span :class="obtenerBadgePrioridad(ot.prioridad)" class="px-2 py-0.5 rounded text-[11px] font-black uppercase inline-block border">
                  {{ ot.prioridad }}
                </span>
              </td>

              <!-- ESTADO -->
              <td class="py-3.5 px-4 text-center">
                <span :class="obtenerBadgeEstado(ot.estado)" class="px-2.5 py-1 rounded text-xs font-bold uppercase inline-block">
                  {{ ot.estado }}
                </span>
              </td>

              <!-- ACCIONES OPERATIVAS -->
              <td class="py-3.5 px-4 text-center whitespace-nowrap space-x-1">
                <router-link 
                  to="/dispatcher" 
                  class="bg-blue-600/20 hover:bg-blue-600/40 text-blue-300 border border-blue-500/30 px-2 py-1 rounded text-xs font-semibold transition inline-block">
                  Despacho
                </router-link>
                <router-link 
                  :to="`/inspeccion/${ot.idOrden}`" 
                  class="bg-yellow-500/20 hover:bg-yellow-500/40 text-yellow-300 border border-yellow-500/30 px-2 py-1 rounded text-xs font-semibold transition inline-block">
                  ATS Tablet
                </router-link>
                <router-link 
                  :to="`/liquidacion/${ot.idOrden}`" 
                  class="bg-emerald-600/20 hover:bg-emerald-600/40 text-emerald-300 border border-emerald-500/30 px-2 py-1 rounded text-xs font-semibold transition inline-block">
                  PDF Final
                </router-link>
              </td>

            </tr>
          </tbody>
        </table>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/client'

const ordenes = ref([])
const cargando = ref(true)

const filtroBuscar = ref('')
const filtroEstado = ref('Todas')
const filtroPrioridad = ref('Todas')

const cargarOrdenes = async () => {
  cargando.value = true
  try {
    const res = await api.get('/ordenes-servicio')
    ordenes.value = res.data
  } catch (err) {
    console.error('Error cargando órdenes:', err)
  } finally {
    cargando.value = false
  }
}

const ordenesFiltradas = computed(() => {
  return ordenes.value.filter(o => {
    if (filtroEstado.value !== 'Todas' && o.estado !== filtroEstado.value) return false
    if (filtroPrioridad.value !== 'Todas' && o.prioridad !== filtroPrioridad.value) return false
    if (filtroBuscar.value.trim() !== '') {
      const q = filtroBuscar.value.toLowerCase().trim()
      const matchCod = o.codigoOt?.toLowerCase().includes(q)
      const matchFalla = o.fallaReportada?.toLowerCase().includes(q)
      const matchFlota = o.maquinaria?.codigoFlota?.toLowerCase().includes(q)
      const matchCli = o.maquinaria?.cliente?.razonSocial?.toLowerCase().includes(q)
      return matchCod || matchFalla || matchFlota || matchCli
    }
    return true
  })
})

const obtenerBadgePrioridad = (p) => {
  if (p?.includes('CRITICA')) return 'bg-red-500/20 text-red-400 border-red-500/40 animate-pulse'
  if (p === 'ALTA') return 'bg-amber-500/20 text-amber-400 border-amber-500/40'
  if (p === 'MEDIA') return 'bg-blue-500/20 text-blue-400 border-blue-500/40'
  return 'bg-gray-500/20 text-gray-400 border-gray-600'
}

const obtenerBadgeEstado = (e) => {
  switch (e) {
    case 'SOLICITADA': return 'bg-gray-700 text-gray-300'
    case 'ASIGNADA': return 'bg-blue-900/60 text-blue-300 border border-blue-600'
    case 'EN_DIAGNOSTICO': return 'bg-yellow-900/60 text-yellow-300 border border-yellow-600'
    case 'EN_REPARACION': return 'bg-orange-900/60 text-orange-300 border border-orange-600'
    case 'CONTROL_CALIDAD': return 'bg-purple-900/60 text-purple-300 border border-purple-600'
    case 'CERRADA':
    case 'LIQUIDADA': return 'bg-emerald-900/60 text-emerald-300 border border-emerald-600'
    default: return 'bg-gray-800 text-gray-400'
  }
}

onMounted(() => {
  cargarOrdenes()
})
</script>
