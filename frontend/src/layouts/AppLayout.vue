<template>
  <div class="min-h-screen bg-[#0B1320] text-gray-100 flex flex-col">
    <!-- BARRA SUPERIOR CORPORATIVA CATERPILLAR / FERREYROS -->
    <header class="bg-[#111827] border-b border-gray-800 sticky top-0 z-50 shadow-lg">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          
          <!-- LOGO E IDENTIDAD CATERPILLAR -->
          <div class="flex items-center space-x-3">
            <div class="flex items-center space-x-2 bg-[#FFCD11] text-black px-3 py-1.5 rounded font-black tracking-wider text-lg shadow-md">
              <span>CAT</span>
            </div>
            <div>
              <div class="flex items-center space-x-2">
                <span class="font-extrabold text-white text-base tracking-wide">FERREYROS S.A.</span>
                <span class="text-xs bg-yellow-500/20 text-yellow-400 px-2 py-0.5 rounded border border-yellow-500/30 font-semibold">SERVTECH 2026</span>
              </div>
              <p class="text-xs text-gray-400 hidden sm:block">Planificación y Asignación de Mantenimiento Pesado</p>
            </div>
          </div>

          <!-- CONTROLES CENTRALES: SEDE E INDICADOR DE ROL OPERATIVO AUTENTICADO -->
          <div class="hidden md:flex items-center space-x-4">
            <!-- Selector de Sede Operativa -->
            <div class="flex items-center space-x-2 bg-gray-800/80 px-3 py-1.5 rounded-lg border border-gray-700 text-xs">
              <span class="text-yellow-400 font-bold">📍 Sede:</span>
              <select v-model="authStore.currentSede" class="bg-transparent text-gray-200 outline-none cursor-pointer font-medium">
                <option value="Centro de Reparación de Componentes Arequipa - La Joya" class="bg-gray-800">Sede Arequipa - CRC La Joya</option>
                <option value="Mina Cerro Verde - Taller Bahía 3" class="bg-gray-800">Mina Cerro Verde (Arequipa)</option>
                <option value="Mina Las Bambas - Taller Chalcobamba" class="bg-gray-800">Mina Las Bambas (Apurímac)</option>
                <option value="Mina Cuajone - Bahía de Mantenimiento Botiflaca" class="bg-gray-800">Mina Cuajone (Moquegua)</option>
              </select>
            </div>

            <!-- Perfil / Rol Oficial Asignado en BD -->
            <div class="flex items-center space-x-1.5 bg-gray-900/90 px-3 py-1.5 rounded-lg border border-yellow-500/30 text-xs shadow-inner">
              <span class="text-gray-400 font-semibold">Perfil:</span>
              <span v-if="authStore.isAdmin" class="bg-purple-900/40 text-purple-300 font-bold px-2 py-0.5 rounded border border-purple-500/30">
                🛡️ Administrador General
              </span>
              <span v-else-if="authStore.isSupervisor" class="bg-amber-900/40 text-amber-300 font-bold px-2 py-0.5 rounded border border-amber-500/30">
                ⚡ Supervisor de Taller & Despacho
              </span>
              <span v-else-if="authStore.isPlanificador" class="bg-blue-900/40 text-blue-300 font-bold px-2 py-0.5 rounded border border-blue-500/30">
                📋 Planificador de Mantenimiento
              </span>
              <span v-else class="bg-emerald-900/40 text-emerald-300 font-bold px-2 py-0.5 rounded border border-emerald-500/30">
                🔧 Técnico de Servicio Especializado
              </span>
            </div>
          </div>

          <!-- PERFIL DE USUARIO Y ESTADO DE RED -->
          <div class="flex items-center space-x-3">
            <div class="text-right hidden sm:block">
              <div class="text-sm font-semibold text-white leading-tight">{{ authStore.nombreCompleto }}</div>
              <div class="flex items-center justify-end space-x-1 text-xs text-emerald-400">
                <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
                <span>Guardia 14x7 Activa</span>
              </div>
            </div>

            <button 
              @click="cerrarSesion" 
              title="Cerrar sesión"
              class="p-2 text-gray-400 hover:text-red-400 hover:bg-gray-800 rounded-lg transition">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
              </svg>
            </button>
          </div>

        </div>
      </div>

      <!-- BARRA DE NAVEGACIÓN PRINCIPAL (PESTAÑAS) -->
      <nav class="bg-[#161F30] border-t border-gray-800 px-4 sm:px-6 lg:px-8 overflow-x-auto scrollbar-none">
        <div class="max-w-7xl mx-auto flex space-x-1 sm:space-x-2 py-2">
          
          <router-link to="/dashboard" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/dashboard' }">
            <span>📊 Dashboard KPIs</span>
          </router-link>

          <router-link to="/catalogo" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/catalogo' }">
            <span>🚜 Catálogo CAT</span>
          </router-link>

          <router-link to="/cuadrillas" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/cuadrillas' }">
            <span>👷 Cuadrillas 14x7</span>
          </router-link>

          <router-link to="/ordenes" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/ordenes' }">
            <span>📑 Bandeja OTs</span>
          </router-link>

          <router-link to="/ordenes/nueva" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/ordenes/nueva' }">
            <span>➕ Apertura OT</span>
          </router-link>

          <router-link to="/dispatcher" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/dispatcher' }">
            <span>⏱️ Matriz GiST</span>
          </router-link>

          <router-link to="/kanban" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/kanban' }">
            <span>📌 Tablero Kanban</span>
          </router-link>

          <router-link to="/auditoria" 
            class="nav-tab" 
            :class="{ 'active': $route.path === '/auditoria' }">
            <span>🛡️ Auditoría RF-14</span>
          </router-link>

        </div>
      </nav>
    </header>

    <!-- CUERPO PRINCIPAL DONDE SE MONTAN TODAS LAS VISTAS DEL SISTEMA -->
    <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6">
      <slot />
    </main>

    <!-- PIE DE PÁGINA CORPORATIVO -->
    <footer class="bg-[#111827] border-t border-gray-800 py-4 text-center text-xs text-gray-500 no-print">
      <div class="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between">
        <p>© 2026 Ferreyros S.A. • ServTech 2026 - Centro de Reparación de Componentes (CRC Arequipa - La Joya)</p>
        <p class="mt-1 sm:mt-0 text-gray-400 font-medium">Curso Integrador Sistemas Software • UTP</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const cerrarSesion = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.nav-tab {
  @apply px-3 py-1.5 rounded-md text-xs sm:text-sm font-semibold text-gray-300 hover:text-white hover:bg-gray-800/80 transition whitespace-nowrap flex items-center space-x-1 border border-transparent;
}
.nav-tab.active {
  @apply bg-[#FFCD11] text-black border-yellow-400 shadow;
}
</style>
