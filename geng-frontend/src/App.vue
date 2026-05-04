<template>
  <NavBar />
  <main class="page-main">
    <RouterView />
  </main>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
</script>

<style>
.page-main {
  padding-top: calc(var(--nav-h) + 32px);
  min-height: 100vh;
}
</style>
