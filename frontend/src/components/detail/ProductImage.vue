<script setup>
import mockPhoneImage from '@/assets/images/mock_phone.png';
</script>

<template>
  <div
    class="flex flex-col-reverse lg:flex-row justify-center items-center gap-6 lg:gap-12"
  >
    <div class="flex flex-row lg:flex-col justify-center items-center gap-4 lg:gap-6">
      <img class="w-16 h-20 object-contain" :src="mockPhoneImage" alt="product-image" />
      <img class="w-16 h-20 opacity-40 object-contain" :src="mockPhoneImage" alt="product-image" />
      <img class="w-16 h-20 opacity-40 object-contain" :src="mockPhoneImage" alt="product-image" />
      <img class="w-16 h-20 opacity-40 object-contain" :src="mockPhoneImage" alt="product-image" />
    </div>
    <img
      class="w-full max-w-[280px] sm:max-w-[300px] md:max-w-[320px] lg:max-w-[400px] lg:h-[520px] object-contain"
      :src="mockPhoneImage"
      alt="product-image"
    />
  </div>
</template>

<style scoped></style>
