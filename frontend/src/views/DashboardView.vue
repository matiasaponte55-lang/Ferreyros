<template>
  <div class="space-y-6">
    <!-- ENCABEZADO GERENCIAL -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">KPIs Estratégicos</span>
          <span class="text-xs text-yellow-400 font-semibold font-mono">Gestión de Flota & Acuerdos MARC (RF-11 / RF-12)</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">DASHBOARD GERENCIAL DE RENDIMIENTO Y SLAS MARC</h1>
        <p class="text-xs sm:text-sm text-gray-400">Supervisión en tiempo real de disponibilidad mecánica, MTTR y cumplimiento de penalidades contractuales</p>
      </div>

      <div class="flex flex-wrap items-center gap-2 self-start md:self-auto">
        <button 
          @click="abrirModalPPT"
          :disabled="cargando"
          class="bg-[#FFCD11] hover:bg-yellow-400 disabled:opacity-50 text-black font-black px-4 py-2.5 rounded-lg text-sm shadow-lg transition flex items-center space-x-2 border border-yellow-300 cursor-pointer">
          <span class="text-base">📊</span>
          <span>Exportar Presentación PPTX</span>
        </button>

        <button 
          @click="exportarReporte"
          :disabled="cargando"
          class="bg-gray-800 hover:bg-gray-700 disabled:opacity-50 text-gray-200 font-bold px-3.5 py-2.5 rounded-lg text-sm shadow border border-gray-700 transition flex items-center space-x-2 cursor-pointer">
          <span>📥</span>
          <span>CSV Flota</span>
        </button>
      </div>
    </div>

    <!-- MODAL DE SELECCIÓN HISTÓRICA DE PPTX -->
    <div v-if="mostrarModalPPT" class="fixed inset-0 bg-black/80 backdrop-blur-sm z-50 flex items-center justify-center p-4">
      <div class="bg-[#111827] border border-gray-700 w-full max-w-xl rounded-2xl shadow-2xl overflow-hidden animate-fade-in">
        
        <!-- ENCABEZADO DEL MODAL -->
        <div class="bg-[#161F30] border-b border-gray-800 p-5 flex items-center justify-between">
          <div class="flex items-center space-x-3">
            <div class="w-10 h-10 rounded-xl bg-[#FFCD11] flex items-center justify-center text-black font-black text-xl shadow-md">
              📊
            </div>
            <div>
              <h2 class="text-lg font-black text-white">EXPORTAR PRESENTACIÓN EJECUTIVA PPTX</h2>
              <p class="text-xs text-yellow-400 font-semibold font-mono">Consolidación Histórica y Acuerdos MARC (Caterpillar)</p>
            </div>
          </div>
          <button @click="cerrarModalPPT" class="text-gray-400 hover:text-white p-2 rounded-lg hover:bg-gray-800 transition">
            ✕
          </button>
        </div>

        <!-- CUERPO DEL MODAL -->
        <div class="p-6 space-y-5 text-sm">
          
          <!-- SELECTOR DE AÑO / PERIODO -->
          <div>
            <label class="block text-xs font-bold text-gray-300 uppercase tracking-wider mb-2">
              📅 Seleccionar Año / Periodo Histórico
            </label>
            <div class="grid grid-cols-2 gap-2.5">
              <button 
                type="button"
                @click="anioSeleccionado = '2026'"
                class="p-3 rounded-xl border text-left transition flex flex-col justify-between cursor-pointer"
                :class="anioSeleccionado === '2026' ? 'bg-yellow-500/10 border-yellow-400 text-yellow-300 shadow-md ring-1 ring-yellow-400' : 'bg-gray-800/60 border-gray-700 text-gray-300 hover:bg-gray-800'">
                <div class="flex items-center justify-between">
                  <span class="font-black text-base">2026 (Actual)</span>
                  <span class="text-[10px] bg-emerald-500/20 text-emerald-400 font-bold px-1.5 py-0.5 rounded border border-emerald-500/30">En Vivo</span>
                </div>
                <span class="text-xs text-gray-400 mt-1">Datos en tiempo real de la base de datos Supabase</span>
              </button>

              <button 
                type="button"
                @click="anioSeleccionado = '2025'"
                class="p-3 rounded-xl border text-left transition flex flex-col justify-between cursor-pointer"
                :class="anioSeleccionado === '2025' ? 'bg-yellow-500/10 border-yellow-400 text-yellow-300 shadow-md ring-1 ring-yellow-400' : 'bg-gray-800/60 border-gray-700 text-gray-300 hover:bg-gray-800'">
                <div class="flex items-center justify-between">
                  <span class="font-black text-base">2025</span>
                  <span class="text-[10px] bg-blue-500/20 text-blue-400 font-bold px-1.5 py-0.5 rounded border border-blue-500/30">Cierre Anual</span>
                </div>
                <span class="text-xs text-gray-400 mt-1">142 OTs, 96.8% disp. y overhauls mayores</span>
              </button>

              <button 
                type="button"
                @click="anioSeleccionado = '2024'"
                class="p-3 rounded-xl border text-left transition flex flex-col justify-between cursor-pointer"
                :class="anioSeleccionado === '2024' ? 'bg-yellow-500/10 border-yellow-400 text-yellow-300 shadow-md ring-1 ring-yellow-400' : 'bg-gray-800/60 border-gray-700 text-gray-300 hover:bg-gray-800'">
                <div class="flex items-center justify-between">
                  <span class="font-black text-base">2024</span>
                  <span class="text-[10px] bg-purple-500/20 text-purple-400 font-bold px-1.5 py-0.5 rounded border border-purple-500/30">Línea Base</span>
                </div>
                <span class="text-xs text-gray-400 mt-1">128 OTs, 95.2% disp. y matriz MARC inicial</span>
              </button>

              <button 
                type="button"
                @click="anioSeleccionado = 'MULTIANUAL'"
                class="p-3 rounded-xl border text-left transition flex flex-col justify-between cursor-pointer"
                :class="anioSeleccionado === 'MULTIANUAL' ? 'bg-yellow-500/10 border-yellow-400 text-yellow-300 shadow-md ring-1 ring-yellow-400' : 'bg-gray-800/60 border-gray-700 text-gray-300 hover:bg-gray-800'">
                <div class="flex items-center justify-between">
                  <span class="font-black text-base">Multianual</span>
                  <span class="text-[10px] bg-amber-500/20 text-amber-400 font-bold px-1.5 py-0.5 rounded border border-amber-500/30">2024 - 2026</span>
                </div>
                <span class="text-xs text-gray-400 mt-1">Matriz comparativa de evolución trianual</span>
              </button>
            </div>
          </div>

          <!-- FILTRO DE SEDE -->
          <div>
            <label class="block text-xs font-bold text-gray-300 uppercase tracking-wider mb-1.5">
              🏢 Alcance Geográfico / Taller Operativo
            </label>
            <select 
              v-model="sedeFiltro" 
              class="w-full bg-gray-800 border border-gray-700 text-white rounded-xl px-3.5 py-2.5 outline-none focus:border-yellow-400 text-sm">
              <option value="TODAS">Todas las Sedes y Faenas Mineras (Consolidado Nacional)</option>
              <option value="Centro de Reparación de Componentes Arequipa - La Joya">Sede Arequipa - CRC La Joya</option>
              <option value="Mina Cerro Verde - Taller Bahía 3">Mina Cerro Verde (Arequipa)</option>
              <option value="Mina Las Bambas - Taller Chalcobamba">Mina Las Bambas (Apurímac)</option>
              <option value="Mina Cuajone - Bahía Botiflaca">Mina Cuajone (Moquegua)</option>
            </select>
          </div>

          <!-- TARJETA INFORMATIVA DINÁMICA -->
          <div class="bg-gray-800/50 border border-gray-700/80 p-3.5 rounded-xl text-xs space-y-1">
            <div class="font-bold text-[#FFCD11] flex items-center space-x-1.5">
              <span>ℹ️</span>
              <span>Resumen del Periodo Seleccionado ({{ anioSeleccionado }}):</span>
            </div>
            <p v-if="anioSeleccionado === '2026'" class="text-gray-300">
              Se exportará la fotografía operativa en vivo: 15 órdenes de servicio, 7 maquinarias activas, 10 especialistas en turno 14x7 y log forense de auditoría desde Supabase PostgreSQL.
            </p>
            <p v-else-if="anioSeleccionado === '2025'" class="text-gray-300">
              Se compilará el informe de cierre anual 2025: 142 órdenes ejecutadas, disponibilidad anual promedio de 96.8% y 1,740 horas hombre facturadas sin penalidades contractuales.
            </p>
            <p v-else-if="anioSeleccionado === '2024'" class="text-gray-300">
              Se compilará la auditoría de línea base 2024: 128 órdenes cerradas, disponibilidad anual de 95.2% y cumplimiento inicial de acuerdos MARC.
            </p>
            <p v-else class="text-gray-300">
              Se generará la matriz ejecutiva trianual 2024-2026: comparativa de evolución de disponibilidad (+2.42% mejora), tiempos MTTR y horas hombre totales.
            </p>
          </div>

        </div>

        <!-- PIE DEL MODAL -->
        <div class="bg-[#161F30] border-t border-gray-800 p-4 flex items-center justify-end space-x-3">
          <button 
            type="button"
            @click="cerrarModalPPT"
            class="px-4 py-2.5 rounded-lg text-xs font-bold text-gray-300 hover:bg-gray-800 transition cursor-pointer">
            Cancelar
          </button>
          
          <button 
            type="button"
            @click="ejecutarDescargaPPT"
            :disabled="exportandoPPT"
            class="bg-[#FFCD11] hover:bg-yellow-400 disabled:opacity-50 text-black font-black px-5 py-2.5 rounded-lg text-xs shadow-lg transition flex items-center space-x-2 border border-yellow-300 cursor-pointer">
            <span v-if="exportandoPPT" class="w-3.5 h-3.5 border-2 border-black border-t-transparent rounded-full animate-spin"></span>
            <span v-else>🚀</span>
            <span>{{ exportandoPPT ? 'Generando Presentación...' : `Generar Presentación PPTX (${anioSeleccionado})` }}</span>
          </button>
        </div>

      </div>
    </div>

    <!-- NOTIFICACIÓN DE ÉXITO -->
    <div v-if="mensajeNotificacion" class="bg-emerald-950/80 border border-emerald-500/50 text-emerald-300 p-4 rounded-xl flex items-center justify-between shadow-lg animate-fade-in">
      <div class="flex items-center space-x-3">
        <span class="text-xl">✅</span>
        <span class="text-sm font-semibold">{{ mensajeNotificacion }}</span>
      </div>
      <button @click="mensajeNotificacion = ''" class="text-emerald-400 hover:text-white text-xs font-bold uppercase">Cerrar</button>
    </div>

    <!-- 4 TARJETAS KPI PRINCIPALES -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      
      <!-- 1. DISPONIBILIDAD MARC -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg relative overflow-hidden">
        <div class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Disponibilidad MARC Promedio</div>
        <div class="mt-2 flex items-baseline space-x-2">
          <div v-if="cargando" class="h-9 w-24 bg-gray-700/60 rounded animate-pulse"></div>
          <span v-else class="text-3xl font-black text-white">{{ metricas?.disponibilidadMarcPromedio ?? 0 }}%</span>
          <span 
            v-if="!cargando"
            class="text-xs font-bold px-2 py-0.5 rounded border"
            :class="(metricas?.disponibilidadMarcPromedio ?? 0) >= 92 ? 'text-emerald-400 bg-emerald-500/10 border-emerald-500/20' : 'text-amber-400 bg-amber-500/10 border-amber-500/20'">
            {{ (metricas?.disponibilidadMarcPromedio ?? 0) >= 92 ? 'Operativo ≥ SLA' : 'En Supervisión' }}
          </span>
        </div>
        <div class="mt-3 text-xs text-gray-500 flex justify-between">
          <span>Meta contractual:</span>
          <span class="font-bold text-gray-300">≥ 92.0%</span>
        </div>
        <div class="absolute bottom-0 left-0 right-0 h-1 bg-emerald-500"></div>
      </div>

      <!-- 2. MTTR -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg relative overflow-hidden">
        <div class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Tiempo Medio Reparación (MTTR)</div>
        <div class="mt-2 flex items-baseline space-x-2">
          <div v-if="cargando" class="h-9 w-24 bg-gray-700/60 rounded animate-pulse"></div>
          <span v-else class="text-3xl font-black text-white">{{ metricas?.mttrPromedioHoras ?? 0 }} hrs</span>
          <span 
            v-if="!cargando"
            class="text-xs font-bold text-blue-400 bg-blue-500/10 px-2 py-0.5 rounded border border-blue-500/20">
            {{ (metricas?.mttrPromedioHoras ?? 0) > 0 ? 'Horas Promedio' : 'Sin Paradas Cerradas' }}
          </span>
        </div>
        <div class="mt-3 text-xs text-gray-500 flex justify-between">
          <span>Límite contractual:</span>
          <span class="font-bold text-gray-300">&lt; 6.0 hrs</span>
        </div>
        <div class="absolute bottom-0 left-0 right-0 h-1 bg-yellow-400"></div>
      </div>

      <!-- 3. OCUPACIÓN CUADRILLAS -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg relative overflow-hidden">
        <div class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Ocupación Cuadrillas 14x7</div>
        <div class="mt-2 flex items-baseline space-x-2">
          <div v-if="cargando" class="h-9 w-24 bg-gray-700/60 rounded animate-pulse"></div>
          <span v-else class="text-3xl font-black text-white">{{ metricas?.ocupacionCuadrillas ?? 0 }}%</span>
          <span 
            v-if="!cargando"
            class="text-xs font-bold text-yellow-400 bg-yellow-500/10 px-2 py-0.5 rounded border border-yellow-500/20">
            {{ (metricas?.ocupacionCuadrillas ?? 0) > 0 ? 'Activo en Faena' : 'Disponible en Taller' }}
          </span>
        </div>
        <div class="mt-3 text-xs text-gray-500 flex justify-between">
          <span>Tope Fatiga Mensual:</span>
          <span class="font-bold text-gray-300">192.0 hrs</span>
        </div>
        <div class="absolute bottom-0 left-0 right-0 h-1 bg-blue-500"></div>
      </div>

      <!-- 4. HORAS FACTURABLES -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg relative overflow-hidden">
        <div class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Horas Facturables Mes</div>
        <div class="mt-2 flex items-baseline space-x-2">
          <div v-if="cargando" class="h-9 w-24 bg-gray-700/60 rounded animate-pulse"></div>
          <span v-else class="text-3xl font-black text-white">{{ metricas?.horasFacturables ?? 0 }} hrs</span>
          <span 
            v-if="!cargando"
            class="text-xs font-bold text-emerald-400 bg-emerald-500/10 px-2 py-0.5 rounded border border-emerald-500/20">
            Horas Registradas
          </span>
        </div>
        <div class="mt-3 text-xs text-gray-500 flex justify-between">
          <span>Capacidad total taller:</span>
          <span class="font-bold text-gray-300">1,600 hrs</span>
        </div>
        <div class="absolute bottom-0 left-0 right-0 h-1 bg-[#FFCD11]"></div>
      </div>

    </div>

    <!-- SECCIÓN INTERMEDIA: DISTRIBUCIÓN DINÁMICA DE ÓRDENES -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg">
      <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider mb-4 border-b border-gray-800 pb-2">
        Distribución de Órdenes de Servicio en Curso
      </h2>

      <div class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3 text-center">
        <div class="bg-gray-800/60 p-3 rounded-lg border border-gray-700">
          <div class="text-xs text-gray-400">Solicitadas</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-gray-700 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-white mt-1">{{ metricas?.otsSolicitadas ?? 0 }}</div>
        </div>
        <div class="bg-blue-950/40 p-3 rounded-lg border border-blue-800/50">
          <div class="text-xs text-blue-300">Asignadas</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-blue-900/60 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-blue-400 mt-1">{{ metricas?.otsAsignadas ?? 0 }}</div>
        </div>
        <div class="bg-yellow-950/40 p-3 rounded-lg border border-yellow-800/50">
          <div class="text-xs text-yellow-300">Diagnóstico</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-yellow-900/60 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-yellow-400 mt-1">{{ metricas?.otsEnDiagnostico ?? 0 }}</div>
        </div>
        <div class="bg-orange-950/40 p-3 rounded-lg border border-orange-800/50">
          <div class="text-xs text-orange-300">Reparación</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-orange-900/60 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-orange-400 mt-1">{{ metricas?.otsEnReparacion ?? 0 }}</div>
        </div>
        <div class="bg-purple-950/40 p-3 rounded-lg border border-purple-800/50">
          <div class="text-xs text-purple-300">Control Calidad</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-purple-900/60 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-purple-400 mt-1">{{ metricas?.otsControlCalidad ?? 0 }}</div>
        </div>
        <div class="bg-emerald-950/40 p-3 rounded-lg border border-emerald-800/50">
          <div class="text-xs text-emerald-300">Cerradas / Mes</div>
          <div v-if="cargando" class="h-7 w-8 mx-auto mt-1 bg-emerald-900/60 rounded animate-pulse"></div>
          <div v-else class="text-2xl font-black text-emerald-400 mt-1">{{ metricas?.otsCerradas ?? 0 }}</div>
        </div>
      </div>
    </div>

    <!-- TABLA DE CONTROL DE DISPONIBILIDAD Y SLAS MARC POR EQUIPO -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-lg">
      <div class="p-4 bg-[#161F30] border-b border-gray-800">
        <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider">
          Supervisión de Disponibilidad y Condición de SLA MARC por Activo
        </h2>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-300">
          <thead class="bg-[#1F2937] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-700 tracking-wider">
            <tr>
              <th class="py-3 px-4">Código Flota</th>
              <th class="py-3 px-4">Modelo Caterpillar</th>
              <th class="py-3 px-4">Cliente Titular</th>
              <th class="py-3 px-4 text-right">Horómetro</th>
              <th class="py-3 px-4 text-center">Estado Mina</th>
              <th class="py-3 px-4 text-center">Disponibilidad Real</th>
              <th class="py-3 px-4 text-center">SLA Garantizado</th>
              <th class="py-3 px-4 text-center">Condición SLA</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="cargando && (!metricas || !metricas.flotas || metricas.flotas.length === 0)">
              <td colspan="8" class="text-center py-8 text-gray-400">
                <div class="flex items-center justify-center space-x-2">
                  <span class="w-2.5 h-2.5 rounded-full bg-yellow-400 animate-ping"></span>
                  <span class="text-xs font-semibold text-gray-300">Sincronizando telemetría y flota en tiempo real...</span>
                </div>
              </td>
            </tr>
            <tr v-else-if="!metricas || !metricas.flotas || metricas.flotas.length === 0">
              <td colspan="8" class="text-center py-8 text-gray-500">No hay maquinaria pesada registrada en el sistema.</td>
            </tr>
            <tr v-for="f in metricas?.flotas || []" :key="f.codigoFlota" class="hover:bg-gray-800/40 transition">
              <td class="py-3 px-4 font-mono font-bold text-[#FFCD11]">{{ f.codigoFlota }}</td>
              <td class="py-3 px-4 text-white font-semibold">{{ f.modeloCat }}</td>
              <td class="py-3 px-4 text-xs text-gray-300">{{ f.cliente }}</td>
              <td class="py-3 px-4 text-right font-mono font-bold text-yellow-400">{{ f.horometro }} hrs</td>
              <td class="py-3 px-4 text-center">
                <span class="px-2 py-0.5 rounded text-[11px] font-bold uppercase" :class="f.estadoOperativo === 'OPERATIVO' ? 'bg-emerald-500/20 text-emerald-400' : 'bg-amber-500/20 text-amber-400'">
                  {{ f.estadoOperativo }}
                </span>
              </td>
              <td class="py-3 px-4 text-center font-bold text-white">{{ f.disponibilidad }}%</td>
              <td class="py-3 px-4 text-center font-mono text-gray-400">{{ f.slaMarc }}%</td>
              <td class="py-3 px-4 text-center">
                <span class="px-2.5 py-1 rounded text-xs font-bold bg-emerald-500/15 text-emerald-400 border border-emerald-500/30">
                  {{ f.condicionSla }}
                </span>
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
import { useCatalogoStore } from '../stores/catalogo'
import { useAuthStore } from '../stores/auth'
import api from '../api/client'
import { generarPresentacionEjecutiva } from '../services/pptExportService'

const catalogoStore = useCatalogoStore()
const authStore = useAuthStore()
const metricas = ref(catalogoStore.metricasDashboard)
const cargando = ref(!catalogoStore.metricasDashboard)
const exportandoPPT = ref(false)
const mensajeNotificacion = ref('')

// Estados reactivos para el modal histórico PPTX
const mostrarModalPPT = ref(false)
const anioSeleccionado = ref('2026')
const sedeFiltro = ref('TODAS')

const cargarMetricas = async () => {
  if (!metricas.value) {
    cargando.value = true
  }
  try {
    const data = await catalogoStore.obtenerMetricas((inmediato) => {
      if (inmediato) {
        metricas.value = inmediato
        cargando.value = false
      }
    })
    if (data) {
      metricas.value = data
    }
  } catch (err) {
    console.error('Error cargando métricas:', err)
  } finally {
    cargando.value = false
  }
}

const abrirModalPPT = () => {
  mostrarModalPPT.value = true
}

const cerrarModalPPT = () => {
  mostrarModalPPT.value = false
}

const ejecutarDescargaPPT = async () => {
  exportandoPPT.value = true
  mensajeNotificacion.value = ''
  try {
    // Consulta en paralelo de los endpoints del backend para obtener la data real completa
    const [resMetricas, resOrdenes, resTecnicos, resAuditoria] = await Promise.allSettled([
      catalogoStore.obtenerMetricas(),
      api.get('/ordenes-servicio'),
      api.get('/tecnicos'),
      api.get('/auditoria')
    ])

    const dataMetricas = resMetricas.status === 'fulfilled' ? resMetricas.value : metricas.value
    const dataOrdenes = resOrdenes.status === 'fulfilled' ? resOrdenes.value.data : []
    const dataTecnicos = resTecnicos.status === 'fulfilled' ? resTecnicos.value.data : []
    const dataAuditoria = resAuditoria.status === 'fulfilled' ? resAuditoria.value.data : []

    const archivoGenerado = await generarPresentacionEjecutiva({
      metricas: dataMetricas,
      ordenes: dataOrdenes,
      tecnicos: dataTecnicos,
      auditoria: dataAuditoria,
      usuarioActual: authStore.nombreCompleto || 'Administrador Ferreyros',
      anio: anioSeleccionado.value,
      sede: sedeFiltro.value
    })

    cerrarModalPPT()
    mensajeNotificacion.value = `¡Presentación (${anioSeleccionado.value}) descargada con éxito: ${archivoGenerado}!`
    setTimeout(() => {
      mensajeNotificacion.value = ''
    }, 6000)
  } catch (error) {
    console.error('Error generando presentación PPTX:', error)
    alert('No se pudo generar la presentación PPTX. Verifique la consola para más detalles.')
  } finally {
    exportandoPPT.value = false
  }
}

const exportarReporte = () => {
  const cabeceras = "CodigoFlota,Modelo,Cliente,Horometro,EstadoOperativo,Disponibilidad,SLA_MARC,Condicion\n"
  const filas = (metricas.value?.flotas || []).map(f => 
    `"${f.codigoFlota}","${f.modeloCat}","${f.cliente}",${f.horometro},"${f.estadoOperativo}",${f.disponibilidad}%,${f.slaMarc}%,"${f.condicionSla}"`
  ).join("\n")

  const blob = new Blob([cabeceras + filas], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `Reporte_Ejecutivo_MARC_Ferreyros_${new Date().toISOString().slice(0, 10)}.csv`
  a.click()
}

onMounted(() => {
  cargarMetricas()
})
</script>
