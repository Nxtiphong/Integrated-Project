<script setup>
import { RouterView, useRoute } from 'vue-router';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';

const route = useRoute();
</script>

<template>
  <main>
    <Navbar v-if="!route.meta.hideLayout" />
    <RouterView />
    <Footer v-if="!route.meta.hideLayout" />
  </main>
</template>

<style scoped></style>
