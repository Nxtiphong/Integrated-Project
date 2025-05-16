<template>
<div>
  <div class="p-5">
    <div class="breadcrumbs text-sm my-2 overflow-x-auto whitespace-nowrap">
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
      </ul>
      <div class="mt-4 flex items-center space-x-2">
        <RouterLink to="/sale-items/add">
          <button className="itbms-sale-item-add btn btn-outline btn-info">Add Sale Item</button>
        </RouterLink>
      </div>
    </div>

    <div class="flex flex-col lg:flex-row gap-5">
      <div class="lg:hidden w-full mb-4">
        <button
          @click="toggleMobileFilters"
          class="w-full py-2 px-2 bg-gray-100 text-gray-700 rounded-lg flex justify-between items-center shadow-sm"
        >
          <span class="font-medium">Filters</span>
          <Icon
            :icon="showMobileFilters ? 'lucide:chevron-up' : 'lucide:chevron-down'"
            class="text-gray-500"
          />
        </button>
      </div>

      <div
        :class="[
          'w-full lg:w-1/7  transition-all duration-300 overflow-hidden',
          showMobileFilters ? 'max-h-screen' : 'max-h-0 lg:max-h-screen',
        ]"
      >
        <div class="bg-white rounded-lg shadow-sm">
          <BrandFilter :items="saleItems" @filter-change="handleBrandFilterChange" />
          <div class="mt-4 rounded-lg shadow-sm">
            <div class="p-2 border-b border-neutral-200">
              <div
                class="flex justify-between items-center cursor-pointer"
                @click="toggleFilter('battery')"
              >
                <h3 class="font-medium text-sm">Battery capacity</h3>
                <Icon
                  :icon="showBatteryFilter ? 'lucide:chevron-down' : 'lucide:chevron-right'"
                  class="text-gray-400"
                />
              </div>
              <div v-if="showBatteryFilter" class="mt-2"></div>
            </div>
            <div class="p-4 border-b border-neutral-200">
              <div
                class="flex justify-between items-center cursor-pointer"
                @click="toggleFilter('screen')"
              >
                <h3 class="font-medium text-sm">Screen type</h3>
                <Icon
                  :icon="showScreenFilter ? 'lucide:chevron-down' : 'lucide:chevron-right'"
                  class="text-gray-400"
                />
              </div>
              <div v-if="showScreenFilter" class="mt-2"></div>
            </div>
            <div class="p-4 border-b border-neutral-200">
              <div
                class="flex justify-between items-center cursor-pointer"
                @click="toggleFilter('diagonal')"
              >
                <h3 class="font-medium text-sm">Screen diagonal</h3>
                <Icon
                  :icon="showDiagonalFilter ? 'lucide:chevron-down' : 'lucide:chevron-right'"
                  class="text-gray-400"
                />
              </div>
              <div v-if="showDiagonalFilter" class="mt-2"></div>
            </div>
            <div class="p-4 border-b border-neutral-200">
              <div
                class="flex justify-between items-center cursor-pointer"
                @click="toggleFilter('protection')"
              >
                <h3 class="font-medium text-sm">Protection class</h3>
                <Icon
                  :icon="showProtectionFilter ? 'lucide:chevron-down' : 'lucide:chevron-right'"
                  class="text-gray-400"
                />
              </div>
              <div v-if="showProtectionFilter" class="mt-2"></div>
            </div>
            <div class="p-4">
              <div
                class="flex justify-between items-center cursor-pointer"
                @click="toggleFilter('memory')"
              >
                <h3 class="font-medium text-sm">Built-in memory</h3>
                <Icon
                  :icon="showMemoryFilter ? 'lucide:chevron-down' : 'lucide:chevron-right'"
                  class="text-gray-400"
                />
              </div>
              <div v-if="showMemoryFilter" class="mt-2"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="w-full lg:w-2/2">
        <div v-if="isLoading" class="flex justify-center items-center min-h-[300px]">
          <p class="text-center text-lg">Loading...</p>
        </div>

        <div
          v-else-if="filteredItems.length > 0"
          class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-5 gap-3 sm:gap-4 py-4 sm:py-5"
        >
          <SaleItemCard
            v-for="item in filteredItems"
            :key="item.id"
            :brand="item.brandName ?? '-'"
            :model="item.model ?? '-'"
            :ram="displayOrDash(item.ramGb)"
            :screen="displayOrDash(item.screen)"
            :storage="displayOrDash(item.storageGb)"
            :quantity="displayOrDash(item.quantity)"
            :price="item.price ?? '-'"
            :color="displayOrDash(item.color)"
            :image="item.image ?? ''"
            :id="item.id"
            class="w-full"
          />
        </div>

        <div v-else class="font-medium text-primary flex justify-center items-center min-h-[300px]">
          <p class="text-center text-lg">! No sale items</p>
        </div>
      </div>
    </div>
  </div>

  <div class="itbms-message">
    <Alert
      :show="alertMessage.visible"
      :type="alertMessage.type"
      :message="alertMessage.message"
      @update:show="alertMessage.visible = $event"
      :duration="alertMessage.duration"
    />
  </div>
  <!-- <div class="Pagination">
  <Pagination />
  </div> -->
</div>
</template>

<script setup>
import { onMounted, ref, onUnmounted } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';
import BrandFilter from '@/components/Brand Filter/BrandFilter.vue';
import { Icon } from '@iconify/vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import Alert from '@/components/share/Alert.vue';
// import Pagination from '@/components/Pagination/Pagination.vue';
const saleStore = useSaleItemStore();

const alertMessage = ref({
  type: '',
  message: '',
  visible: false,
  duration: 3000,
});

const fetchSaleItems = async () => {
  const response = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`);
  const data = await response.json();
  return data;
};

const saleItems = ref([]);
const filteredItems = ref([]);
const isLoading = ref(true);
const selectedBrands = ref([]);
const refreshInterval = ref(null);

const showMobileFilters = ref(false);
const toggleMobileFilters = () => {
  showMobileFilters.value = !showMobileFilters.value;
};

const showBatteryFilter = ref(false);
const showScreenFilter = ref(false);
const showDiagonalFilter = ref(false);
const showProtectionFilter = ref(false);
const showMemoryFilter = ref(false);

const toggleFilter = (filterName) => {
  switch (filterName) {
    case 'battery':
      showBatteryFilter.value = !showBatteryFilter.value;
      break;
    case 'screen':
      showScreenFilter.value = !showScreenFilter.value;
      break;
    case 'diagonal':
      showDiagonalFilter.value = !showDiagonalFilter.value;
      break;
    case 'protection':
      showProtectionFilter.value = !showProtectionFilter.value;
      break;
    case 'memory':
      showMemoryFilter.value = !showMemoryFilter.value;
      break;
  }
};

const handleBrandFilterChange = (brands) => {
  selectedBrands.value = brands;
  applyFilters();
};

const applyFilters = () => {
  if (selectedBrands.value.length === 0) {
    filteredItems.value = [...saleItems.value];
    return;
  }

  filteredItems.value = saleItems.value.filter((item) => {
    const brandName = item.brandName || 'Unknown';
    return selectedBrands.value.includes(brandName);
  });
};

const loadSaleItems = async () => {
  isLoading.value = true;
  try {
    const items = await fetchSaleItems();
    saleItems.value = items;

    if (saleItems.value.length > 0) {
      saleItems.value.sort((a, b) => {
        const updatedA = new Date(b.createdOn);
        const updatedB = new Date(a.createdOn);
        return updatedA - updatedB;
      });
    }

    filteredItems.value = [...saleItems.value];
  } catch (error) {
    console.error('Error fetching sale items:', error);
  } finally {
    isLoading.value = false;
  }
};

const handleVisibilityChange = () => {
  if (document.visibilityState === 'visible') {
    loadSaleItems();
  }
};

const displayOrDash = (val) => {
  return val === null || val === undefined || val === '' || val === 0 ? '-' : val;
};

onMounted(() => {
  loadSaleItems();
  if (saleStore.created) {
    alertMessage.value = {
      type: 'success',
      message: 'The sale item has been successfully added.',
      visible: true,
      duration: 3000,
    };
    saleStore.created = false;
  }
  if (saleStore.deleted) {
    alertMessage.value = {
      type: 'success',
      message: 'The sale item has been deleted.',
      visible: true,
      duration: 3000,
    };
    saleStore.deleted = false;
  }

  document.addEventListener('visibilitychange', handleVisibilityChange);
});

onUnmounted(() => {
  if (refreshInterval.value) {
    clearInterval(refreshInterval.value);
  }
  document.removeEventListener('visibilitychange', handleVisibilityChange);
});
</script>

<style scoped>
@media (max-width: 640px) {
  .breadcrumbs {
    padding: 0.5rem 0;
  }
}
@media (max-width: 1023px) {
  .max-h-0 {
    margin-bottom: 0;
  }
  .max-h-screen {
    margin-bottom: 1rem;
  }
}
</style>
