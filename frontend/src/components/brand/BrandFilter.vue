<script setup>
import { onMounted, ref } from 'vue';
import { Icon } from '@iconify/vue';
import { useGalleryFilterStore } from '@/stores/useGalleryFilterStore';

const emit = defineEmits(['filterSaleItemsByBrands']);

const brands = ref([]);
const saleGalleryFilter = useGalleryFilterStore();
const isDropdownOpen = ref(false);

const clearFilters = () => {
  saleGalleryFilter.filterLists.splice(0, saleGalleryFilter.filterLists.length);
  handleFilter();
};

const handleFilter = () => {
  emit('filterSaleItemsByBrands');
  console.log('emitting filter-sale-items-by-brands');
};

const getAllBrands = async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`);
    if (!res.ok) throw new Error('Failed to fetch brands');
    brands.value = await res.json();
    brands.value.sort((a, b) => a.name.localeCompare(b.name));
  } catch (error) {
    console.error('API error:', error);
    return { success: false, error };
  }
};

const toggleBrand = (brandName) => {
  const index = saleGalleryFilter.filterLists.indexOf(brandName);
  if (index > -1) {
    saleGalleryFilter.filterLists.splice(index, 1);
  } else {
    saleGalleryFilter.filterLists.push(brandName);
  }
  handleFilter();
};

onMounted(() => {
  getAllBrands();
});
</script>

<template>
  <section
    class="join border-secondary rounded-lg flex items-center space-x-2 lg:space-x-4 w-full bg-white border border-slate-200 lg:p-2"
  >
    <div
      class="itbms-brand-filter flex gap-1 lg:gap-2 px-2 flex-1 w-lg overflow-x-auto scrollbar-hidden"
    >
      <p
        v-for="(brand, index) in saleGalleryFilter.filterLists"
        :key="index"
        class="itbms-filter-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
      >
        <span class="itbms-filter-item-name">{{ brand }}</span>
        <span class="cursor-pointer itbms-filter-item-clear" @click="toggleBrand(brand)"
          ><Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
        </span>
      </p>
      <span v-if="saleGalleryFilter.filterLists.length === 0" class="text-gray-400 text-sm"
        >Filter by brand(s)</span
      >
    </div>

    <div class="flex items-center justify-center rounded-r-lg">
      <div class="relative inline-block text-left">
        <button
          @click="isDropdownOpen = !isDropdownOpen"
          class="itbms-brand-filter-button p-2 text-black font-semibold cursor-pointer transition duration-200"
        >
          <Icon icon="lets-icons:filter" class="text-2xl" />
        </button>

        <ul
          v-if="isDropdownOpen"
          class="absolute right-0 mt-2 max-h-[250px] w-[250px] lg:w-[550px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
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
                :checked="saleGalleryFilter.filterLists.includes(brand.name)"
                @change="toggleBrand(brand.name)"
              />
              <span>{{ brand.name }}</span>
            </label>
          </li>
        </ul>
      </div>

      <button
        @click="clearFilters"
        class="itbms-brand-filter-clear p-2 rounded-md text-black font-semibold btn btn-ghost"
      >
        Clear
      </button>
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
