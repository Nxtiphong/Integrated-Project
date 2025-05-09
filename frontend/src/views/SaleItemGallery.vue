<template>
  <div class="container mx-auto px-4 sm:px-6 lg:px-8">
    <div class="breadcrumbs text-sm my-2 overflow-x-auto whitespace-nowrap">
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
      </ul>
      <div class="itbms-sale-item-add mt-4">
        <RouterLink to="/sale-items/add">
          <button className="btn btn-outline btn-info">Add Sale Item</button>
        </RouterLink>
      </div>
    </div>

    <div class="flex flex-col lg:flex-row gap-6">
      <!-- Filters Sidebar -->
      <div class="w-full lg:w-1/4">
        <!-- Brand Filter -->
        <BrandFilter :items="saleItems" @filter-change="handleBrandFilterChange" />

        <!-- เตรียมพื้นที่สำหรับ Filter อื่นๆ ที่อาจเพิ่มในอนาคต เพราะบางอันเทียนยังไม่ได้ ใช้งานจ้า -->
        <div class="mt-4 rounded-lg shadow-sm">
          <div class="p-4 border-b border-neutral-200">
            <div
              class="flex justify-between items-center cursor-pointer"
              @click="toggleFilter('battery')"
            >
              <h3 class="font-medium">Battery capacity</h3>
              <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
              <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
            </div>
          </div>

          <div class="p-4 border-b border-neutral-200">
            <div
              class="flex justify-between items-center cursor-pointer"
              @click="toggleFilter('screen')"
            >
              <h3 class="font-medium">Screen type</h3>
              <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
              <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
            </div>
          </div>

          <div class="p-4 border-b border-neutral-200">
            <div
              class="flex justify-between items-center cursor-pointer"
              @click="toggleFilter('diagonal')"
            >
              <h3 class="font-medium">Screen diagonal</h3>
              <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
              <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
            </div>
          </div>

          <div class="p-4 border-b border-neutral-200">
            <div
              class="flex justify-between items-center cursor-pointer"
              @click="toggleFilter('protection')"
            >
              <h3 class="font-medium">Protection class</h3>
              <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
              <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
            </div>
          </div>

          <div class="p-4">
            <div
              class="flex justify-between items-center cursor-pointer"
              @click="toggleFilter('memory')"
            >
              <h3 class="font-medium">Built-in memory</h3>
              <Icon icon="lucide:chevron-right" v-if="!isExpanded" class="text-gray-400" />
              <Icon icon="lucide:chevron-down" v-else class="text-gray-400" />
            </div>
          </div>
        </div>
      </div>

      <!-- Products Grid -->
      <div class="w-full lg:w-3/4">
        <div v-if="isLoading" class="flex justify-center items-center min-h-[300px]">
          <p class="text-center text-lg">Loading...</p>
        </div>

        <div
          v-else-if="filteredItems.length > 0"
          class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 xl:grid-cols-4 gap-3 sm:gap-4 py-4 sm:py-5"
        >
          <SaleItemCard
            v-for="item in filteredItems"
            :key="item.id"
            :brand="item.brandName ?? '-'"
            :model="item.model ?? '-'"
            :ram="item.ramGb ?? '-'"
            :price="item.price ?? '-'"
            :storage="item.storageGb ?? '-'"
            :color="item.color ?? '-'"
            :image="item.image ?? ''"
            :id="item.id"
            class="w-full"
          />
        </div>

        <div
          v-else
          class="itbms-* font-medium text-primary flex justify-center items-center min-h-[300px]"
        >
          <p class="text-center text-lg">! No sale items</p>
        </div>
      </div>
    </div>
  </div>
  <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
    :duration="alertMessage.duration"
  />
</template>

<script setup>
import { onMounted, ref } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';
import BrandFilter from '@/components/Brand Filter/BrandFilter.vue';
import { Icon } from '@iconify/vue';
import { useRoute } from 'vue-router';
import Alert from '@/components/share/Alert.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';

const saleStore = useSaleItemStore();

const fetchSaleItems = async () => {
  const response = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`);
  const data = await response.json();
  return data;
};

const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
  duration: 3000,
});

const route = useRoute();

const saleItems = ref([]);
const filteredItems = ref([]);
const isLoading = ref(true);
const selectedBrands = ref([]);
const isExpanded = ref(false);
// Filter visibility states สำหรับ filter อันอื่นๆ ตอนนี้กยังไม่ได้ใช้นะ
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
  // ถ้าไม่มีแบรนด์ที่เลือก ก็จะแสดงสินค้าทั้งหมด
  if (selectedBrands.value.length === 0) {
    filteredItems.value = [...saleItems.value];
    return;
  }

  // ตัวกรองสินค้าตามแบรนด์ที่เราเลือก
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

    // เริ่มต้นแสดงสินค้าทั้งหมดหน้าก่อนเราเลือกแบรนด์อะ
    filteredItems.value = [...saleItems.value];
  } catch (error) {
    console.error('Error fetching sale items:', error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(async () => {
  try {
    loadSaleItems();
    console.log('Sale items loaded successfully');
    console.log('Sale items filteredItems:', filteredItems.value);
    if (saleStore.created) {
      alertMessage.value = {
        type: 'success',
        message: 'Sale item created successfully!',
        visible: true,
        duration: 3000,
      };
      saleStore.created = false;
    }
    if (saleStore.deleted) {
      alertMessage.value = {
        type: 'success',
        message: 'Sale item deleted successfully!',
        visible: true,
        duration: 3000,
      };
      saleStore.deleted = false;
    }

  } catch (e) {
    isLoading.value = false;
  } 

});
</script>

<style scoped>
@media (max-width: 640px) {
  .breadcrumbs {
    padding: 0.5rem 0;
  }
}
</style>