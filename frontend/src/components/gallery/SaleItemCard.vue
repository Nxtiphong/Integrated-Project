<template>
  <div class="itbms-row border-1 border-base-300 rounded-lg shadow-md p-4 relative flex flex-col h-full">
    <button @click="toggleFavorite" class="absolute top-3 right-3 z-10 focus:outline-none">
      <Icon
        :icon="isFavorite ? 'mdi:heart' : 'mdi:heart-outline'"
        class="text-2xl"
        :class="isFavorite ? 'text-red-500' : 'text-gray-400'"
      />
    </button>

    <router-link :to="`/sale-items/${id}`" class="block" flex-grow>
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
        <p class="itbms-ramGb text-sm">Ram: {{ ram }}</p>
        <p class="itbms-storageGb text-sm">
          Storage
          <span v-if="Number(storage)"
            >{{ storage }} <span class="itbms-storageGb-unit">GB</span></span
          >
          <span v-else>-</span>
        </p>
        <p class="itbms-Color text-sm">Color: {{ color }}</p>
        <p class="itbms-price text-black text-sm">
          Price : {{ Number(price).toLocaleString() }} <span class="itbms-price-unit">Baht</span>
        </p>
      </div>
    </router-link>

    <div class="mt-auto">
      <button class="btn btn-primary w-full">Buy</button>
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
