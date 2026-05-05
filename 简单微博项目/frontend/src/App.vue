<template>
  <div id="app">
    <NavBar v-if="showNavBar" />
    <div class="main-container">
      <router-view :key="$route.fullPath" />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'

const route = useRoute()
const showNavBar = computed(() => !['/login', '/register'].includes(route.path))
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; }
#app { max-width: 800px; margin: 0 auto; padding: 20px; }
</style>