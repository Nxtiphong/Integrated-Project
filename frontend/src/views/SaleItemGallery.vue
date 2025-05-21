<script setup>
import { onMounted, ref, onUnmounted } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';
import BrandFilter from '@/components/brand/BrandFilter.vue';
import { Icon } from '@iconify/vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import Alert from '@/components/share/Alert.vue';
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

<template>
  <div class="container mx-auto">
    <div
      class="breadcrumbs text-sm overflow-x-auto whitespace-nowrap my-2 flex items-center justify-between"
    >
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
      </ul>
      <RouterLink to="/sale-items/add">
        <button className="itbms-sale-item-add btn btn-outline btn-info">Add Sale Item</button>
      </RouterLink>
    </div>
    <div class="p-2">
      <div class="flex flex-col gap-1 lg:gap-5">
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
            'w-full  transition-all duration-300 flex items-center justify-between gap-8',
            showMobileFilters ? 'max-h-screen' : 'max-h-0 lg:max-h-screen',
          ]"
        >
          <BrandFilter :sale-items="saleItems" @filter-change="handleBrandFilterChange" />
          <div class="border w-full">
            <!-- Sort Here -->
          </div>
        </div>

        <div class="w-full lg:w-2/2">
          <div v-if="isLoading" class="flex justify-center items-center min-h-[300px]">
            <p class="text-center text-lg">Loading...</p>
          </div>

          <div
            v-else-if="filteredItems.length > 0"
            class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-5 gap-3 sm:gap-4 py-1 sm:py-2"
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

          <div
            v-else
            class="font-medium text-primary flex justify-center items-center min-h-[300px]"
          >
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
  </div>
</template>

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
