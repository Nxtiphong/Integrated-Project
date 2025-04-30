<script setup>
import mockPhoneImage from '@/assets/images/mock_phone.png'
</script>

<template>
  <div class="flex justify-start items-center gap-12">
    <div class="flex flex-col justify-start items-center gap-6">
      <img class="w-18 h-24 object-contain" :src="mockPhoneImage" />
      <img class="w-18 h-24 opacity-40 object-contain" :src="mockPhoneImage" />
      <img class="w-18 h-24 opacity-40 object-contain" :src="mockPhoneImage" />
      <img class="w-18 h-24 opacity-40 object-contain" :src="mockPhoneImage" />
    </div>
    <img class="w-auto h-[520px] object-contain" :src="mockPhoneImage" />
  </div>
</template>

<style scoped></style>
