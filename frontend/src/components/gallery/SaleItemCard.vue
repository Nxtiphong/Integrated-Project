<template>
  <div class="Itbms-row border-1 border-base-300 rounded-lg shadow-md p-4 bg-[#F6F6F6]">
    <router-link :to="`/sale-items/${id}`" class="block">
      <img
        :src="mock_phone"
        alt="product"
        class="product-image w-full h-auto max-h-[180px] object-contain rounded-md"
      />
      <div class="mt-4 space-y-1 text-base-content">
        <p class="Itbms-brand">
          <strong>{{ brand }}</strong>
        </p>
        <p class="Itbms-model">{{ model }}</p>
        <p class="Itbms-ramGb">Ram: {{ ram }}</p>
        <p class="Itbms-storageGb">
          Storage: {{ storage }} <span class="Itbms-storageGb-unit">GB</span>
        </p>
        <p class="Itbms-Color">Color: {{ color }}</p>
        <p class="Itbms-price text-black">
          Price : {{ Number(price).toLocaleString() }} <span class="Itbms-price-unit">Baht</span>
        </p>
      </div>
    </router-link>
    <div class="mt-4">
      <button class="btn btn-primary text-white w-full rounded-box">Buy</button>
    </div>
  </div>
</template>

<script setup>
import mock_phone from '@/assets/images/mock_phone.png';
import { RouterLink } from 'vue-router';

const props = defineProps([
  'brand',
  'model',
  'ram',
  'storage',
  'color',
  'price',
  'image',
  'id',
  'ramGb',
]);
</script>
