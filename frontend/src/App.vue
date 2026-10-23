<template>
  <!-- Si estamos en login, mostramos la pantalla limpia -->
  <div v-if="$route.path === '/login'">
    <router-view />
  </div>

  <!-- Si estamos en el sistema operativo, usamos la ÚNICA PLANTILLA MAESTRA CORPORATIVA (AppLayout) -->
  <AppLayout v-else>
    <router-view />
  </AppLayout>
</template>

<script setup>
import AppLayout from './layouts/AppLayout.vue'
</script>
