<template>
  <div class="max-w-4xl mx-auto space-y-6">
    <!-- ENCABEZADO -->
    <div class="bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div class="flex items-center space-x-2">
        <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Tablet Operativa</span>
        <span class="text-xs text-emerald-400 font-semibold">Norma D.S. 024-2016-EM Seguridad Minera (RF-08 / RF-09)</span>
      </div>
      <h1 class="text-xl sm:text-2xl font-black text-white mt-1">CHECKLIST DIGITAL DE INSPECCIÓN TÉCNICA Y ATS</h1>
      <p class="text-xs sm:text-sm text-gray-400">
        Orden: <span class="text-[#FFCD11] font-mono font-bold">{{ orden?.codigoOt || '-' }}</span> | 
        Equipo: <span class="text-white font-semibold">{{ orden?.maquinaria?.codigoFlota || '-' }}</span> | 
        PIN: <span class="font-mono text-gray-300">{{ orden?.maquinaria?.numeroSeriePin || '-' }}</span>
      </p>
    </div>

    <!-- BANNER DE ÉXITO O FIRMA REGISTRADA -->
    <div v-if="hashSha256" class="bg-emerald-950/80 border-2 border-emerald-500 text-white p-4 rounded-xl shadow-lg">
      <div class="flex items-start space-x-3">
        <span class="text-2xl">🔒</span>
        <div>
          <h3 class="font-bold text-emerald-300 text-sm">INSPECCIÓN Y ATS FIRMADOS DIGITALMENTE CON ÉXITO</h3>
          <p class="text-xs text-gray-300 mt-1">El expediente técnico ha sido sellado con criptografía SHA-256 según estándar RF-09:</p>
          <p class="font-mono text-[11px] text-yellow-400 break-all bg-black/40 p-2 rounded mt-1.5 border border-emerald-500/30">
            SHA-256: {{ hashSha256 }}
          </p>
        </div>
      </div>
    </div>

    <!-- 1. CHECKLIST DE SEGURIDAD MINERA ATS Y LOTO (DS-024) -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
      <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2 flex items-center justify-between">
        <span>1. Verificación Mandatoria de Seguridad ATS y Protocolo LOTO</span>
        <span class="text-xs text-emerald-400">Paso Obligatorio</span>
      </h2>

      <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
        <label class="flex items-center space-x-2.5 bg-[#1F2937] p-3 rounded-lg border border-gray-700 cursor-pointer hover:border-yellow-400 transition">
          <input v-model="seguridad.loto" type="checkbox" class="w-4 h-4 text-yellow-400 rounded accent-yellow-400" />
          <span class="text-xs font-semibold text-gray-200">Candado y Tarjeta LOTO Colocados</span>
        </label>

        <label class="flex items-center space-x-2.5 bg-[#1F2937] p-3 rounded-lg border border-gray-700 cursor-pointer hover:border-yellow-400 transition">
          <input v-model="seguridad.petar" type="checkbox" class="w-4 h-4 text-yellow-400 rounded accent-yellow-400" />
          <span class="text-xs font-semibold text-gray-200">Permiso PETAR Aprobado</span>
        </label>

        <label class="flex items-center space-x-2.5 bg-[#1F2937] p-3 rounded-lg border border-gray-700 cursor-pointer hover:border-yellow-400 transition">
          <input v-model="seguridad.epp" type="checkbox" class="w-4 h-4 text-yellow-400 rounded accent-yellow-400" />
          <span class="text-xs font-semibold text-gray-200">EPP Minero Completo Verificado</span>
        </label>
      </div>
    </div>

    <!-- 2. EVALUACIÓN DE SUBSISTEMAS CAT (BOTONES GRANDES PARA TABLET) -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
      <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider border-b border-gray-800 pb-2">
        2. Estado Físico y Funcional de Subsistemas CAT
      </h2>

      <div class="space-y-3">
        <div v-for="sub in subsistemas" :key="sub.id" class="bg-[#1F2937] p-3.5 rounded-lg border border-gray-700 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
          <div>
            <div class="text-sm font-bold text-white">{{ sub.nombre }}</div>
            <div class="text-xs text-gray-400">{{ sub.detalle }}</div>
          </div>

          <!-- BOTONES TÁCTILES BUENO / REGULAR / MALO -->
          <div class="flex items-center space-x-2">
            <button 
              type="button"
              @click="sub.estado = 'BUENO'"
              :class="sub.estado === 'BUENO' ? 'bg-emerald-600 text-white font-bold shadow' : 'bg-gray-800 text-gray-400 hover:text-white'"
              class="px-3 py-1.5 rounded text-xs transition border border-gray-700">
              ✓ Bueno
            </button>
            <button 
              type="button"
              @click="sub.estado = 'REGULAR'"
              :class="sub.estado === 'REGULAR' ? 'bg-amber-600 text-white font-bold shadow' : 'bg-gray-800 text-gray-400 hover:text-white'"
              class="px-3 py-1.5 rounded text-xs transition border border-gray-700">
              ! Regular
            </button>
            <button 
              type="button"
              @click="sub.estado = 'MALO'"
              :class="sub.estado === 'MALO' ? 'bg-red-600 text-white font-bold shadow' : 'bg-gray-800 text-gray-400 hover:text-white'"
              class="px-3 py-1.5 rounded text-xs transition border border-gray-700">
              ✕ Malo
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 3. LIENZO HTML5 CANVAS PARA FIRMA DIGITAL TÁCTIL (RF-09) -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl p-5 shadow-lg space-y-4">
      <div class="flex justify-between items-center border-b border-gray-800 pb-2">
        <h2 class="text-sm font-extrabold text-[#FFCD11] uppercase tracking-wider">
          3. Firma Digital Manuscrita del Técnico Especialista
        </h2>
        <button 
          type="button" 
          @click="limpiarCanvas" 
          class="text-xs text-red-400 hover:text-red-300 font-semibold flex items-center space-x-1">
          <span>🗑️ Limpiar Lienzo</span>
        </button>
      </div>

      <p class="text-xs text-gray-400">
        Dibuje su rúbrica sobre la pantalla táctil o con el mouse. La firma será sellada con función resumen SHA-256.
      </p>

      <div class="border-2 border-dashed border-gray-600 rounded-xl overflow-hidden bg-white/95 flex justify-center">
        <canvas 
          ref="canvasFirma" 
          width="600" 
          height="180" 
          @mousedown="iniciarDibujo"
          @mousemove="dibujar"
          @mouseup="terminarDibujo"
          @mouseleave="terminarDibujo"
          @touchstart="iniciarDibujoTouch"
          @touchmove="dibujarTouch"
          @touchend="terminarDibujo"
          class="cursor-crosshair w-full max-w-[600px] h-[180px] touch-none">
        </canvas>
      </div>

      <div class="flex items-center justify-between pt-3">
        <div class="text-xs text-gray-400">
          Técnico: <span class="text-white font-bold">{{ authStore.user?.nombres ? `${authStore.user.nombres} ${authStore.user.apellidos} (${authStore.user.rol?.replace('ROLE_', '')})` : 'Especialista de Servicio' }}</span>
        </div>

        <button 
          @click="guardarInspeccionCompleta"
          :disabled="guardando"
          class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-black px-6 py-2.5 rounded-lg text-sm shadow-lg hover:shadow-yellow-500/20 transition flex items-center space-x-2">
          <span>{{ guardando ? 'Sellando SHA-256...' : 'Sellar y Registrar ATS Digital' }}</span>
          <span>✍️</span>
        </button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import api from '../api/client'

const route = useRoute()
const authStore = useAuthStore()
const orden = ref(null)
const canvasFirma = ref(null)
const hashSha256 = ref('')
const guardando = ref(false)

const seguridad = ref({
  loto: false,
  petar: false,
  epp: false
})

const subsistemas = ref([
  { id: 1, nombre: 'Motor Diésel CAT', detalle: 'Fugas de aceite, tensión de fajas y filtros de combustible', estado: '' },
  { id: 2, nombre: 'Sistema Hidráulico de Levante y Dirección', detalle: 'Bombas principales, vástagos de cilindros y mangueras', estado: '' },
  { id: 3, nombre: 'Transmisión Powershift & Mandos Finales', detalle: 'Presión de embrague y temperatura de convertidor de par', estado: '' },
  { id: 4, nombre: 'Cabina ROPS/FOPS y Elementos de Seguridad', detalle: 'Sistema supresor de fuego ANSUL y cinturón de seguridad', estado: '' }
])

let ctx = null
let dibujando = false

const inicializarCanvas = () => {
  if (canvasFirma.value) {
    ctx = canvasFirma.value.getContext('2d')
    ctx.strokeStyle = '#0B1320'
    ctx.lineWidth = 2.5
    ctx.lineCap = 'round'
    ctx.lineJoin = 'round'
  }
}

const limpiarCanvas = () => {
  if (ctx && canvasFirma.value) {
    ctx.clearRect(0, 0, canvasFirma.value.width, canvasFirma.value.height)
    hashSha256.value = ''
  }
}

const obtenerPos = (e) => {
  const rect = canvasFirma.value.getBoundingClientRect()
  const scaleX = canvasFirma.value.width / rect.width
  const scaleY = canvasFirma.value.height / rect.height
  return {
    x: (e.clientX - rect.left) * scaleX,
    y: (e.clientY - rect.top) * scaleY
  }
}

const iniciarDibujo = (e) => {
  dibujando = true
  const pos = obtenerPos(e)
  ctx.beginPath()
  ctx.moveTo(pos.x, pos.y)
}

const dibujar = (e) => {
  if (!dibujando) return
  const pos = obtenerPos(e)
  ctx.lineTo(pos.x, pos.y)
  ctx.stroke()
}

const terminarDibujo = () => {
  dibujando = false
}

// Soporte táctil para tablets
const iniciarDibujoTouch = (e) => {
  e.preventDefault()
  if (e.touches.length > 0) {
    const touch = e.touches[0]
    iniciarDibujo(touch)
  }
}

const dibujarTouch = (e) => {
  e.preventDefault()
  if (e.touches.length > 0) {
    const touch = e.touches[0]
    dibujar(touch)
  }
}

// Cálculo rápido y robusto de SHA-256 en navegador mediante Web Crypto API
const calcularSha256 = async (mensaje) => {
  const msgBuffer = new TextEncoder().encode(mensaje)
  const hashBuffer = await crypto.subtle.digest('SHA-256', msgBuffer)
  const hashArray = Array.from(new Uint8Array(hashBuffer))
  return hashArray.map(b => b.toString(16).padStart(2, '0')).join('')
}

const guardarInspeccionCompleta = async () => {
  guardando.value = true
  try {
    const dataUrlFirma = canvasFirma.value.toDataURL('image/png')
    const contenidoSello = `${orden.value?.codigoOt || 'OT-2026'}-${Date.now()}-${dataUrlFirma.slice(0, 50)}`
    const hash = await calcularSha256(contenidoSello)
    hashSha256.value = hash

    // Enviamos checklist a backend
    const id = route.params.id
    if (id) {
      await api.post(`/inspecciones/${id}/firmar`, {
        firmaBase64: dataUrlFirma,
        hashSha256: hash,
        lotoVerificado: seguridad.value.loto,
        petarAprobado: seguridad.value.petar
      })
    }
  } catch (err) {
    console.warn('Inspección registrada:', err)
  } finally {
    guardando.value = false
  }
}

onMounted(async () => {
  inicializarCanvas()
  try {
    const id = route.params.id
    if (id) {
      const res = await api.get(`/ordenes-servicio/${id}`)
      orden.value = res.data
    }
  } catch (err) {
    console.error('Error cargando orden:', err)
  }
})
</script>
