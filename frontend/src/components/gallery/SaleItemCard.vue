<template>
  <div class="itbms-row border-1 border-base-300 rounded-lg shadow-md p-4 relative">
    <button @click="toggleFavorite" class="absolute top-3 right-3 z-10 focus:outline-none">
      <Icon
        :icon="isFavorite ? 'mdi:heart' : 'mdi:heart-outline'"
        class="text-2xl"
        :class="isFavorite ? 'text-red-500' : 'text-gray-400'"
      />
    </button>

    <router-link :to="`/sale-items/${id}`" class="block">
      <img
        :src="mock_phone"
        alt="product"
        class="product-image w-full h-auto max-h-[180px] object-contain rounded-md"
      />
      <div class="mt-4 space-y-1 text-base-content">
        <p class="itbms-model font-bold text-lg">{{ model }}</p>
        <p class="itbms-brand font-semibold text-sm">
          {{ brand }}
        </p>
        <p class="itbms-ramGb">Ram: {{ ram }}</p>
        <p class="itbms-storageGb">
  Storage: 
  <span v-if="Number(storage)">{{ storage }} <span class="itbms-storageGb-unit">GB</span></span>
  <span v-else>-</span>
</p>
        <p class="itbms-Color">Color: {{ color }}</p>
        <p class="itbms-price text-black">
          Price : {{ Number(price).toLocaleString() }} <span class="itbms-price-unit">Baht</span>
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
import { Icon } from '@iconify/vue';
import { computed } from 'vue';
import { useFavoriteStore } from '@/stores/useFavoriteStore';

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
  'screen', 
]);

const favoriteStore = useFavoriteStore();

const product = {
  id: props.id,
  brand: props.brand,
  model: props.model,
  ram: props.ram,
  screen: props.screen,  
  storage: props.storage,
  price: props.price,
  color: props.color,
  image: props.image,
};

const isFavorite = computed(() => favoriteStore.isFavorite(product.id));

const toggleFavorite = () => {
  if (isFavorite.value) {
    favoriteStore.removeFavorite(product.id);
  } else {
    favoriteStore.addFavorite(product);
  }
};
</script>
