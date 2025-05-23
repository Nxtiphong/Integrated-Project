<script setup>
import { onMounted, ref, onUnmounted } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';
import BrandFilter from '@/components/brand/BrandFilter.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import Alert from '@/components/share/Alert.vue';
import SortComponent from '@/components/gallery/SortComponent.vue';
import { useGalleryFilterStore } from '@/stores/useGalleryFilterStore';
import Pagination from '@/components/pagination/Pagination.vue';
const saleStore = useSaleItemStore();

const alertMessage = ref({
  type: 'success',
  message: '',
  visible: false,
  duration: 3000,
});

const filteredItems = ref([]);
const isLoading = ref(false);
const page = ref(1);
const totalPages = ref(1);
const sortField = ref('');
const sortDirection = ref('asc');
const pageSize = ref(5);
const { filterLists } = useGalleryFilterStore();

const fetchSaleItems = async ({
  page = 1,
  size = 10,
  filterBrands = [],
  sortField = '',
  sortDirection = 'asc',
} = {}) => {
  const params = new URLSearchParams();
  params.append('page', page - 1);
  params.append('size', size);
  if (filterBrands.length > 0) {
    filterBrands.forEach((brand) => params.append('filterBrands', brand));
  }
  if (sortField) params.append('sortField', sortField);
  if (sortDirection) params.append('sortDirection', sortDirection);

  const response = await fetch(
    `${import.meta.env.VITE_BASE_URL}/itb-mshop/v2/sale-items?${params.toString()}`,
  );
  const data = await response.json();
  return data;
};
const fetchPage = (newPage) => {
  page.value = newPage;
  loadSaleItems();
};

const handleFilterSaleItems = () => {
  fetchPage(1);
};

const handleSortChange = (val) => {
  if (val === 'none') {
    sortDirection.value = 'asc';
    sortField.value = 'id';
  } else {
    sortDirection.value = val;
    sortField.value = 'brand.name';
  }
  loadSaleItems();
};

const handlePageSizeChange = (size) => {
  pageSize.value = size;
  loadSaleItems();
};

const loadSaleItems = async () => {
  isLoading.value = true;
  try {
    const items = await fetchSaleItems({
      page: page.value,
      size: pageSize.value,
      sortField: sortField.value,
      sortDirection: sortDirection.value,
      filterBrands: filterLists,
    });
    totalPages.value = items.totalPages;
    filteredItems.value = [...items.content];
  } catch (error) {
    console.error('Error fetching sale items:', error);
  } finally {
    isLoading.value = false;
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
        <button class="itbms-sale-item-add btn btn-outline btn-info">Add Sale Item</button>
      </RouterLink>
    </div>
    <div class="p-2">
      <div class="flex flex-col gap-1 lg:gap-5">
        <div
          class="w-full transition-all duration-300 flex flex-col lg:flex-row items-center justify-between gap-8"
        >
          <BrandFilter
            :sale-items="saleItems"
            @filter-sale-items-by-brands="handleFilterSaleItems"
          />
          <div class="w-full flex items-center justify-end">
            <SortComponent @sortType="handleSortChange" @pageSize="handlePageSizeChange" />
          </div>
        </div>

        <div class="w-full lg:w-2/2">
          <div v-if="isLoading" class="flex justify-center items-center min-h-screen">
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
      <div>
        <Pagination :currentPage="page" :totalPages="totalPages" @update:currentPage="fetchPage" />
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
