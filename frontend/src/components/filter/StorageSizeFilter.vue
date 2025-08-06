<script setup>
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { Icon } from '@iconify/vue';
import { ref } from 'vue';

const emit = defineEmits(['filterSaleItemByStorageSize']);

const saleGalleryState = useGalleryStateStore();

const isDropdownOpen = ref(false);

const priceRange = ['32Gb', '64Gb', '128Gb', '256Gb', '512Gb', '1Tb+'];

const handleFilter = () => {
  emit('filterSaleItemByStorageSize');
};

// Toggle Price Selection
const toggleStorageSize = (size) => {
  const index = saleGalleryState.filterStorageSize.indexOf(size);
  if (index > -1) {
    saleGalleryState.filterStorageSize.splice(index, 1);
  } else {
    saleGalleryState.filterStorageSize.push(size);
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
        v-for="(size, index) in saleGalleryState.filterStorageSize"
        :key="index"
        class="itbms-filter-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
      >
        <span class="itbms-filter-item-name">{{ size }}</span>
        <span class="cursor-pointer itbms-filter-item-clear" @click="toggleStorageSize(size)">
          <Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
        </span>
      </p>
      <span v-if="saleGalleryState.filterStorageSize.length === 0" class="text-gray-400 text-sm">
        Filter by storage size
      </span>
    </div>

    <!-- Price Filter Dropdown -->
    <div class="flex items-center justify-center rounded-r-lg">
      <div class="relative inline-block text-left">
        <button
          @click="isDropdownOpen = !isDropdownOpen"
          class="itbms-storage-size-filter-button p-2 text-black font-semibold cursor-pointer transition duration-200"
        >
          <Icon icon="lets-icons:filter" class="text-2xl" />
        </button>

        <ul
          v-if="isDropdownOpen"
          class="absolute right-0 mt-2 max-h-[250px] w-[150px] lg:w-[200px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
        >
          <li
            v-for="(size, index) in priceRange"
            :key="index"
            class="itbms-filter-item px-3 py-2 hover:bg-gray-100 cursor-pointer"
          >
            <label class="flex items-center space-x-2 text-sm lg:text-base w-full cursor-pointer">
              <input
                type="checkbox"
                class="form-checkbox h-4 w-4 text-blue-600"
                :checked="saleGalleryState.filterStorageSize.includes(size)"
                @change="toggleStorageSize(size)"
              />
              <span>{{ size }}</span>
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
