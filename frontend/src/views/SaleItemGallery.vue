<script setup>
import { onMounted, ref, computed } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue';
import BrandFilter from '@/components/brand/BrandFilter.vue';
import SortComponent from '@/components/gallery/SortComponent.vue';
import Pagination from '@/components/pagination/Pagination.vue';
import Alert from '@/components/share/Alert.vue';
import { useSaleItemStore } from '@/stores/useSaleItemStore';
import { useGalleryStateStore } from '@/stores/useGalleryStateStore';
import { httpRequest } from '@/utils/fetchUtils';

const saleStore = useSaleItemStore();
const galleryState = useGalleryStateStore();

const filteredItems = ref([]);
const isLoading = ref(false);
const alertMessage = ref({
  type: 'success',
  message: '',
  visible: false,
  duration: 3000,
});

const hasItems = computed(() => filteredItems.value.length > 0);
const showLoading = computed(() => isLoading.value);
const showNoItems = computed(() => !isLoading.value && !hasItems.value);

const displayOrDash = (val) => {
  return val === null || val === undefined || val === '' || val === 0 ? '-' : val;
};

const showAlert = (message, type = 'success') => {
  alertMessage.value = {
    type,
    message,
    visible: true,
    duration: 3000,
  };
};

const fetchSaleItems = async (params = {}) => {
  const { page = 1, size = 10, filterBrands = [], sortField = '', sortDirection = 'asc' } = params;

  const urlParams = new URLSearchParams();
  urlParams.append('page', page - 1);
  urlParams.append('size', size);

  filterBrands.forEach((brand) => urlParams.append('filterBrands', brand));

  if (sortField) urlParams.append('sortField', sortField);
  if (sortDirection !== 'none') urlParams.append('sortDirection', sortDirection);

  const response = await httpRequest('GET', `v2/sale-items?${urlParams.toString()}`);

  return response.data;
};

const loadSaleItems = async () => {
  isLoading.value = true;

  try {
    const items = await fetchSaleItems({
      page: galleryState.page,
      size: galleryState.pageSize,
      sortField: galleryState.sortField,
      sortDirection: galleryState.sortDirection,
      filterBrands: galleryState.filterLists,
    });

    galleryState.totalPages = items.totalPages;
    filteredItems.value = [...items.content];
  } catch (error) {
    console.error('Error fetching sale items:', error);
    showAlert('Failed to load sale items', 'error');
  } finally {
    isLoading.value = false;
  }
};

const handlePageChange = async (newPage) => {
  try {
    isLoading.value = true;
    galleryState.page = newPage;
    await loadSaleItems();
  } catch (error) {
    console.error('Error loading page:', error);
  } finally {
    isLoading.value = false;
  }
};

const handleGoToLastPage = async () => {
  try {
    isLoading.value = true;
    await loadSaleItems();
    galleryState.page = galleryState.totalPages;
    await loadSaleItems();
  } catch (error) {
    console.error('Error loading last page:', error);
  } finally {
    isLoading.value = false;
  }
};

const handleFilter = async () => {
  galleryState.page = 1;
  await loadSaleItems();
};

const handleSortChange = () => {
  if (galleryState.sortDirection === 'none') {
    galleryState.sortField = 'id';
  } else {
    galleryState.sortField = 'brand.name';
  }

  galleryState.resetPageOnly();
  loadSaleItems();
};

const handlePageSizeChange = () => {
  galleryState.resetPageOnly();
  loadSaleItems();
};

const initializeComponent = () => {
  const hasVisited = sessionStorage.getItem('hasVisitedGallery');

  if (!hasVisited) {
    galleryState.resetAll();
    sessionStorage.setItem('hasVisitedGallery', 'true');
  }

  loadSaleItems();

  if (saleStore.created) {
    showAlert('The sale item has been successfully added.');
    saleStore.created = false;
  }

  if (saleStore.deleted) {
    showAlert('The sale item has been deleted.');
    saleStore.deleted = false;
  }
};

onMounted(initializeComponent);
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
          class="w-full transition-all duration-300 flex flex-col lg:flex-row items-center justify-between gap-4 lg:gap-8"
        >
          <BrandFilter @filter-sale-items-by-brands="handleFilter" />
          <div class="w-full flex items-center lg:justify-end lg:w-full">
            <SortComponent @sortType="handleSortChange" @pageSize="handlePageSizeChange" />
          </div>
        </div>

        <div class="w-full lg:w-2/2">
          <div v-if="showLoading" class="flex justify-center items-center min-h-screen">
            <p class="text-center text-lg">Loading...</p>
          </div>

          <div
            v-else-if="hasItems"
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
            v-else-if="showNoItems"
            class="font-medium text-primary flex justify-center items-center min-h-[300px]"
          >
            <p class="text-center text-lg">! No sale items</p>
          </div>
        </div>
      </div>

      <Pagination
        :currentPage="galleryState.page"
        :totalPages="galleryState.totalPages"
        @update:currentPage="handlePageChange"
        @lastPage="handleGoToLastPage"
      />
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