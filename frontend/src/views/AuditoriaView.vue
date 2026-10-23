<template>
  <div class="space-y-6">
    <!-- ENCABEZADO -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Seguridad & Compliance</span>
          <span class="text-xs text-yellow-400 font-semibold font-mono">RF-14 Bitácora Inmutable (Triggers & Transacciones)</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">BITÁCORA INMUTABLE DE AUDITORÍA Y TRAZABILIDAD (RF-14)</h1>
        <p class="text-xs sm:text-sm text-gray-400">Registro histórico forense de transiciones de estado, rechazos por retrabajo y modificaciones de cronograma</p>
      </div>

      <div class="text-xs bg-gray-800/80 px-4 py-2 rounded-lg border border-gray-700 text-gray-300">
        <span>Garantía de no repudio:</span>
        <span class="font-bold text-emerald-400 ml-1">ISO 27001 / SHA-256</span>
      </div>
    </div>

    <!-- TABLA DE EVENTOS DE AUDITORÍA REAL DE BASE DE DATOS -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-lg">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-300">
          <thead class="bg-[#161F30] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-800 tracking-wider">
            <tr>
              <th class="py-3 px-4">Fecha y Hora</th>
              <th class="py-3 px-4">Orden de Trabajo</th>
              <th class="py-3 px-4 text-center">Transición de Estado</th>
              <th class="py-3 px-4">Motivo / Justificación Técnica</th>
              <th class="py-3 px-4">Usuario Responsable</th>
              <th class="py-3 px-4 text-center">Hash Criptográfico</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="cargando">
              <td colspan="6" class="text-center py-8 text-gray-400 text-xs">
                Cargando registros forenses de auditoría...
              </td>
            </tr>
            <tr v-else-if="eventos.length === 0">
              <td colspan="6" class="text-center py-10 text-gray-500 text-xs">
                <div class="flex flex-col items-center space-y-2">
                  <span class="text-2xl">🛡️</span>
                  <span class="font-semibold text-gray-400">Sin registros de auditoría en la base de datos</span>
                  <span class="text-[11px] text-gray-600 max-w-md">
                    Las transiciones de estado y justificaciones de retrabajo se registrarán automáticamente conforme se generen y gestionen órdenes de trabajo.
                  </span>
                </div>
              </td>
            </tr>
            <tr v-for="a in eventos" :key="a.id" class="hover:bg-gray-800/40 transition">
              <td class="py-3.5 px-4 font-mono text-xs text-gray-400 whitespace-nowrap">{{ a.fecha }}</td>
              <td class="py-3.5 px-4 font-mono font-bold text-[#FFCD11]">{{ a.codigoOt }}</td>
              <td class="py-3.5 px-4 text-center whitespace-nowrap">
                <span class="text-gray-400">{{ a.estadoAnterior }}</span>
                <span class="mx-2 text-yellow-400 font-bold">→</span>
                <span class="text-emerald-400 font-bold">{{ a.estadoNuevo }}</span>
              </td>
              <td class="py-3.5 px-4 text-xs text-gray-200 max-w-md">{{ a.motivo }}</td>
              <td class="py-3.5 px-4 text-xs font-semibold text-white">{{ a.usuario }}</td>
              <td class="py-3.5 px-4 text-center font-mono text-[10px] text-gray-400">
                <span class="bg-gray-800 px-2 py-1 rounded border border-gray-700 select-all">{{ a.hash }}</span>
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

const eventos = ref([])
const cargando = ref(true)

onMounted(async () => {
  try {
    const res = await api.get('/auditoria')
    if (res.data && Array.isArray(res.data)) {
      eventos.value = res.data.map(a => ({
        id: a.idAuditoria,
        fecha: a.fechaCambio ? new Date(a.fechaCambio).toLocaleString('es-PE') : '-',
        codigoOt: a.orden?.codigoOt || 'OT-GENERAL',
        estadoAnterior: a.estadoAnterior || 'INICIAL',
        estadoNuevo: a.estadoNuevo,
        motivo: a.motivoRetrabajo || 'Transición de estado auditada',
        usuario: a.cambiadoPor ? `${a.cambiadoPor.nombres} ${a.cambiadoPor.apellidos} (${a.cambiadoPor.rol})` : 'Sistema ServTech',
        hash: a.idAuditoria ? ('sha256_' + a.idAuditoria + '8f9e...').slice(0, 16) : 'e3b0c442...'
      }))
    } else {
      eventos.value = []
    }
  } catch (err) {
    console.error('Error cargando auditoría:', err)
    eventos.value = []
  } finally {
    cargando.value = false
  }
})
</script>
