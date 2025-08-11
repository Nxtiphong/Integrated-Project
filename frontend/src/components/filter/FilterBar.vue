<script setup>
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import BrandFilter from '../brand/BrandFilter.vue';
import PriceFilter from './PriceFilter.vue';
import StorageSizeFilter from './StorageSizeFilter.vue';

const emit = defineEmits(['filterSaleItems']);

const saleGalleryState = useGalleryStateStore();

const handleFilter = () => {
  emit('filterSaleItems');
};

const clearFilters = () => {
  saleGalleryState.filterBrandLists.splice(0, saleGalleryState.filterBrandLists.length);
  saleGalleryState.minPrice = null;
  saleGalleryState.maxPrice = null;
  saleGalleryState.filterStorageSize.splice(0, saleGalleryState.filterStorageSize.length);
  handleFilter();
};
</script>

<template>
  <div
    class="join border-slate-200 shadow-sm rounded-lg flex flex-wrap lg:flex-nowrap items-center space-x-0 lg:space-x-2 w-full bg-white border p-1 gap-2"
  >
    <div class="w-full lg:w-auto flex-1">
      <BrandFilter @filter-sale-items-by-brands="handleFilter" />
    </div>

    <div class="w-full lg:w-auto flex-1">
      <PriceFilter @filter-sale-item-by-price="handleFilter" />
    </div>

    <div class="w-full lg:w-auto flex-1">
      <StorageSizeFilter @filter-sale-item-by-storage-size="handleFilter" />
    </div>

    <div class="w-full lg:w-auto flex justify-end lg:justify-start mr-2">
      <button
        @click="clearFilters"
        class="itbms-brand-filter-clear w-full lg:w-auto rounded-md text-black font-semibold btn btn-ghost"
      >
        Clear
      </button>
    </div>
  </div>
</template>

<style scoped></style>
