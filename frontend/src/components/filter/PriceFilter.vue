<script setup>
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { Icon } from '@iconify/vue';
import { ref } from 'vue';

const emit = defineEmits(['filterSaleItemByPrice']);

const saleGalleryState = useGalleryStateStore();

const isDropdownOpen = ref(false);

const priceRange = [
  '0-5,000 Baht',
  '5,001-10,000 Baht',
  '10,001-20,000 Baht',
  '20,001-30,000 Baht',
  '30,001-40,000 Baht',
  '40,001-50,000 Baht',
  '50,001 + Baht',
];

const handleFilter = () => {
  emit('filterSaleItemByPrice');
};

// Toggle Price Selection
const togglePrice = (price) => {
  const index = saleGalleryState.filterPrice.indexOf(price);
  if (index > -1) {
    saleGalleryState.filterPrice.splice(index, 1);
  } else {
    saleGalleryState.filterPrice.push(price);
  }
  handleFilter();
};
</script>

<template>
  <section
    class="join flex items-center space-x-2 lg:space-x-4 lg:w-60 lg:border-r-2 border-slate-200 bg-white lg:p-2"
  >
    <!-- Selected Price Tags -->
    <div
      class="itbms-brand-filter flex gap-1 lg:gap-2 px-2 flex-1 w-lg overflow-x-auto scrollbar-hidden"
    >
      <p
        v-for="(price, index) in saleGalleryState.filterPrice"
        :key="index"
        class="itbms-filter-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
      >
        <span class="itbms-filter-item-name">{{ price }}</span>
        <span class="cursor-pointer itbms-filter-item-clear" @click="togglePrice(price)">
          <Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
        </span>
      </p>
      <span v-if="saleGalleryState.filterPrice.length === 0" class="text-gray-400 text-sm">
        Filter by price
      </span>
    </div>

    <!-- Price Filter Dropdown -->
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
          class="absolute right-0 mt-2 max-h-[250px] w-[200px] lg:w-[250px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
        >
          <li
            v-for="(price, index) in priceRange"
            :key="index"
            class="itbms-filter-item px-3 py-2 hover:bg-gray-100 cursor-pointer"
          >
            <label class="flex items-center space-x-2 text-sm lg:text-base w-full cursor-pointer">
              <input
                type="checkbox"
                class="form-checkbox h-4 w-4 text-blue-600"
                :checked="saleGalleryState.filterPrice.includes(price)"
                @change="togglePrice(price)"
              />
              <span>{{ price }}</span>
            </label>
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<style scoped>
.scrollbar-hidden::-webkit-scrollbar {
  display: none;
}
</style>
