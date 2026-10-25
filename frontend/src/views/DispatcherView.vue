<template>
  <div class="space-y-6">
    <!-- ENCABEZADO DE PANTALLA -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Módulo Dispatcher</span>
          <span class="text-xs text-yellow-400 font-mono font-semibold">PostgreSQL GiST Anti-Overlap (RF-04)</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">MATRIZ DINÁMICA DE DESPACHO Y CONTROL DE SOLAPAMIENTOS</h1>
        <p class="text-xs sm:text-sm text-gray-400">Asignación horaria de cuadrillas de campo con validación de exclusión matemática de intervalos (tstzrange &&)</p>
      </div>

      <button 
        @click="abrirModalAsignacion(null, null)"
        class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold px-4 py-2 rounded-lg text-sm shadow transition flex items-center space-x-1.5 self-start md:self-auto">
        <span>➕</span>
        <span>Asignar Labor a Técnico</span>
      </button>
    </div>

    <!-- BANNER DE ALERTA DE CONFLICTO GIST (HTTP 409) -->
    <div v-if="alertaGist" class="bg-red-950/80 border-2 border-red-500 text-white p-5 rounded-xl shadow-2xl animate-bounce">
      <div class="flex items-start space-x-3">
        <span class="text-3xl">🚫</span>
        <div class="flex-1">
          <h3 class="text-base font-black text-red-300 uppercase tracking-wide">
            ¡ALERTA DE SOLAPAMIENTO GiST (Código 23P01 / RF-04)!
          </h3>
          <p class="text-sm font-semibold text-gray-200 mt-1">
            {{ alertaGist.mensaje }}
          </p>
          <div class="mt-2 text-xs bg-red-900/60 p-2.5 rounded border border-red-700/50 font-mono text-red-200">
            Regla de Negocio: No se permite la sobreasignación de un técnico en un mismo intervalo temporal de trabajo. La base de datos abortó la inserción mediante índice GiST EXCLUDE.
          </div>
          <button 
            @click="alertaGist = null" 
            class="mt-3 bg-red-600 hover:bg-red-500 text-white text-xs font-bold py-1.5 px-3 rounded shadow transition">
            Entendido / Cerrar Alerta
          </button>
        </div>
      </div>
    </div>

    <!-- MATRIZ HORARIA DE DESPACHO -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-xl">
      <div class="p-4 bg-[#161F30] border-b border-gray-800 flex justify-between items-center text-xs font-bold text-gray-300">
        <div class="flex items-center space-x-2">
          <span>📅 FECHA OPERATIVA:</span>
          <span class="text-yellow-400 font-mono text-sm">Hoy (Guardia Diurna 14x7)</span>
        </div>
        <div class="flex items-center space-x-4">
          <span class="flex items-center"><span class="w-2.5 h-2.5 bg-yellow-400 rounded mr-1.5"></span> Asignada / Programada</span>
          <span class="flex items-center"><span class="w-2.5 h-2.5 bg-gray-800 border border-gray-700 rounded mr-1.5"></span> Franja Libre</span>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-xs border-collapse">
          <thead>
            <tr class="bg-[#1F2937] text-gray-300 uppercase text-[11px] font-bold border-b border-gray-700">
              <th class="p-3.5 border-r border-gray-700 w-64">Técnico Especialista</th>
              <th v-for="slot in slotsHorarios" :key="slot.id" class="p-3.5 text-center border-r border-gray-700 min-w-[150px]">
                {{ slot.label }}
              </th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800">
            <tr v-if="tecnicos.length === 0">
              <td :colspan="slotsHorarios.length + 1" class="text-center py-12 text-gray-500 font-medium">
                No hay personal técnico registrado para despacho en este turno.
              </td>
            </tr>
            <tr v-for="tecnico in tecnicos" :key="tecnico.idTecnico" class="hover:bg-gray-850 transition">
              
              <!-- COLUMNA TÉCNICO -->
              <td class="p-3.5 border-r border-gray-800 bg-[#161F30]/40">
                <div class="font-bold text-white text-sm">
                  {{ tecnico.usuario ? `${tecnico.usuario.nombres} ${tecnico.usuario.apellidos}` : 'Especialista' }}
                </div>
                <div class="flex items-center space-x-1.5 mt-0.5">
                  <span class="text-yellow-400 font-mono font-bold">{{ tecnico.codigoEmpleado }}</span>
                  <span class="text-gray-500">•</span>
                  <span class="text-purple-300 font-semibold">{{ tecnico.nivelCertificacion }}</span>
                </div>
                <div class="text-[11px] text-gray-400 truncate">{{ tecnico.especialidadPrincipal }}</div>
              </td>

              <!-- FRANJAS HORARIAS DEL TÉCNICO -->
              <td 
                v-for="slot in slotsHorarios" 
                :key="slot.id" 
                class="p-2 border-r border-gray-800/80 align-top">
                
                <!-- SI TIENE ASIGNACIÓN EN ESTA FRANJA -->
                <div 
                  v-if="obtenerAsignacion(tecnico.idTecnico, slot)" 
                  class="bg-gradient-to-r from-yellow-500/20 to-amber-600/20 border-l-4 border-[#FFCD11] p-2.5 rounded shadow text-white hover:border-yellow-400 transition">
                  <div class="flex justify-between items-start">
                    <span class="font-mono font-black text-[#FFCD11] text-xs">
                      {{ (obtenerAsignacion(tecnico.idTecnico, slot).orden?.codigoOt || obtenerAsignacion(tecnico.idTecnico, slot).ordenServicio?.codigoOt || 'OT-2026') }}
                    </span>
                    <span class="text-[10px] bg-yellow-500/30 text-yellow-300 px-1 rounded font-bold">Líder</span>
                  </div>
                  <div class="text-xs font-semibold text-gray-200 mt-1 truncate">
                    {{ (obtenerAsignacion(tecnico.idTecnico, slot).orden?.maquinaria?.codigoFlota || obtenerAsignacion(tecnico.idTecnico, slot).ordenServicio?.maquinaria?.codigoFlota || 'CAT-Flota') }}
                  </div>
                  <div class="text-[10px] text-gray-400 mt-1 font-mono">
                    {{ slot.label }}
                  </div>
                </div>

                <!-- SI ESTÁ LIBRE: BOTÓN PARA ASIGNAR -->
                <div v-else class="h-full flex items-center justify-center p-1">
                  <button 
                    @click="abrirModalAsignacion(tecnico, slot)"
                    class="w-full h-12 rounded border border-dashed border-gray-700 hover:border-yellow-400 hover:bg-yellow-400/10 text-gray-500 hover:text-yellow-400 text-xs font-semibold transition flex items-center justify-center space-x-1">
                    <span>+ Asignar</span>
                  </button>
                </div>

              </td>

            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- MODAL DE ASIGNACIÓN -->
    <div v-if="showModal" class="fixed inset-0 z-50 bg-black/75 flex items-center justify-center p-4">
      <div class="bg-[#111827] border border-gray-800 rounded-xl max-w-md w-full p-6 shadow-2xl relative">
        <h2 class="text-base font-bold text-white mb-4 flex items-center justify-between">
          <span>Nueva Asignación de Franja Horaria</span>
          <button @click="showModal = false" class="text-gray-400 hover:text-white">✕</button>
        </h2>

        <div v-if="errorModal" class="mb-4 bg-red-900/40 border border-red-500 text-red-200 text-xs p-3 rounded-lg flex items-center space-x-2">
          <span>⚠️</span>
          <span>{{ errorModal }}</span>
        </div>

        <form @submit.prevent="guardarAsignacion" class="space-y-4 text-xs sm:text-sm">
          <div>
            <label class="block font-semibold text-gray-300 mb-1">Orden de Trabajo (OT)</label>
            <select v-model="formAsignar.idOrden" required class="form-input">
              <option :value="null" disabled>-- Seleccione OT --</option>
              <option v-for="o in ordenesDisponibles" :key="o.idOrden" :value="o.idOrden">
                {{ o.codigoOt }} - {{ o.maquinaria?.codigoFlota }} ({{ o.tipoMantenimiento }})
              </option>
            </select>
          </div>

          <div>
            <label class="block font-semibold text-gray-300 mb-1">Técnico Especialista Responsable</label>
            <select v-model="formAsignar.idTecnico" required class="form-input">
              <option :value="null" disabled>-- Seleccione Especialista --</option>
              <option v-for="t in tecnicos" :key="t.idTecnico" :value="t.idTecnico">
                {{ t.codigoEmpleado }} - {{ t.usuario ? `${t.usuario.nombres} ${t.usuario.apellidos}` : 'Técnico' }} ({{ t.nivelCertificacion }})
              </option>
            </select>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block font-semibold text-gray-300 mb-1">Hora Inicio</label>
              <input v-model="formAsignar.horaInicio" type="text" placeholder="09:00" required class="form-input font-mono" />
            </div>
            <div>
              <label class="block font-semibold text-gray-300 mb-1">Hora Fin</label>
              <input v-model="formAsignar.horaFin" type="text" placeholder="11:00" required class="form-input font-mono" />
            </div>
          </div>

          <p class="text-[11px] text-gray-400">
            * Al presionar guardar, el servidor ejecutará la verificación GiST contra Supabase PostgreSQL.
          </p>

          <div class="flex justify-end space-x-2 pt-3 border-t border-gray-800">
            <button type="button" @click="showModal = false" class="px-4 py-2 bg-gray-800 text-gray-300 rounded-lg text-xs font-semibold">Cancelar</button>
            <button type="submit" :disabled="guardando" class="px-4 py-2 bg-[#FFCD11] text-black font-extrabold rounded-lg text-xs shadow hover:bg-yellow-400">
              {{ guardando ? 'Validando...' : 'Confirmar Asignación' }}
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

const tecnicos = ref([])
const asignaciones = ref([])
const ordenesDisponibles = ref([])
const alertaGist = ref(null)
const showModal = ref(false)
const guardando = ref(false)
const errorModal = ref('')
const fechaFiltro = ref('')

const slotsHorarios = [
  { id: 's1', inicio: '07:00', fin: '09:00', label: '07:00 - 09:00' },
  { id: 's2', inicio: '09:00', fin: '11:00', label: '09:00 - 11:00' },
  { id: 's3', inicio: '11:00', fin: '13:00', label: '11:00 - 13:00' },
  { id: 's4', inicio: '13:00', fin: '15:00', label: '13:00 - 15:00' },
  { id: 's5', inicio: '15:00', fin: '17:00', label: '15:00 - 17:00' }
]

const formAsignar = ref({
  idOrden: null,
  idTecnico: null,
  horaInicio: '09:00',
  horaFin: '11:00'
})

const cargarDatos = async () => {
  try {
    const [resTec, resAsig, resOts] = await Promise.all([
      api.get('/tecnicos'),
      api.get('/asignaciones'),
      api.get('/ordenes-servicio')
    ])
    tecnicos.value = resTec.data
    asignaciones.value = resAsig.data
    ordenesDisponibles.value = resOts.data
  } catch (err) {
    console.error('Error cargando dispatcher:', err)
  }
}

const obtenerAsignacion = (idTecnico, slot) => {
  return asignaciones.value.find(a => {
    if (a.tecnico?.idTecnico !== idTecnico) return false
    const fecha = a.fechaInicio || a.fechaHoraInicio
    if (!fecha) return false

    const d = new Date(fecha)
    if (fechaFiltro.value) {
      const fechaAsignacion = d.toISOString().split('T')[0]
      if (fechaAsignacion !== fechaFiltro.value) return false
    }

    const slotInicioHora = parseInt(slot.inicio.split(':')[0])
    const slotFinHora = parseInt(slot.fin.split(':')[0])
    const horaLocal = d.getHours()

    return horaLocal >= slotInicioHora && horaLocal < slotFinHora
  })
}

const abrirModalAsignacion = (tecnico, slot) => {
  errorModal.value = ''
  formAsignar.value.idTecnico = tecnico ? tecnico.idTecnico : (tecnicos.value[0]?.idTecnico || null)
  formAsignar.value.idOrden = ordenesDisponibles.value[0]?.idOrden || null
  if (slot) {
    formAsignar.value.horaInicio = slot.inicio
    formAsignar.value.horaFin = slot.fin
  }
  showModal.value = true
}

const guardarAsignacion = async () => {
  alertaGist.value = null
  errorModal.value = ''

  if (!formAsignar.value.idOrden || Number(formAsignar.value.idOrden) <= 0) {
    errorModal.value = 'Validación de Orden: Debe seleccionar una Orden de Trabajo válida.'
    return
  }

  if (!formAsignar.value.idTecnico || Number(formAsignar.value.idTecnico) <= 0) {
    errorModal.value = 'Validación de Técnico: Debe seleccionar un Técnico Especialista responsable.'
    return
  }

  const timeRegex = /^([01]\d|2[0-3]):[0-5]\d$/
  if (!timeRegex.test(formAsignar.value.horaInicio) || !timeRegex.test(formAsignar.value.horaFin)) {
    errorModal.value = 'Validación de Formato: Las horas deben tener el formato exacto HH:mm (ej: 08:30, 14:00).'
    return
  }

  if (formAsignar.value.horaInicio >= formAsignar.value.horaFin) {
    errorModal.value = `Validación Cronológica: La hora de inicio (${formAsignar.value.horaInicio}) no puede ser igual o posterior a la hora de fin (${formAsignar.value.horaFin}).`
    return
  }

  guardando.value = true
  try {
    await api.post('/asignaciones', {
      idOrden: Number(formAsignar.value.idOrden),
      idTecnico: Number(formAsignar.value.idTecnico),
      horaInicio: formAsignar.value.horaInicio,
      horaFin: formAsignar.value.horaFin
    })
    showModal.value = false
    await cargarDatos()
  } catch (err) {
    // CAPTURA DEL ERROR 409 CONFLICT (SOLAPAMIENTO GIST RF-04)
    if (err.response && err.response.status === 409) {
      alertaGist.value = {
        mensaje: err.response.data.mensaje || 'Error 23P01: Técnico ocupado en la franja seleccionada.',
        codigoOtConflictiva: err.response.data.codigoOtConflictiva
      }
      showModal.value = false
    } else {
      alert('Error en asignación: ' + (err.response?.data?.mensaje || err.message))
    }
  } finally {
    guardando.value = false
  }
}

onMounted(() => {
  cargarDatos()
})
</script>

<style scoped>
.form-input {
  @apply w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11];
}
</style>
