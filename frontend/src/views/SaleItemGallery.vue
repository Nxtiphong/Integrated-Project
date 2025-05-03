<template>
  <div class="container mx-auto px-4 sm:px-6 lg:px-8">
    <div class="breadcrumbs text-sm my-3 overflow-x-auto whitespace-nowrap">
      <ul class="flex space-x-2">
        <li class="flex items-center">
          <RouterLink to="/" class="hover:text-primary transition-colors">Home</RouterLink>
          <span class="mx-2 text-gray-400"></span>
        </li>
        <li>
          <RouterLink to="/sale-items" class="hover:text-primary transition-colors">Gallery</RouterLink>
        </li>
      </ul>
    </div>
    
    <div v-if="saleItems.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-3 sm:gap-4 py-4 sm:py-5">
      <SaleItemCard
        v-for="item in saleItems"
        :key="item.id"
        :brand="item.brandName ?? '-'"
        :model="item.model ?? '-'"
        :ram="item.ramGb ?? '-'"
        :price="item.price ?? '-'"
        :storage="item.storageGb ?? '-'"
        :color="item.color ?? '-'"
        :image="item.image ?? ''"
        :id="item.id"
        class="w-full"
      />
    </div>
    
    <div
      class="itbms-* font-medium text-primary flex justify-center items-center min-h-[300px]"
      v-else
    >
      <p class="text-center text-lg">! No sale items</p>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';

const fetchSaleItems = async () => {
  const response = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`);
  const data = await response.json();
  return data;
};

const saleItems = ref([]);
const loadSaleItems = async () => {
  try {
    const items = await fetchSaleItems();
    saleItems.value = items;
    console.log('Sale items:', saleItems.value);
  } catch (error) {
    console.error('Error fetching sale items:', error);
  }
};

onMounted(async () => {
  await loadSaleItems();
  if (saleItems.value.length > 0) {
    saleItems.value.sort((a, b) => {
      const updatedA = new Date(b.createdOn);
      const updatedB = new Date(a.createdOn);
      return updatedA - updatedB;
    });
  }
});
</script>

<style scoped>
@media (max-width: 640px) {
  .breadcrumbs {
    padding: 0.5rem 0;
  }
}
</style>