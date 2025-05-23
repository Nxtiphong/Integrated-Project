<script setup>
import { onMounted, ref } from 'vue';
import { Icon } from '@iconify/vue';
import { useGalleryFilterStore } from '@/stores/useGalleryFilterStore';

const props = defineProps({
  saleItems: Array,
});

const emit = defineEmits(['filterSaleItemsByBrands']);

const brands = ref([]);
const { filterLists } = useGalleryFilterStore();

const clearFilters = () => {
  filterLists.splice(0, filterLists.length);
  handleFilter();
};

const handleFilter = () => {
  emit('filterSaleItemsByBrands');
};

const getAllBrands = async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`);
    if (!res.ok) throw new Error('Failed to fetch brands');
    brands.value = await res.json();
  } catch (error) {
    console.error('API error:', error);
    return { success: false, error };
  }
};

const toggleBrand = (brandName) => {
  const index = filterLists.indexOf(brandName);
  if (index > -1) {
    filterLists.splice(index, 1);
  } else {
    filterLists.push(brandName);
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
        v-for="(brand, index) in filterLists"
        :key="index"
        class="itbms-filter-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
      >
        <span class="itbms-filter-item-name">{{ brand }}</span>
        <span class="cursor-pointer itbms-filter-item-clear" @click="toggleBrand(brand)"
          ><Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
        </span>
      </p>
      <span v-if="filterLists.length === 0" class="text-gray-400 text-sm">Filter by brand(s)</span>
    </div>

    <div class="flex items-center justify-center rounded-r-lg">
      <div class="dropdown dropdown-bottom dropdown-end">
        <button
          tabindex="0"
          class="itbms-brand-filter-button p-2 text-black font-semibold cursor-pointer transition duration-200"
        >
          <Icon icon="lets-icons:filter" class="text-2xl" />
        </button>

        <ul
          tabindex="0"
          class="dropdown-content lg:menu p-2 shadow-md border border-slate-200 mt-2 bg-white rounded-box h-[250px] w-[250px] lg:w-[550px] overflow-y-auto z-1"
        >
          <li v-for="brand in brands" :key="brand.id">
            <label class="label cursor-pointer justify-start text-xs lg:text-base gap-2">
              <input
                type="checkbox"
                class="checkbox checkbox-xs lg:checkbox-sm"
                :checked="filterLists.includes(brand.name)"
                @change="toggleBrand(brand.name)"
              />
              <span class="label-text">{{ brand.name }}</span>
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
