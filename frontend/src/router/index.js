// src/router/index.js - Enrutador SPA de Ferreyros ServTech 2026 (Sección 10.2)
import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

// Carga Perezosa (Lazy Loading / Code Splitting) para acelerar la carga inicial
const LoginView = () => import('../views/LoginView.vue')
const CatalogoView = () => import('../views/CatalogoView.vue')
const FichaMaquinariaView = () => import('../views/FichaMaquinariaView.vue')
const CuadrillasView = () => import('../views/CuadrillasView.vue')
const NuevaOrdenView = () => import('../views/NuevaOrdenView.vue')
const OrdenesView = () => import('../views/OrdenesView.vue')
const DispatcherView = () => import('../views/DispatcherView.vue')
const KanbanView = () => import('../views/KanbanView.vue')
const InspeccionView = () => import('../views/InspeccionView.vue')
const LiquidacionView = () => import('../views/LiquidacionView.vue')
const DashboardView = () => import('../views/DashboardView.vue')
const AuditoriaView = () => import('../views/AuditoriaView.vue')

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
