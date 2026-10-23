<template>
  <div class="min-h-screen bg-[#0B1320] flex items-center justify-center p-4">
    <!-- TARJETA PRINCIPAL DE LOGIN CORPORATIVO CATERPILLAR / FERREYROS -->
    <div class="max-w-md w-full bg-[#111827] border border-gray-800 rounded-xl shadow-2xl p-8 relative overflow-hidden">
      
      <!-- LÍNEA AMARILLA CORPORATIVA CATERPILLAR -->
      <div class="absolute top-0 left-0 right-0 h-1.5 bg-[#FFCD11]"></div>

      <!-- LOGO Y TÍTULO OFICIAL -->
      <div class="text-center mb-6">
        <div class="inline-flex items-center space-x-2 bg-[#FFCD11] text-black px-4 py-1.5 rounded font-black tracking-widest text-xl mb-3 shadow">
          <span>FERREYROS CAT</span>
        </div>
        <h1 class="text-xl font-bold text-white tracking-wide">SERVTECH 2026</h1>
        <p class="text-xs text-gray-400 mt-1">SISTEMA DE GESTIÓN DE SERVICIO TÉCNICO Y DESPACHO OPERATIVO</p>
        <p class="text-[11px] text-yellow-500/90 font-mono mt-0.5">Control de Acceso Seguro RBAC & Autenticación Criptográfica</p>
      </div>

      <!-- BANNER DE ALERTA DE ERROR -->
      <div v-if="errorMsg" class="mb-5 bg-red-900/30 border border-red-500/50 text-red-200 text-xs p-3 rounded-lg flex items-center space-x-2 animate-pulse">
        <svg class="w-4 h-4 text-red-400 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
        <span>{{ errorMsg }}</span>
      </div>

      <!-- FORMULARIO DE ACCESO AUTENTICADO CON BASE DE DATOS -->
      <form @submit.prevent="handleLogin" class="space-y-4">
        
        <div>
          <label class="block text-xs font-semibold text-gray-300 mb-1">USUARIO O CORREO CORPORATIVO</label>
          <input 
            v-model="username" 
            type="text" 
            placeholder="usuario@ferreyros.com.pe"
            required
            autocomplete="username"
            class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#FFCD11] transition"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-gray-300 mb-1">CONTRASEÑA</label>
          <input 
            v-model="password" 
            type="password" 
            placeholder="••••••••"
            required
            autocomplete="current-password"
            class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#FFCD11] transition"
          />
        </div>

        <div>
          <label class="block text-xs font-semibold text-gray-300 mb-1">PERFIL / ROL OPERATIVO</label>
          <select 
            v-model="rol"
            class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#FFCD11] transition cursor-pointer">
            <option value="">Autodetectar según credenciales en BD</option>
            <option value="ROLE_SUPERVISOR">Supervisor de Mantenimiento & Despacho</option>
            <option value="ROLE_PLANIFICADOR">Planificador de Servicios & Contratos MARC</option>
            <option value="ROLE_TECNICO">Técnico Mecánico de Campo / Taller CRC</option>
            <option value="ROLE_ADMIN">Administrador de Plataforma y Seguridad</option>
          </select>
        </div>

        <div>
          <label class="block text-xs font-semibold text-gray-300 mb-1">SEDE / CENTRO OPERATIVO</label>
          <select 
            v-model="sede"
            class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3.5 py-2.5 text-sm text-white focus:outline-none focus:border-[#FFCD11] transition cursor-pointer">
            <option value="Centro de Reparación de Componentes Arequipa - La Joya">Sede Arequipa - Taller CRC La Joya</option>
            <option value="Mina Cerro Verde - Taller Bahía 3">Mina Cerro Verde (Arequipa)</option>
            <option value="Mina Las Bambas - Taller Chalcobamba">Mina Las Bambas (Apurímac)</option>
            <option value="Mina Cuajone - Bahía de Mantenimiento Botiflaca">Mina Cuajone (Moquegua)</option>
          </select>
        </div>

        <button 
          type="submit" 
          :disabled="cargando"
          class="w-full bg-[#FFCD11] hover:bg-yellow-400 text-black font-extrabold py-3 px-4 rounded-lg shadow-lg hover:shadow-yellow-500/20 transition flex items-center justify-center space-x-2 text-sm tracking-wide mt-2 disabled:opacity-50">
          <span v-if="cargando" class="animate-spin mr-2">⚙️</span>
          <span>{{ cargando ? 'VERIFICANDO EN BASE DE DATOS...' : 'INICIAR SESIÓN' }}</span>
        </button>

      </form>

      <!-- PIE DE SEGURIDAD -->
      <div class="mt-6 pt-4 border-t border-gray-800 text-center">
        <p class="text-[10px] text-gray-500 uppercase tracking-widest font-mono">
          Ferreyros S.A. • Acceso Restringido • Protocolo TLS 1.3 / JWT / BCrypt
        </p>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const rol = ref('')
const sede = ref('Centro de Reparación de Componentes Arequipa - La Joya')
const cargando = ref(false)
const errorMsg = ref('')

const handleLogin = async () => {
  if (!username.value.trim() || !password.value.trim()) {
    errorMsg.value = 'Por favor complete su usuario corporativo y contraseña.'
    return
  }

  cargando.value = true
  errorMsg.value = ''
  try {
    authStore.cambiarSede(sede.value)
    await authStore.login(username.value.trim(), password.value, rol.value)
    router.push('/dashboard')
  } catch (err) {
    if (err.response && err.response.data && err.response.data.message) {
      errorMsg.value = err.response.data.message
    } else if (err.response && err.response.status === 401) {
      errorMsg.value = 'Credenciales no válidas. Verifique su usuario y contraseña.'
    } else if (err.response && err.response.status === 403) {
      errorMsg.value = 'Acceso denegado: el rol seleccionado no coincide con sus credenciales en la base de datos.'
    } else {
      errorMsg.value = 'Error al verificar credenciales con la base de datos.'
    }
  } finally {
    cargando.value = false
  }
}
</script>
