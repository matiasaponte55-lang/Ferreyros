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

      <button 
        @click="exportarReporte"
        class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold px-4 py-2.5 rounded-lg text-sm shadow transition flex items-center space-x-2 self-start md:self-auto">
        <span>📥</span>
        <span>Descargar Informe Ejecutivo MARC</span>
      </button>
    </div>

    <!-- 4 TARJETAS KPI PRINCIPALES -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      
      <!-- 1. DISPONIBILIDAD MARC -->
      <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg relative overflow-hidden">
        <div class="text-xs font-semibold text-gray-400 uppercase tracking-wider">Disponibilidad MARC Promedio</div>
        <div class="mt-2 flex items-baseline space-x-2">
          <span class="text-3xl font-black text-white">{{ metricas?.disponibilidadMarcPromedio ?? 0 }}%</span>
          <span 
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
          <span class="text-3xl font-black text-white">{{ metricas?.mttrPromedioHoras ?? 0 }} hrs</span>
          <span class="text-xs font-bold text-blue-400 bg-blue-500/10 px-2 py-0.5 rounded border border-blue-500/20">
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
          <span class="text-3xl font-black text-white">{{ metricas?.ocupacionCuadrillas ?? 0 }}%</span>
          <span class="text-xs font-bold text-yellow-400 bg-yellow-500/10 px-2 py-0.5 rounded border border-yellow-500/20">
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
          <span class="text-3xl font-black text-white">{{ metricas?.horasFacturables ?? 0 }} hrs</span>
          <span class="text-xs font-bold text-emerald-400 bg-emerald-500/10 px-2 py-0.5 rounded border border-emerald-500/20">
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
          <div class="text-2xl font-black text-white mt-1">{{ metricas?.otsSolicitadas ?? 0 }}</div>
        </div>
        <div class="bg-blue-950/40 p-3 rounded-lg border border-blue-800/50">
          <div class="text-xs text-blue-300">Asignadas</div>
          <div class="text-2xl font-black text-blue-400 mt-1">{{ metricas?.otsAsignadas ?? 0 }}</div>
        </div>
        <div class="bg-yellow-950/40 p-3 rounded-lg border border-yellow-800/50">
          <div class="text-xs text-yellow-300">Diagnóstico</div>
          <div class="text-2xl font-black text-yellow-400 mt-1">{{ metricas?.otsEnDiagnostico ?? 0 }}</div>
        </div>
        <div class="bg-orange-950/40 p-3 rounded-lg border border-orange-800/50">
          <div class="text-xs text-orange-300">Reparación</div>
          <div class="text-2xl font-black text-orange-400 mt-1">{{ metricas?.otsEnReparacion ?? 0 }}</div>
        </div>
        <div class="bg-purple-950/40 p-3 rounded-lg border border-purple-800/50">
          <div class="text-xs text-purple-300">Control Calidad</div>
          <div class="text-2xl font-black text-purple-400 mt-1">{{ metricas?.otsControlCalidad ?? 0 }}</div>
        </div>
        <div class="bg-emerald-950/40 p-3 rounded-lg border border-emerald-800/50">
          <div class="text-xs text-emerald-300">Cerradas / Mes</div>
          <div class="text-2xl font-black text-emerald-400 mt-1">{{ metricas?.otsCerradas ?? 0 }}</div>
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
            <tr v-if="!metricas || !metricas.flotas || metricas.flotas.length === 0">
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
import api from '../api/client'

const metricas = ref(null)

const cargarMetricas = async () => {
  try {
    const res = await api.get('/dashboard/metricas')
    metricas.value = res.data
  } catch (err) {
    console.error('Error cargando métricas:', err)
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
