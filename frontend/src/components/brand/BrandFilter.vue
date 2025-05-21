<script setup>
import { onMounted, ref } from 'vue';
import { Icon } from '@iconify/vue';

const props = defineProps({
  saleItems: Array,
});

const selectedBrands = ref([]);
const brands = ref([]);

const clearFilters = () => {
  selectedBrands.value = [];
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
  if (selectedBrands.value.includes(brandName)) {
    selectedBrands.value = selectedBrands.value.filter((existBrand) => existBrand !== brandName);
  } else {
    selectedBrands.value.push(brandName);
  }
};

onMounted(() => {
  getAllBrands();
});
</script>

<template>
  <section
    class="join border-secondary rounded-lg flex items-center space-x-4 w-full bg-white border border-slate-200 p-2"
  >
    <div class="itbms-brand-filter flex gap-2 px-2 flex-1 w-lg overflow-x-auto scrollbar-hidden">
      <p
        v-for="(brand, index) in selectedBrands"
        :key="index"
        class="itbms-filter-item flex items-center select-none justify-center gap-1 bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full"
      >
        <span class="itbms-filter-item-name">{{ brand }}</span>
        <span class="cursor-pointer itbms-filter-item-clear" @click="toggleBrand(brand)"
          ><Icon icon="iconoir:delete-circle" class="text-base" />
        </span>
      </p>
      <span v-if="selectedBrands.length === 0" class="text-gray-400 text-sm"
        >Filter by brand(s)</span
      >
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
          class="dropdown-content menu p-2 shadow-md border border-slate-200 mt-2 bg-white rounded-box h-[250px] w-[500px] z-1"
        >
          <li v-for="brand in brands" :key="brand.id">
            <label class="label cursor-pointer justify-start gap-2">
              <input
                type="checkbox"
                class="checkbox checkbox-sm"
                :checked="selectedBrands.includes(brand.name)"
                @change="toggleBrand(brand.name)"
              />
              <span class="label-text">{{ brand.name }}</span>
            </label>
          </li>
        </ul>
      </div>

      <button
        @click="clearFilters"
        class="itbms-brand-filter-clear p-2 text-black font-semibold cursor-pointer rounded-r-md transition duration-200"
      >
        Clear
      </button>
    </div>
  </section>
</template>

<style scoped>
.scrollbar-hidden {
  -ms-overflow-style: none; /* Internet Explorer 10+ */
  scrollbar-width: none; /* Firefox */

  ::-webkit-scrollbar {
    display: none; /* Safari and Chrome */
  }
}
</style>
