
<script setup>
import { useRouter } from 'vue-router';
import notFoundImg from '@/assets/images/404.png';

const router = useRouter();
</script>

<template>
  <div>
    <div
      class="flex flex-col items-center justify-center m-10 p-5 shadow-lg border rounded-2xl bg-white border-neutral-200 gap-2"
    >
      <img :src="notFoundImg" alt="not_found" />
      <h1 class="text-3xl font-bold uppercase">Something is wrong</h1>
      <p class="itbms-message">The requested sale item does not exist.</p>
      <button @click="router.push('/sale-items')" class="itbms-button btn btn-primary my-2">
        Back
      </button>
    </div>
  </div>
</template>

<style scoped></style>
