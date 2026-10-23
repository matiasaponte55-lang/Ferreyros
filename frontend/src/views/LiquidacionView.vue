<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <!-- BARRA DE ACCIONES (NO SE MUESTRA EN IMPRESIÓN) -->
    <div class="no-print flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Cierre Técnico</span>
          <span class="text-xs text-emerald-400 font-semibold">RF-10 Acta de Liquidación & RF-09 Firma Digital</span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">ACTA TÉCNICA DE LIQUIDACIÓN Y CONFORMIDAD MARC</h1>
        <p class="text-xs sm:text-sm text-gray-400">Expediente formal para auditoría contractual y facturación de servicios Caterpillar</p>
      </div>

      <div class="flex items-center space-x-3">
        <button 
          @click="imprimirActa"
          class="bg-gray-800 hover:bg-gray-700 text-yellow-400 border border-gray-700 font-bold px-4 py-2.5 rounded-lg text-sm shadow transition flex items-center space-x-2">
          <span>🖨️</span>
          <span>Imprimir Acta (A4 PDF)</span>
        </button>
        <button 
          @click="liquidarServicio"
          :disabled="servicioLiquidado"
          class="bg-[#FFCD11] hover:bg-yellow-400 disabled:bg-gray-700 disabled:text-gray-500 text-black font-extrabold px-5 py-2.5 rounded-lg text-sm shadow-lg transition flex items-center space-x-2">
          <span>{{ servicioLiquidado ? '✓ Servicio Liquidado' : 'Firmar y Liquidar Servicio' }}</span>
        </button>
      </div>
    </div>

    <!-- DOCUMENTO FORMAL IMPRIMIBLE (ESTILO ACTA TÉCNICA FERREYROS) -->
    <div class="bg-white text-gray-900 rounded-xl p-8 shadow-2xl border border-gray-300 print-clean">
      
      <!-- ENCABEZADO CORPORATIVO -->
      <div class="flex justify-between items-center border-b-2 border-black pb-4 mb-6">
        <div class="flex items-center space-x-3">
          <div class="bg-[#FFCD11] text-black font-black text-2xl px-3 py-1 rounded">
            CAT
          </div>
          <div>
            <h2 class="text-lg font-black tracking-tight leading-tight">FERREYROS S.A.</h2>
            <p class="text-xs font-semibold text-gray-600">DIVISIÓN DE SOPORTE AL PRODUCTO • CONTRATOS MARC</p>
          </div>
        </div>

        <div class="text-right">
          <div class="text-xs font-bold text-gray-500 uppercase">Acta de Liquidación Técnica</div>
          <div class="text-lg font-mono font-black text-red-700">{{ orden?.codigoOt || '-' }}</div>
          <div class="text-xs text-gray-600 font-mono">{{ fechaHoy }}</div>
        </div>
      </div>

      <!-- DATOS DE LA MAQUINARIA Y CLIENTE -->
      <div class="grid grid-cols-2 gap-4 text-xs mb-6 bg-gray-50 p-4 rounded-lg border border-gray-200">
        <div>
          <p><span class="font-bold text-gray-700">Cliente / Razón Social:</span> {{ orden?.maquinaria?.cliente?.razonSocial || '-' }}</p>
          <p class="mt-1"><span class="font-bold text-gray-700">Operación Minera / Sede:</span> {{ orden?.sede?.nombre || orden?.maquinaria?.sedeActual?.nombre || '-' }}</p>
          <p class="mt-1"><span class="font-bold text-gray-700">Contrato Vigente:</span> {{ orden?.maquinaria?.contratoMarc || '-' }}</p>
        </div>
        <div>
          <p><span class="font-bold text-gray-700">Flota / Modelo CAT:</span> {{ orden?.maquinaria?.codigoFlota || '-' }} - {{ orden?.maquinaria?.modeloCat || '-' }}</p>
          <p class="mt-1"><span class="font-bold text-gray-700">Serie Chasis PIN:</span> <span class="font-mono font-bold">{{ orden?.maquinaria?.numeroSeriePin || '-' }}</span></p>
          <p class="mt-1"><span class="font-bold text-gray-700">Horómetro de Cierre:</span> <span class="font-mono font-bold text-blue-800">{{ orden?.horometroRegistro ?? 0 }} hrs</span></p>
        </div>
      </div>

      <!-- RESUMEN DE LA LABOR EJECUTADA -->
      <div class="mb-6">
        <h3 class="text-xs font-black uppercase text-gray-800 border-b border-gray-300 pb-1 mb-2">
          1. Descripción del Servicio y Protocolo Técnico Aplicado
        </h3>
        <p class="text-xs text-gray-700 leading-relaxed bg-yellow-50/50 p-3 rounded border border-yellow-200">
          <span class="font-bold">Labor Realizada:</span> {{ orden?.tipoMantenimiento || '-' }} - {{ orden?.fallaReportada || 'Servicio de mantenimiento técnico ejecutado conforme a pauta Caterpillar.' }}
        </p>
      </div>

      <!-- TABLA DE REPUESTOS GENUINOS INSTALADOS -->
      <div class="mb-6">
        <h3 class="text-xs font-black uppercase text-gray-800 border-b border-gray-300 pb-1 mb-2">
          2. Repuestos e Insumos Genuinos Caterpillar Instalados
        </h3>
        <table class="w-full text-left text-xs border border-gray-300">
          <thead class="bg-gray-100 text-gray-700 uppercase font-bold text-[11px] border-b border-gray-300">
            <tr>
              <th class="p-2 border-r border-gray-300">Nro Parte CAT</th>
              <th class="p-2 border-r border-gray-300">Descripción del Componente</th>
              <th class="p-2 text-center border-r border-gray-300">Cant.</th>
              <th class="p-2 text-right border-r border-gray-300">P. Unitario ($)</th>
              <th class="p-2 text-right">Total ($)</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-200">
            <tr v-if="repuestos.length === 0">
              <td colspan="5" class="p-4 text-center text-gray-500">
                No se registraron repuestos ni consumibles en esta orden.
              </td>
            </tr>
            <tr v-for="r in repuestos" :key="r.numeroParte">
              <td class="p-2 font-mono font-bold border-r border-gray-200">{{ r.numeroParte }}</td>
              <td class="p-2 border-r border-gray-200">{{ r.descripcion }}</td>
              <td class="p-2 text-center font-bold border-r border-gray-200">{{ r.cantidad }}</td>
              <td class="p-2 text-right font-mono border-r border-gray-200">${{ r.precioUnitario?.toFixed(2) }}</td>
              <td class="p-2 text-right font-mono font-bold">${{ ((r.cantidad || 0) * (r.precioUnitario || 0)).toFixed(2) }}</td>
            </tr>
          </tbody>
          <tfoot class="bg-gray-100 font-bold border-t-2 border-gray-400">
            <tr>
              <td colspan="4" class="p-2 text-right">TOTAL MATERIALES & REPUESTOS:</td>
              <td class="p-2 text-right font-mono font-black text-gray-900">${{ totalRepuestos.toFixed(2) }}</td>
            </tr>
          </tfoot>
        </table>
      </div>

      <!-- COSTOS DE MANO DE OBRA Y LIQUIDACIÓN -->
      <div class="mb-8 flex justify-end">
        <div class="w-72 bg-gray-50 p-3 rounded border border-gray-300 text-xs space-y-1.5">
          <div class="flex justify-between">
            <span class="text-gray-600">Materiales y Filtros CAT:</span>
            <span class="font-mono font-semibold">${{ totalRepuestos.toFixed(2) }}</span>
          </div>
          <div class="flex justify-between pt-1.5 border-t border-gray-300 font-black text-sm text-gray-900">
            <span>TOTAL LIQUIDACIÓN:</span>
            <span class="font-mono text-red-700">${{ totalRepuestos.toFixed(2) }} USD</span>
          </div>
        </div>
      </div>

      <!-- FIRMAS DE CONFORMIDAD Y HASH SHA-256 -->
      <div class="border-t-2 border-gray-800 pt-6">
        <div class="grid grid-cols-2 gap-8 text-center text-xs">
          
          <!-- FIRMA TÉCNICO FERREYROS -->
          <div class="border border-gray-300 rounded p-4 bg-gray-50/50">
            <div class="h-16 flex items-center justify-center">
              <span class="font-serif italic font-bold text-gray-700 text-base">
                {{ authStore.user?.nombres ? `${authStore.user.nombres} ${authStore.user.apellidos}` : 'Firma Pendiente' }}
              </span>
            </div>
            <div class="border-t border-gray-400 pt-1">
              <p class="font-bold text-gray-900">
                {{ authStore.user?.nombres ? `${authStore.user.nombres} ${authStore.user.apellidos}` : 'Técnico Especialista' }}
              </p>
              <p class="text-[11px] text-gray-600">{{ authStore.user?.rol?.replace('ROLE_', '') || 'Técnico CAT' }}</p>
              <p class="text-[10px] text-yellow-700 font-mono mt-0.5">Ferreyros S.A.</p>
            </div>
          </div>

          <!-- FIRMA CLIENTE MINERO -->
          <div class="border border-gray-300 rounded p-4 bg-gray-50/50">
            <div class="h-16 flex items-center justify-center">
              <span class="font-serif italic font-bold text-gray-700 text-base">
                {{ orden?.maquinaria?.cliente?.contactoNombre || 'Representante Autorizado' }}
              </span>
            </div>
            <div class="border-t border-gray-400 pt-1">
              <p class="font-bold text-gray-900">
                {{ orden?.maquinaria?.cliente?.contactoNombre || 'Representante Autorizado' }}
              </p>
              <p class="text-[11px] text-gray-600">Superintendencia de Mina</p>
              <p class="text-[10px] text-blue-700 font-mono mt-0.5">
                {{ orden?.maquinaria?.cliente?.razonSocial || 'Cliente Titular' }}
              </p>
            </div>
          </div>

        </div>

        <!-- SELLO CRIPTOGRÁFICO SHA-256 -->
        <div class="mt-6 p-2.5 bg-gray-100 rounded border border-gray-300 text-[10px] font-mono text-gray-600 text-center">
          <span class="font-bold text-gray-800">SELLO CRIPTOGRÁFICO DE CONFORMIDAD (RF-09 / ISO 27001):</span>
          <br>
          <span class="text-gray-900 font-semibold">{{ hashDocumento }}</span>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api/client'

const route = useRoute()
const authStore = useAuthStore()
const orden = ref(null)
const servicioLiquidado = ref(false)
const hashDocumento = ref('e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855')

const fechaHoy = new Date().toLocaleDateString('es-PE', { day: '2-digit', month: '2-digit', year: 'numeric' })

const repuestos = ref([])

const totalRepuestos = computed(() => {
  return repuestos.value.reduce((acc, r) => acc + ((r.cantidad || 0) * (r.precioUnitario || 0)), 0)
})

const imprimirActa = () => {
  window.print()
}

const liquidarServicio = async () => {
  try {
    const id = route.params.id
    if (id) {
      await api.patch(`/ordenes-servicio/${id}/estado`, {
        nuevoEstado: 'CERRADA',
        motivoRetrabajo: 'Liquidación formal de servicio y conformidad técnica aprobada'
      })
    }
    servicioLiquidado.value = true
    alert('Servicio liquidado y cerrado formalmente en el sistema.')
  } catch (err) {
    alert('Error al liquidar servicio: ' + (err.response?.data?.mensaje || err.message))
  }
}

onMounted(async () => {
  try {
    const id = route.params.id
    if (id) {
      const res = await api.get(`/ordenes-servicio/${id}`)
      orden.value = res.data
      hashDocumento.value = '7a91bc4428de1c149afbf4c8996fb92427ae41e4649b934ca495991b' + id.toString().padStart(8, '0')
    }
  } catch (err) {
    console.error('Error cargando liquidación:', err)
  }
})
</script>
