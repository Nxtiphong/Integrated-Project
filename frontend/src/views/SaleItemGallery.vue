<template>
  <div class="container mx-auto px-4 sm:px-6 lg:px-8">
    <div class="breadcrumbs text-sm my-2 overflow-x-auto whitespace-nowrap">
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items"
            >Gallery</RouterLink
          >
        </li>
      </ul>
      <div class="mt-4">
        <RouterLink to="/sale-items/add">
        <button className="btn btn-outline btn-info">Add Sale Item</button>
      </RouterLink>
      </div>
    </div>
    
    <div v-if="isLoading" class="flex justify-center items-center min-h-[300px]">
      <p class="text-center text-lg">Loading...</p>
    </div>
    
    <div 
      v-else-if="saleItems.length > 0" 
      class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-3 sm:gap-4 pb-4 pt-0 sm:py-2"
    >
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
      v-else
      class="itbms-* font-medium text-primary flex justify-center items-center min-h-[300px]"
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
const isLoading = ref(true); 

const loadSaleItems = async () => {
  isLoading.value = true; 
  try {
    const items = await fetchSaleItems();
    saleItems.value = items;
    console.log('Sale items:', saleItems.value);
    
    if (saleItems.value.length > 0) {
      saleItems.value.sort((a, b) => {
        const updatedA = new Date(b.createdOn);
        const updatedB = new Date(a.createdOn);
        return updatedA - updatedB;
      });
    }
  } catch (error) {
    console.error('Error fetching sale items:', error);
  } finally {
    isLoading.value = false; 
  }
};

onMounted(() => {
  loadSaleItems();
});
</script>

<style scoped>
@media (max-width: 640px) {
  .breadcrumbs {
    padding: 0.5rem 0;
  }
}
</style>
