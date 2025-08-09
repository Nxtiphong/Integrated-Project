<script setup>
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { Icon } from '@iconify/vue';
import { ref } from 'vue';

const emit = defineEmits(['filterSaleItemByStorageSize']);

const saleGalleryState = useGalleryStateStore();

const isDropdownOpen = ref(false);

const storageSize = ['32Gb', '64Gb', '128Gb', '256Gb', '512Gb', '1Tb', 'Not specified'];

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
    class="join flex items-start space-x-2 lg:space-x-4 lg:w-60 lg:border-r-2 border-slate-200 bg-white"
  >
    <div class="flex gap-1 lg:gap-2 px-2 flex-1">
      <div class="flex items-start flex-col gap-[2px]">
        <span
          class="itbms-storage-size-filter text-black text-sm cursor-pointer"
          @click="isDropdownOpen = !isDropdownOpen"
        >
          Storage Size
        </span>
        <div class="flex items-start gap-1 flex-1 w-55 overflow-x-auto scrollbar-hidden">
          <p
            v-for="(size, index) in saleGalleryState.filterStorageSize"
            :key="index"
            class="itbms-storage-size-item flex items-center select-none justify-center gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
          >
            <span class="itbms-storage-size-item">{{ size }}</span>
            <span
              class="cursor-pointer itbms-storage-size-item-clear z-20"
              @click="toggleStorageSize(size)"
            >
              <Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
            </span>
          </p>
        </div>
        <span v-if="saleGalleryState.filterStorageSize.length === 0" class="text-gray-400 text-sm">
          Storage size(s)
        </span>
      </div>
    </div>

    <div class="flex items-center justify-center rounded-r-lg">
      <div class="relative inline-block text-left">
        <ul
          v-if="isDropdownOpen"
          class="absolute right-8 top-15 max-h-[250px] w-[150px] lg:w-[200px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
        >
          <li
            v-for="(size, index) in storageSize"
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
.scrollbar-hidden {
  scrollbar-width: none;
  -ms-overflow-style: none;
}
::-webkit-scrollbar {
  display: none;
}
</style>
