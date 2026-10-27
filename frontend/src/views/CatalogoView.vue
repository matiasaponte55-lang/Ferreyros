<template>
  <div class="space-y-6">
    <!-- ENCABEZADO DE PANTALLA -->
    <div class="flex flex-col md:flex-row md:items-center justify-between gap-4 bg-[#111827] p-5 rounded-xl border border-gray-800 shadow-md">
      <div>
        <div class="flex items-center space-x-2">
          <span class="text-xs bg-[#FFCD11] text-black font-extrabold px-2.5 py-0.5 rounded uppercase">Flota Crítica MARC</span>
          <span class="text-xs text-emerald-400 flex items-center font-semibold">
            <span class="w-2 h-2 rounded-full bg-emerald-500 animate-ping mr-1.5"></span>
            Telemetría MineStar / VIMS Activa
          </span>
        </div>
        <h1 class="text-xl sm:text-2xl font-black text-white mt-1">CATÁLOGO CENTRALIZADO DE MAQUINARIA PESADA CAT</h1>
        <p class="text-xs sm:text-sm text-gray-400">Inventario de activos Caterpillar vinculados a contratos MARC en faenas mineras y talleres zonales</p>
      </div>

      <div class="flex items-center space-x-3">
        <button 
          @click="showModal = true" 
          class="bg-[#FFCD11] hover:bg-yellow-400 text-black font-bold px-4 py-2 rounded-lg text-sm shadow transition flex items-center space-x-1.5">
          <span>➕</span>
          <span>Registrar Maquinaria</span>
        </button>
      </div>
    </div>

    <!-- FILTROS REACTIVOS (BÚSQUEDA, CLIENTE Y FAMILIA CAT) -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-3 bg-[#111827] p-4 rounded-xl border border-gray-800">
      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Buscar por Flota / Serie PIN</label>
        <input 
          v-model="filtroBuscar" 
          type="text" 
          placeholder="Ej: CAT-797F, CAT6060..." 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]"
        />
      </div>

      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Filtrar por Familia de Maquinaria</label>
        <select 
          v-model="filtroFamilia" 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todas">Todas las Familias</option>
          <option value="CAMION_MINERO">Camiones Mineros (797F / 793F)</option>
          <option value="PALA_HIDRAULICA">Palas Hidráulicas (6060 FS)</option>
          <option value="TRACTOR_ORUGA">Tractores de Oruga (D11T)</option>
          <option value="CARGADOR_FRONTAL">Cargadores de Ruedas (994K)</option>
        </select>
      </div>

      <div>
        <label class="block text-xs font-semibold text-gray-400 mb-1">Estado Operativo en Mina</label>
        <select 
          v-model="filtroEstado" 
          class="w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11]">
          <option value="Todos">Todos los Estados</option>
          <option value="OPERATIVO">OPERATIVO (En Producción)</option>
          <option value="EN_MANTENIMIENTO">EN MANTENIMIENTO (Taller)</option>
          <option value="STAND_BY">STAND BY (Disponible)</option>
          <option value="PARADA_CRITICA_DOWN">PARADA CRÍTICA (Down)</option>
        </select>
      </div>
    </div>

    <!-- TABLA CENTRAL DE MAQUINARIAS -->
    <div class="bg-[#111827] border border-gray-800 rounded-xl overflow-hidden shadow-lg">
      <div class="overflow-x-auto">
        <table class="w-full text-left text-sm text-gray-300">
          <thead class="bg-[#161F30] text-gray-400 uppercase text-[11px] font-bold border-b border-gray-800 tracking-wider">
            <tr>
              <th class="py-3.5 px-4">Código Flota</th>
              <th class="py-3.5 px-4">Modelo CAT</th>
              <th class="py-3.5 px-4">Serie PIN</th>
              <th class="py-3.5 px-4">Cliente / Mina</th>
              <th class="py-3.5 px-4">Sede Actual</th>
              <th class="py-3.5 px-4 text-right">Horómetro Actual</th>
              <th class="py-3.5 px-4 text-center">Telemetría</th>
              <th class="py-3.5 px-4 text-center">Estado</th>
              <th class="py-3.5 px-4 text-center">Acciones</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-gray-800 font-medium">
            <tr v-if="cargando">
              <td colspan="9" class="text-center py-8 text-gray-400">
                <div class="flex items-center justify-center space-x-2">
                  <span class="w-2.5 h-2.5 rounded-full bg-yellow-400 animate-ping"></span>
                  <span class="text-xs font-semibold text-gray-300">Cargando flota de maquinaria pesada...</span>
                </div>
              </td>
            </tr>
            <tr v-else-if="maquinariasFiltradas.length === 0">
              <td colspan="9" class="text-center py-8 text-gray-400">No se encontraron maquinarias con los filtros aplicados.</td>
            </tr>
            <tr 
              v-for="m in maquinariasFiltradas" 
              :key="m.idMaquinaria"
              class="hover:bg-gray-800/50 transition">
              
              <!-- CÓDIGO FLOTA -->
              <td class="py-3.5 px-4">
                <span class="font-extrabold text-[#FFCD11]">{{ m.codigoFlota }}</span>
              </td>

              <!-- MODELO -->
              <td class="py-3.5 px-4 text-white font-semibold">
                {{ m.modelo?.nombreComercial || m.modeloCat || 'CAT' }}
              </td>

              <!-- SERIE PIN -->
              <td class="py-3.5 px-4 font-mono text-xs text-gray-300">
                {{ m.numeroSeriePin }}
              </td>

              <!-- CLIENTE -->
              <td class="py-3.5 px-4 text-gray-200">
                {{ m.cliente?.razonSocial || '-' }}
              </td>

              <!-- SEDE -->
              <td class="py-3.5 px-4 text-xs text-gray-300">
                {{ m.sedeActual?.nombre || '-' }}
              </td>

              <!-- HORÓMETRO -->
              <td class="py-3.5 px-4 text-right font-mono font-bold text-yellow-400">
                {{ Number(m.horometroAcumulado || 0).toLocaleString('es-PE', { minimumFractionDigits: 1 }) }} hrs
              </td>

              <!-- TELEMETRÍA MINESTAR EN VIVO (PULSO VERDE) -->
              <td class="py-3.5 px-4 text-center">
                <span class="inline-flex items-center px-2 py-0.5 rounded-full text-[11px] font-bold bg-emerald-500/10 text-emerald-400 border border-emerald-500/30">
                  <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-pulse mr-1"></span>
                  ONLINE
                </span>
              </td>

              <!-- ESTADO -->
              <td class="py-3.5 px-4 text-center">
                <span :class="obtenerBadgeEstado(m.estadoOperativo)" class="px-2.5 py-1 rounded text-xs font-bold uppercase inline-block">
                  {{ m.estadoOperativo }}
                </span>
              </td>

              <!-- ACCIONES -->
              <td class="py-3.5 px-4 text-center space-x-1.5">
                <router-link 
                  :to="`/maquinaria/${m.idMaquinaria}`" 
                  class="bg-gray-800 hover:bg-gray-700 text-yellow-400 px-2.5 py-1 rounded text-xs border border-gray-700 font-semibold transition inline-block">
                  Ficha 360°
                </router-link>
                <router-link 
                  :to="`/ordenes/nueva?pin=${m.numeroSeriePin}`" 
                  class="bg-[#FFCD11] hover:bg-yellow-400 text-black px-2.5 py-1 rounded text-xs font-extrabold transition inline-block shadow">
                  Nueva OT
                </router-link>
              </td>

            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- MODAL PARA REGISTRO DE NUEVA MAQUINARIA -->
    <div v-if="showModal" class="fixed inset-0 z-50 bg-black/70 flex items-center justify-center p-4">
      <div class="bg-[#111827] border border-gray-800 rounded-xl max-w-lg w-full p-6 shadow-2xl relative">
        <h2 class="text-lg font-bold text-white mb-4 flex items-center justify-between">
          <span>Registrar Activo Caterpillar</span>
          <button @click="showModal = false" class="text-gray-400 hover:text-white">✕</button>
        </h2>

        <form @submit.prevent="guardarNuevaMaquinaria" class="space-y-3.5">
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Código de Flota</label>
              <input v-model="form.codigoFlota" type="text" placeholder="ej: CAM-049" required class="form-input" />
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Serie PIN (Chasis)</label>
              <input v-model="form.numeroSeriePin" type="text" placeholder="ej: CAT797FX00999" required class="form-input" />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Familia</label>
              <select v-model="form.familia" class="form-input">
                <option value="CAMION_MINERO">Camión Minero</option>
                <option value="PALA_HIDRAULICA">Pala Hidráulica</option>
                <option value="TRACTOR_ORUGA">Tractor de Orugas</option>
                <option value="CARGADOR_FRONTAL">Cargador de Ruedas</option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Horómetro Inicial</label>
              <input v-model.number="form.horometroAcumulado" type="number" step="0.5" placeholder="ej: 14500" required class="form-input" />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Cliente / Mina</label>
              <select v-model="form.idCliente" required class="form-input">
                <option :value="null" disabled>-- Seleccione Cliente --</option>
                <option v-for="c in clientes" :key="c.idCliente" :value="c.idCliente">
                  {{ c.razonSocial }}
                </option>
              </select>
            </div>
            <div>
              <label class="block text-xs font-semibold text-gray-400 mb-1">Sede / Taller Operativo</label>
              <select v-model="form.idSede" required class="form-input">
                <option :value="null" disabled>-- Seleccione Sede --</option>
                <option v-for="s in sedes" :key="s.idSede" :value="s.idSede">
                  {{ s.nombre }} ({{ s.region }})
                </option>
              </select>
            </div>
          </div>

          <div>
            <label class="block text-xs font-semibold text-gray-400 mb-1">Denominación Comercial</label>
            <input v-model="form.modeloCat" type="text" placeholder="ej: Camión Minero CAT 797F (400T)" required class="form-input" />
          </div>

          <div class="flex justify-end space-x-2 pt-3 border-t border-gray-800">
            <button type="button" @click="showModal = false" class="px-4 py-2 bg-gray-800 text-gray-300 rounded-lg text-xs font-semibold">Cancelar</button>
            <button type="submit" class="px-4 py-2 bg-[#FFCD11] text-black font-extrabold rounded-lg text-xs shadow hover:bg-yellow-400">Guardar Unidad</button>
          </div>
        </form>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/client'
import { useCatalogoStore } from '../stores/catalogo'

const catalogoStore = useCatalogoStore()
const maquinarias = ref([])
const cargando = ref(true)
const showModal = ref(false)

const filtroBuscar = ref('')
const filtroFamilia = ref('Todas')
const filtroEstado = ref('Todos')

const sedes = ref([])
const clientes = ref([])

const form = ref({
  codigoFlota: '',
  numeroSeriePin: '',
  modeloCat: '',
  familia: 'CAMION_MINERO',
  horometroAcumulado: 0,
  estadoOperativo: 'OPERATIVO',
  contratoMarc: 'MARC Oro (SLA 92%)',
  idCliente: null,
  idSede: null
})

const cargarCatalogos = async () => {
  try {
    const [listaSedes, listaCli] = await Promise.all([
      catalogoStore.obtenerSedes(),
      catalogoStore.obtenerClientes()
    ])
    sedes.value = listaSedes || []
    clientes.value = listaCli || []
    if (sedes.value.length > 0 && !form.value.idSede) {
      form.value.idSede = sedes.value[0].idSede
    }
    if (clientes.value.length > 0 && !form.value.idCliente) {
      form.value.idCliente = clientes.value[0].idCliente
    }
  } catch (err) {
    console.error('Error cargando sedes/clientes:', err)
  }
}

const cargarMaquinarias = async () => {
  cargando.value = true
  try {
    const res = await api.get('/maquinarias')
    maquinarias.value = res.data || []
  } catch (err) {
    console.error('Error cargando maquinarias:', err)
  } finally {
    cargando.value = false
  }
}

const maquinariasFiltradas = computed(() => {
  return maquinarias.value.filter(m => {
    // Filtro familia
    if (filtroFamilia.value !== 'Todas') {
      const fam = m.modelo?.familia || m.familia
      if (fam !== filtroFamilia.value) return false
    }
    // Filtro estado
    if (filtroEstado.value !== 'Todos' && m.estadoOperativo !== filtroEstado.value) {
      return false
    }
    // Búsqueda
    if (filtroBuscar.value.trim() !== '') {
      const q = filtroBuscar.value.toLowerCase().trim()
      const matchCod = m.codigoFlota?.toLowerCase().includes(q)
      const matchPin = m.numeroSeriePin?.toLowerCase().includes(q)
      const matchMod = (m.modelo?.nombreComercial || m.modeloCat)?.toLowerCase().includes(q)
      return matchCod || matchPin || matchMod
    }
    return true
  })
})

const obtenerBadgeEstado = (estado) => {
  switch (estado) {
    case 'OPERATIVO': return 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30'
    case 'EN_MANTENIMIENTO': return 'bg-amber-500/15 text-amber-400 border border-amber-500/30'
    case 'PARADA_CRITICA_DOWN': return 'bg-red-500/15 text-red-400 border border-red-500/30'
    case 'STAND_BY': return 'bg-blue-500/15 text-blue-400 border border-blue-500/30'
    default: return 'bg-gray-500/15 text-gray-300'
  }
}

const guardarNuevaMaquinaria = async () => {
  try {
    const payload = {
      codigoFlota: form.value.codigoFlota,
      numeroSeriePin: form.value.numeroSeriePin,
      modeloCat: form.value.modeloCat,
      familia: form.value.familia,
      horometroAcumulado: Number(form.value.horometroAcumulado || 0),
      estadoOperativo: form.value.estadoOperativo,
      contratoMarc: form.value.contratoMarc
    }
    if (form.value.idCliente) {
      payload.cliente = { idCliente: form.value.idCliente }
    }
    if (form.value.idSede) {
      payload.sedeActual = { idSede: form.value.idSede }
    }

    await api.post('/maquinarias', payload)
    showModal.value = false
    await cargarMaquinarias()
  } catch (err) {
    alert('Error al registrar maquinaria: ' + (err.response?.data?.mensaje || err.message))
  }
}

onMounted(() => {
  cargarMaquinarias()
  cargarCatalogos()
})
</script>

<style scoped>
.form-input {
  @apply w-full bg-[#1F2937] border border-gray-700 rounded-lg px-3 py-2 text-sm text-white focus:outline-none focus:border-[#FFCD11];
}
</style>
