<script setup>
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { Icon } from '@iconify/vue';
import { computed, ref } from 'vue';
import Input from '../Input.vue';

const emit = defineEmits(['filterSaleItemByPrice']);

const saleGalleryState = useGalleryStateStore();

const isDropdownOpen = ref(false);

const priceRange = [
  { label: '0-5,000 Baht', min: 0, max: 5000 },
  { label: '5,001-10,000 Baht', min: 5001, max: 10000 },
  { label: '10,001-20,000 Baht', min: 10001, max: 20000 },
  { label: '20,001-30,000 Baht', min: 20001, max: 30000 },
  { label: '30,001-40,000 Baht', min: 30001, max: 40000 },
  { label: '40,001-50,000 Baht', min: 40001, max: 50000 },
];

const handleFilter = () => {
  emit('filterSaleItemByPrice');
};

const togglePrice = (range) => {
  saleGalleryState.minPrice = range.min;
  saleGalleryState.maxPrice = range.max;
  handleFilter();
};

const clearPriceRange = () => {
  saleGalleryState.minPrice = null;
  saleGalleryState.maxPrice = null;
  handleFilter();
};

// To do Later
// const isError = computed(() => ({
//   minPrice:
//     saleGalleryState.minPrice !== null &&
//     saleGalleryState.maxPrice !== null &&
//     saleGalleryState.minPrice > saleGalleryState.maxPrice,
//   maxPrice:
//     saleGalleryState.maxPrice !== null &&
//     saleGalleryState.minPrice !== null &&
//     saleGalleryState.maxPrice < saleGalleryState.minPrice,
// }));

const displayPrice = computed(() => {
  const match = priceRange.find(
    (p) =>
      p.min === Number(saleGalleryState.minPrice) && p.max === Number(saleGalleryState.maxPrice),
  );

  if (match) return [match];

  if (saleGalleryState.minPrice !== null && saleGalleryState.maxPrice !== null) {
    return [
      {
        label: `${saleGalleryState.minPrice.toLocaleString()} - ${saleGalleryState.maxPrice.toLocaleString()} Baht`,
        min: saleGalleryState.minPrice,
        max: saleGalleryState.maxPrice,
      },
    ];
  }
  if (saleGalleryState.minPrice !== null && saleGalleryState.maxPrice === null) {
    return [
      {
        label: `${saleGalleryState.minPrice.toLocaleString()} Baht+`,
        min: saleGalleryState.minPrice,
        max: null,
      },
    ];
  }
  if (saleGalleryState.minPrice === null && saleGalleryState.maxPrice !== null) {
    return [
      {
        label: `Up to ${saleGalleryState.maxPrice.toLocaleString()} Baht`,
        min: null,
        max: saleGalleryState.maxPrice,
      },
    ];
  }

  return null;
});
</script>

<template>
  <section
    class="join flex items-start space-x-2 lg:space-x-4 lg:w-60 lg:border-r-2 border-slate-200 bg-white"
  >
    <div class="flex gap-1 lg:gap-2 px-2 flex-1">
      <div class="flex items-start flex-col gap-[2px]">
        <span
          class="itbms-price-filter text-black text-sm cursor-pointer"
          @click="isDropdownOpen = !isDropdownOpen"
        >
          Price
        </span>
        <div
          class="flex items-start gap-1 flex-1 w-xs md:w-[40rem] lg:w-55 overflow-x-auto scrollbar-hidden"
        >
          <p
            v-for="(price, index) in displayPrice"
            :key="index"
            class="itbms-price-item flex items-center select-none gap-1 bg-primary/80 text-white text-xs lg:text-sm font-medium px-2 lg:px-3 py-1 rounded-full"
          >
            <span class="itbms-price-item w-34">{{ price.label }}</span>
            <span
              class="cursor-pointer itbms-storage-price-item-clear z-20"
              @click="clearPriceRange()"
            >
              <Icon icon="iconoir:delete-circle" class="text-sm lg:text-base" />
            </span>
          </p>
        </div>
        <span
          v-if="saleGalleryState.minPrice === null && saleGalleryState.maxPrice === null"
          class="text-gray-400 text-sm"
        >
          Price Range
        </span>
      </div>
    </div>

    <div class="flex items-center justify-center rounded-r-lg">
      <div class="relative inline-block text-left">
        <ul
          v-if="isDropdownOpen"
          class="absolute right-8 top-15 max-h-[250px] w-[250px] lg:w-[220px] overflow-y-auto rounded-md border border-gray-300 bg-white shadow-md z-50"
        >
          <li
            v-for="(price, index) in priceRange"
            :key="index"
            class="itbms-filter-item px-3 py-2 hover:bg-gray-100 cursor-pointer"
          >
            <label class="flex items-center space-x-2 text-sm lg:text-base w-full cursor-pointer">
              <input
                type="radio"
                class="form-radio h-4 w-4 text-blue-600"
                :checked="
                  saleGalleryState.minPrice === price.min && saleGalleryState.maxPrice === price.max
                "
                @change="togglePrice(price)"
              />
              <span>{{ price.label }}</span>
            </label>
          </li>
          <li class="px-3 py-2">
            <div class="flex flex-col gap-2">
              <Input
                type="number"
                v-model.number="saleGalleryState.minPrice"
                placeholder="Min Price"
                class="form-input w-full text-sm px-2 py-1 border rounded"
                min="0"
                :custom-class="'itbms-price-item-min'"
              />
              <Input
                type="number"
                v-model.number="saleGalleryState.maxPrice"
                placeholder="Max Price"
                class="form-input w-full text-sm px-2 py-1 border rounded"
                min="0"
                :custom-class="'itbms-price-item-max'"
              />
              <button
                class="mt-1 bg-primary text-white text-xs px-2 py-1 rounded hover:bg-primary/90"
                @click="handleFilter"
              >
                Apply
              </button>
            </div>
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
