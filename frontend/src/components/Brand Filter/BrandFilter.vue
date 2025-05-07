<template>
  <div class=" rounded-lg shadow-sm">
    <div class="p-4 ">
      <div class="flex justify-between items-center cursor-pointer" @click="toggleFilter">
        <h3 class="font-medium">Brand</h3>
        <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
        <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
      </div>

      <div v-if="isExpanded" class="mt-3">
        <div class="relative mb-3">
          <input
            type="text"
            v-model="brandSearch"
            placeholder="Search"
            class="w-full py-2 px-3 pl-9 border rounded-md bg-gray-50"
          />
          <span class="absolute inset-y-0 left-3 flex items-center text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </span>
        </div>
        
        <div class="max-h-60 overflow-y-auto">
          <div 
            v-for="brand in filteredBrands" 
            :key="brand.name"
            class="flex items-center mb-2"
          >
            <input 
              type="checkbox" 
              :id="brand.name" 
              v-model="brand.selected"
              class="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
              @change="updateFilters"
            />
            <label :for="brand.name" class="ml-2 text-sm">
              {{ brand.name }} 
              <span class="text-gray-500 text-xs">{{ brand.count }}</span>
            </label>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Icon } from '@iconify/vue';
import { ref, computed, watch, onMounted } from 'vue';

const props = defineProps({
  items: {
    type: Array,
    required: true
  }
});

const emit = defineEmits(['filter-change']);

const isExpanded = ref(true);
const brandSearch = ref('');
const brandsList = ref([]);

const filteredBrands = computed(() => {
  if (!brandSearch.value) {
    return brandsList.value;
  }
  
  return brandsList.value.filter(brand => 
    brand.name.toLowerCase().includes(brandSearch.value.toLowerCase())
  );
});

const toggleFilter = () => {
  isExpanded.value = !isExpanded.value;
};

const generateBrandsList = () => {
  const brandsMap = new Map();
  
  // Count items by brand
  props.items.forEach(item => {
    const brandName = item.brandName || 'Unknown';
    if (brandsMap.has(brandName)) {
      brandsMap.set(brandName, brandsMap.get(brandName) + 1);
    } else {
      brandsMap.set(brandName, 1);
    }
  });
  
  const brandsArray = Array.from(brandsMap.entries()).map(([name, count]) => ({
    name,
    count,
    selected: false
  }));
  

  brandsArray.sort((a, b) => a.name.localeCompare(b.name));
  
  brandsList.value = brandsArray;
};

const updateFilters = () => {
  const selectedBrands = brandsList.value
    .filter(brand => brand.selected)
    .map(brand => brand.name);
  
  emit('filter-change', selectedBrands);
};

watch(() => props.items, () => {
  generateBrandsList();
}, { deep: true });

onMounted(() => {
  generateBrandsList();
});
</script>