<script setup>
import { onMounted, ref } from 'vue';
import { Icon } from '@iconify/vue';
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { httpRequest } from '@/utils/fetchUtils';

const emit = defineEmits(['filterSaleItemsByBrands']);

const brands = ref([]);
const saleGalleryState = useGalleryStateStore();
const isDropdownOpen = ref(false);

const handleFilter = () => {
  emit('filterSaleItemsByBrands');
};

const getAllBrands = async () => {
  try {
    const res = await httpRequest('GET', `v1/brands`);
    brands.value = res.data;
    brands.value.sort((a, b) => a.name.localeCompare(b.name));
  } catch (error) {
    console.error('API error:', error);
    return { success: false, error };
  }
};

const toggleBrand = (brandName) => {
  const index = saleGalleryState.filterBrandLists.indexOf(brandName);
  if (index > -1) {
    saleGalleryState.filterBrandLists.splice(index, 1);
  } else {
    saleGalleryState.filterBrandLists.push(brandName);
  }
  handleFilter();
};

onMounted(() => {
  getAllBrands();
});
</script>

<template>
  <section
    class="join flex items-center space-x-2 lg:space-x-4 lg:w-60 lg:border-r-2 border-slate-200 bg-white lg:p-2"
  >
    <div
      class="itbms-brand-filter flex gap-1 lg:gap-2 px-2 flex-1 w-lg overflow-x-auto scrollbar-hidden"
    >
      <div class="flex items-start flex-col gap-[2px]">
        <span
          class="itbms-price-filter text-black text-sm cursor-pointer"
          @click="isDropdownOpen = !isDropdownOpen"
        >
          Brand
        </span>
        <div
          class="flex items-start gap-1 flex-1 w-xs md:w-[40rem] lg:w-55 overflow-x-auto scrollbar-hidden"
        >
          <p
            v-for="(brand, index) in saleGalleryState.filterBrandLists"
            :key="index"
            class="itbms-brand-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
          >
            <span class="itbms-brand-item-name">{{ brand }}</span>
            <span class="cursor-pointer itbms-brand-item-clear" @click="toggleBrand(brand)"
              ><Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
            </span>
          </p>
        </div>
        <span v-if="saleGalleryState.filterBrandLists.length === 0" class="text-gray-400 text-sm"
          >Filter by brand(s)</span
        >
      </div>
    </div>

    <div class="flex items-center justify-center rounded-r-lg">
      <div class="relative inline-block text-left">
        <ul
          v-if="isDropdownOpen"
          class="absolute right-8 top-10 max-h-[250px] w-[150px] lg:w-[200px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
        >
          <li
            v-for="brand in brands"
            :key="brand.id"
            class="itbms-filter-item px-3 py-2 hover:bg-gray-100 cursor-pointer"
            :data-brand-name="brand.name"
          >
            <label class="flex items-center space-x-2 text-sm lg:text-base w-full cursor-pointer">
              <input
                type="checkbox"
                class="form-checkbox h-4 w-4 text-blue-600"
                :checked="saleGalleryState.filterBrandLists.includes(brand.name)"
                @change="toggleBrand(brand.name)"
              />
              <span>{{ brand.name }}</span>
            </label>
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<style scoped>
.scrollbar-hidden {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
::-webkit-scrollbar {
  display: none;
}
</style>
