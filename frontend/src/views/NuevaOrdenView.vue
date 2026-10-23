<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <!-- ENCABEZADO -->
    <div class="bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div class="flex items-center space-x-2">
        <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Módulo Despacho</span>
        <span class="text-xs text-yellow-400 font-semibold">RF-02 / RF-03 Regla Horómetro Ascendente</span>
      </div>
      <h1 class="text-xl sm:text-2xl font-black text-white mt-1">FERREYROS CAT - APERTURA Y TIPIFICACIÓN DE ORDEN DE TRABAJO (OT)</h1>
      <p class="text-xs sm:text-sm text-gray-400">Digitalización de solicitudes de servicio, validación en tiempo real y asignación a cronograma</p>
    </div>

    <!-- MENSAJES DE ALERTA -->
    <div v-if="errorHorometro" class="bg-red-900/40 border border-red-500 text-red-200 text-xs sm:text-sm p-4 rounded-xl flex items-center space-x-3 shadow-lg animate-pulse">
      <span class="text-2xl">⚠️</span>
      <div>
        <p class="font-extrabold">ERROR DE INTEGRIDAD EN HORÓMETRO (RF-03):</p>
        <p>{{ errorHorometro }}</p>
      </div>
    </div>

    <div v-if="mensajeExito" class="bg-emerald-900/40 border border-emerald-500 text-emerald-200 text-xs sm:text-sm p-4 rounded-xl flex items-center space-x-3 shadow-lg">
      <span class="text-2xl">✅</span>
      <div>
        <p class="font-extrabold">ORDEN REGISTRADA SATISFACTORIAMENTE:</p>
        <p>{{ mensajeExito }}</p>
      </div>
    </div>

    <form @submit.prevent="guardarOrden" class="space-y-6">
      
      <!-- SECCIÓN 1: IDENTIFICACIÓN PREDICTIVA DE MAQUINARIA CAT -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
        <h2 class="text-xs sm:text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2">
          1. Identificación Predictiva de Maquinaria CAT (Modelo / Flota)
        </h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-semibold text-gray-300 mb-1">Seleccionar Maquinaria Pesada</label>
            <select 
              v-model="maquinariaSeleccionadaId" 
              @change="alCambiarMaquinaria"
              required
              class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
              <option :value="null" disabled>-- Seleccione un equipo de la flota --</option>
              <option v-for="m in maquinarias" :key="m.idMaquinaria" :value="m.idMaquinaria">
                {{ m.codigoFlota }} - {{ m.modelo?.nombreComercial || m.modeloCat }} ({{ m.numeroSeriePin }})
              </option>
            </select>
          </div>

          <div>
            <label class="block text-xs font-semibold text-gray-300 mb-1">Cliente Titular / Faena Asignada</label>
            <input 
              type="text" 
              readonly 
              :value="maquinariaActual?.cliente?.razonSocial || 'Seleccione maquinaria'"
              class="w-full bg-[#161F30] border border-gray-800 rounded-lg px-3 py-2 text-sm text-gray-400 cursor-not-allowed"
            />
          </div>
        </div>
      </div>

      <!-- SECCIÓN 2: CONTROL Y VALIDACIÓN DE HORÓMETRO ASCENDENTE (RF-03) -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
        <h2 class="text-xs sm:text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2">
          2. Control y Validación de Horómetro Ascendente (RF-03)
        </h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-semibold text-gray-400 mb-1">Horómetro Actual Histórico (Telemetría Satelital)</label>
            <div class="w-full bg-[#161F30] border border-gray-800 rounded-lg px-3 py-2.5 text-base font-mono font-bold text-gray-400 flex justify-between items-center">
              <span>{{ Number(maquinariaActual?.horometroAcumulado || 0).toFixed(1) }} hrs</span>
              <span class="text-xs text-emerald-400 font-sans">✓ Verificado</span>
            </div>
          </div>

          <div>
            <label class="block text-xs font-semibold text-yellow-400 mb-1">Horómetro de Entrada al Servicio (Nuevo)</label>
            <input 
              v-model.number="form.horometroRegistro" 
              type="number" 
              step="0.1" 
              required
              placeholder="Debe ser mayor o igual al actual"
              class="w-full bg-[#1F2937] border border-yellow-500/50 rounded-lg px-3 py-2.5 text-base font-mono font-bold text-white focus:outline-none focus:border-[#FFCD11]"
            />
            <p class="text-[11px] text-gray-400 mt-1">El trigger relacional en PostgreSQL abortará la transacción si el valor ingresado retrocede.</p>
          </div>
        </div>
      </div>

      <!-- SECCIÓN 3: TIPIFICACIÓN DEL SERVICIO Y PRIORIDAD -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
        <h2 class="text-xs sm:text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2">
          3. Tipificación del Servicio y Prioridad Operativa
        </h2>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-semibold text-gray-300 mb-1">Tipo de Mantenimiento Estandarizado</label>
            <select v-model="form.tipoMantenimiento" class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
              <option value="PM1_250H">Preventivo PM1 (250 horas) - Fluidos y Filtros</option>
              <option value="PM2_500H">Preventivo PM2 (500 horas) - Hidráulica y Válvulas</option>
              <option value="PM3_1000H">Preventivo PM3 (1000 horas) - Transmisión y Diferenciales</option>
              <option value="PM4_2000H">Preventivo PM4 (2000 horas) - Overhaul Parcial de Motor</option>
              <option value="CORRECTIVO">Mantenimiento Correctivo Programado</option>
              <option value="DIAGNOSTICO_FALLA">Diagnóstico de Falla por Código ECM</option>
            </select>
          </div>

          <div>
            <label class="block text-xs font-semibold text-gray-300 mb-1">Severidad / Criticidad de Despacho</label>
            <select v-model="form.prioridad" class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
              <option value="BAJA">Baja - Mantenimiento en Bahía Programada</option>
              <option value="MEDIA">Media - Desgaste Prematuro Monitoreado</option>
              <option value="ALTA">Alta - Riesgo Inminente de Parada</option>
              <option value="CRITICA_DOWN_MACHINE">⚡ CRÍTICA - Down Machine / Parada de Planta</option>
            </select>
          </div>
        </div>
      </div>

      <!-- SECCIÓN 4: SUBSISTEMAS Y DESCRIPCIÓN TÉCNICA -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
        <h2 class="text-xs sm:text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2">
          4. Descripción Técnica de la Falla y Requerimiento de Taller
        </h2>

        <div>
          <label class="block text-xs font-semibold text-gray-300 mb-1">Detalle del Requerimiento o Falla Notificada</label>
          <textarea 
            v-model="form.fallaReportada" 
            rows="3" 
            required
            placeholder="Describa los síntomas reportados por telemetría o el operador de campo..."
            class="w-full bg-[#1F2937] border border-gray-700 rounded-lg p-3 text-sm text-white focus:outline-none focus:border-[#FFCD11]"
          ></textarea>
        </div>
      </div>

      <!-- BOTONES DE ACCIÓN -->
      <div class="flex items-center justify-end space-x-3 pt-2">
        <router-link to="/ordenes" class="px-5 py-2.5 rounded-lg bg-gray-800 hover:bg-gray-700 text-gray-300 font-semibold text-sm transition">
          Cancelar
        </router-link>
        <button 
          type="submit" 
          :disabled="enviando"
          class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold px-6 py-2.5 rounded-lg text-sm shadow-lg hover:shadow-yellow-500/20 transition flex items-center space-x-2">
          <span>{{ enviando ? 'Registrando...' : 'Guardar y Despachar a Matriz' }}</span>
          <span>→</span>
        </button>
      </div>

    </form>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '../api/client'

const router = useRouter()
const route = useRoute()

const maquinarias = ref([])
const maquinariaSeleccionadaId = ref(null)
const enviando = ref(false)
const errorHorometro = ref('')
const mensajeExito = ref('')

const form = ref({
  horometroRegistro: null,
  tipoMantenimiento: 'PM2_500H',
  prioridad: 'ALTA',
  fallaReportada: ''
})

const maquinariaActual = computed(() => {
  return maquinarias.value.find(m => m.idMaquinaria === maquinariaSeleccionadaId.value) || null
})

const alCambiarMaquinaria = () => {
  if (maquinariaActual.value) {
    form.value.horometroRegistro = Number(maquinariaActual.value.horometroAcumulado || 0)
    errorHorometro.value = ''
  }
}

const cargarMaquinarias = async () => {
  try {
    const res = await api.get('/maquinarias')
    maquinarias.value = res.data

    // Si viene con parámetro ?pin= en la URL
    const pinQuery = route.query.pin
    if (pinQuery) {
      const encontrada = maquinarias.value.find(m => m.numeroSeriePin === pinQuery)
      if (encontrada) {
        maquinariaSeleccionadaId.value = encontrada.idMaquinaria
        alCambiarMaquinaria()
        return
      }
    }

    if (maquinarias.value.length > 0) {
      maquinariaSeleccionadaId.value = maquinarias.value[0].idMaquinaria
      alCambiarMaquinaria()
    }
  } catch (err) {
    console.error('Error cargando maquinarias:', err)
  }
}

const guardarOrden = async () => {
  errorHorometro.value = ''
  mensajeExito.value = ''

  if (!maquinariaActual.value) {
    alert('Seleccione una maquinaria')
    return
  }

  // VALIDACIÓN PERIMETRAL EN EL CLIENTE (Pantalla 5 / RF-03)
  const horoActual = Number(maquinariaActual.value.horometroAcumulado || 0)
  const horoNuevo = Number(form.value.horometroRegistro || 0)

  if (horoNuevo < horoActual) {
    errorHorometro.value = `El horómetro reportado (${horoNuevo} hrs) no puede ser inferior al acumulado histórico verificado (${horoActual} hrs). Verifique la lectura en la cabina del equipo.`
    return
  }

  enviando.value = true
  try {
    const res = await api.post('/ordenes-servicio', {
      idMaquinaria: maquinariaActual.value.idMaquinaria,
      numeroSeriePin: maquinariaActual.value.numeroSeriePin,
      horometroRegistro: horoNuevo,
      tipoMantenimiento: form.value.tipoMantenimiento,
      prioridad: form.value.prioridad,
      fallaReportada: form.value.fallaReportada,
      idSede: maquinariaActual.value.sedeActual?.idSede || 11
    })

    mensajeExito.value = `Se aperturó la orden de trabajo ${res.data.codigoOt} exitosamente. Redirigiendo a la matriz de despacho...`
    setTimeout(() => {
      router.push('/dispatcher')
    }, 1500)
  } catch (err) {
    errorHorometro.value = err.response?.data?.mensaje || 'Error al aperturar orden de trabajo.'
  } finally {
    enviando.value = false
  }
}

onMounted(() => {
  cargarMaquinarias()
})
</script>
