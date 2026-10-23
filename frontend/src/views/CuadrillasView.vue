<template>
  <div class="space-y-6">
    <!-- ENCABEZADO -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Caterpillar Service Pro</span>
          <span class="text-xs text-blue-400 font-semibold">Régimen Minero Atípico 14x7</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">ADMINISTRACIÓN DE CUADRILLAS TÉCNICAS Y TURNOS 14X7</h1>
        <p class="text-xs sm:text-sm text-gray-400">Escalafón oficial de habilidades de fábrica, control de fatiga (tope 192 hrs) y disponibilidad en tiempo real</p>
      </div>

      <div class="flex items-center space-x-2 bg-gray-800/80 px-4 py-2 rounded-lg border border-gray-700 text-xs text-gray-300">
        <span>Tope legal mensual:</span>
        <span class="font-bold text-yellow-400">192.0 hrs</span>
      </div>
    </div>

    <!-- FILTROS -->
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 bg-[#111827] p-4 rounded-xl border border-gray-800">
      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Filtrar por Certificación CAT</label>
        <select v-model="filtroNivel" class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todas">Todas las Certificaciones</option>
          <option value="Master CAT">Master CAT (Nivel 4 Cúspide)</option>
          <option value="Specialist">Specialist (Nivel 3 Sistemas Complejos)</option>
          <option value="Level 2">Level 2 (Técnico Hidráulico/Mecánico)</option>
          <option value="Level 1">Level 1 (Técnico de Servicio)</option>
          <option value="Foundational">Foundational (Fundamentos de Servicio)</option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Guardia Minera 14x7</label>
        <select v-model="filtroTurno" class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todos">Todos los Regímenes</option>
          <option value="14x7_DIURNO">14x7 Diurno (07:00 - 19:00 hrs)</option>
          <option value="14x7_NOCTURNO">14x7 Nocturno (19:00 - 07:00 hrs)</option>
        </select>
      </div>
    </div>

    <!-- LISTADO DE TÉCNICOS -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-lg">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-300">
          <thead class="bg-[#161F30] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-800 tracking-wider">
            <tr>
              <th class="py-3.5 px-4">Fotocheck</th>
              <th class="py-3.5 px-4">Nombre del Especialista</th>
              <th class="py-3.5 px-4">Especialidad Principal</th>
              <th class="py-3.5 px-4 text-center">Certificación CAT</th>
              <th class="py-3.5 px-4">Régimen Guardia</th>
              <th class="py-3.5 px-4 text-center">Horas Mes</th>
              <th class="py-3.5 px-4 text-center">Estado Disponibilidad</th>
              <th class="py-3.5 px-4 text-center">Conmutar</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="tecnicosFiltrados.length === 0">
              <td colspan="8" class="text-center py-8 text-gray-400">No se encontraron técnicos registrados.</td>
            </tr>
            <tr v-for="t in tecnicosFiltrados" :key="t.idTecnico" class="hover:bg-gray-800/40 transition">
              
              <!-- FOTOCHECK -->
              <td class="py-3.5 px-4 font-mono font-bold text-[#FFCD11]">{{ t.codigoEmpleado || t.fotocheck }}</td>

              <!-- NOMBRE -->
              <td class="py-3.5 px-4 text-white font-semibold">
                {{ t.usuario ? `${t.usuario.nombres} ${t.usuario.apellidos}` : 'Técnico Especialista' }}
              </td>

              <!-- ESPECIALIDAD -->
              <td class="py-3.5 px-4 text-gray-300 text-xs">{{ t.especialidadPrincipal || 'Mantenimiento General' }}</td>

              <!-- CERTIFICACIÓN BADGE -->
              <td class="py-3.5 px-4 text-center">
                <span :class="obtenerBadgeNivel(t.nivelCertificacion)" class="px-2.5 py-1 rounded text-xs font-black uppercase inline-block border">
                  {{ t.nivelCertificacion }}
                </span>
              </td>

              <!-- REGIMEN -->
              <td class="py-3.5 px-4 text-xs font-mono text-gray-200">{{ t.regimenGuardia || '14x7_DIURNO' }}</td>

              <!-- HORAS ACUMULADAS -->
              <td class="py-3.5 px-4 text-center font-mono">
                <span :class="t.horasAcumuladasMes > 180 ? 'text-red-400 font-bold' : 'text-yellow-400 font-semibold'">
                  {{ t.horasAcumuladasMes ?? 0 }} / 192 hrs
                </span>
              </td>

              <!-- ESTADO DISPONIBILIDAD -->
              <td class="py-3.5 px-4 text-center">
                <span :class="t.estadoDisponibilidad === 'DISPONIBLE' ? 'bg-emerald-500/20 text-emerald-400 border border-emerald-500/40' : 'bg-blue-500/20 text-blue-400 border border-blue-500/40'" class="px-2.5 py-0.5 rounded-full text-xs font-bold inline-block">
                  {{ t.estadoDisponibilidad || 'DISPONIBLE' }}
                </span>
              </td>

              <!-- CONMUTAR ESTADO -->
              <td class="py-3.5 px-4 text-center">
                <button 
                  @click="conmutarEstado(t)"
                  class="bg-gray-800 hover:bg-gray-700 text-xs text-yellow-400 border border-gray-700 px-2.5 py-1 rounded transition font-bold">
                  {{ t.estadoDisponibilidad === 'DISPONIBLE' ? 'Asignar' : 'Liberar' }}
                </button>
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

const tecnicos = ref([])
const filtroNivel = ref('Todas')
const filtroTurno = ref('Todos')

const cargarTecnicos = async () => {
  try {
    const res = await api.get('/tecnicos')
    tecnicos.value = res.data
  } catch (err) {
    console.error('Error cargando técnicos:', err)
  }
}

const tecnicosFiltrados = computed(() => {
  return tecnicos.value.filter(t => {
    if (filtroNivel.value !== 'Todas' && t.nivelCertificacion !== filtroNivel.value) return false
    if (filtroTurno.value !== 'Todos' && t.regimenGuardia !== filtroTurno.value) return false
    return true
  })
})

const obtenerBadgeNivel = (nivel) => {
  switch (nivel) {
    case 'Master CAT': return 'bg-yellow-500/20 text-yellow-400 border-yellow-500/50'
    case 'Specialist': return 'bg-purple-500/20 text-purple-300 border-purple-500/50'
    case 'Level 2': return 'bg-blue-500/20 text-blue-300 border-blue-500/50'
    case 'Level 1': return 'bg-cyan-500/20 text-cyan-300 border-cyan-500/50'
    default: return 'bg-gray-500/20 text-gray-300 border-gray-600'
  }
}

const conmutarEstado = async (tecnico) => {
  const nuevo = tecnico.estadoDisponibilidad === 'DISPONIBLE' ? 'EN_SERVICIO' : 'DISPONIBLE'
  try {
    await api.patch(`/tecnicos/${tecnico.idTecnico}/estado?nuevoEstado=${nuevo}`)
    tecnico.estadoDisponibilidad = nuevo
  } catch (err) {
    console.error('Error cambiando estado técnico:', err)
  }
}

onMounted(() => {
  cargarTecnicos()
})
</script>
