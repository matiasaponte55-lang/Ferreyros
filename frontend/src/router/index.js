// src/router/index.js - Enrutador SPA de Ferreyros ServTech 2026 (Sección 10.2)
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

import LoginView from '../views/LoginView.vue'
import CatalogoView from '../views/CatalogoView.vue'
import FichaMaquinariaView from '../views/FichaMaquinariaView.vue'
import CuadrillasView from '../views/CuadrillasView.vue'
import NuevaOrdenView from '../views/NuevaOrdenView.vue'
import OrdenesView from '../views/OrdenesView.vue'
import DispatcherView from '../views/DispatcherView.vue'
import KanbanView from '../views/KanbanView.vue'
import InspeccionView from '../views/InspeccionView.vue'
import LiquidacionView from '../views/LiquidacionView.vue'
import DashboardView from '../views/DashboardView.vue'
import AuditoriaView from '../views/AuditoriaView.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: LoginView, meta: { publico: true } },
  { path: '/dashboard', name: 'Dashboard', component: DashboardView },
  { path: '/catalogo', name: 'Catalogo', component: CatalogoView },
  { path: '/maquinaria/:id', name: 'FichaMaquinaria', component: FichaMaquinariaView },
  { path: '/cuadrillas', name: 'Cuadrillas', component: CuadrillasView },
  { path: '/ordenes', name: 'Ordenes', component: OrdenesView },
  { path: '/ordenes/nueva', name: 'NuevaOrden', component: NuevaOrdenView },
  { path: '/dispatcher', name: 'Dispatcher', component: DispatcherView },
  { path: '/kanban', name: 'Kanban', component: KanbanView },
  { path: '/inspeccion/:id?', name: 'Inspeccion', component: InspeccionView },
  { path: '/liquidacion/:id?', name: 'Liquidacion', component: LiquidacionView },
  { path: '/auditoria', name: 'Auditoria', component: AuditoriaView },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// Guardia de Navegación Global (RBAC y Token JWT)
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (!to.meta.publico && !authStore.isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
