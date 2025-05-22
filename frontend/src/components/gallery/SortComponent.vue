<script setup>
import { ref } from 'vue';
import axios from 'axios';

const page = ref(0);
const pageSize = ref(5);
const sortOrder = ref('none');

const items = ref([]);
const pageSizeOptions = [5, 10, 20];

const fetchData = async () => {
  try {
    let sortParam = undefined;
    if (sortOrder.value === 'asc') {
      sortParam = 'ASC';
    } else if (sortOrder.value === 'desc') {
      sortParam = 'DESC';
    }

    const response = await axios.get(`${import.meta.env.VITE_BASE_URL}/v2/sale-items`, {
      params: {
        page: page.value,
        size: pageSize.value,
        sortDirection: sortParam,
      },
    });

    items.value = response.data.content;
  } catch (error) {
    console.error('Error fetching items:', error);
  }
};

const toggleSort = (order) => {
  sortOrder.value = order;
  fetchData();
};

const handlePageSizeChange = (event) => {
  pageSize.value = parseInt(event.target.value);
  fetchData();
};

const isSortActive = (order) => {
  return sortOrder.value === order;
};

fetchData();
</script>


<template>
  <div
    class="flex items-center flex-wrap gap-4 bg-white p-3 rounded-lg shadow-sm border border-gray-200"
  >
    <div class="flex items-center gap-2">
      <label for="page-size" class="text-sm font-medium text-gray-600">Show</label>
      <div class="itbms-page-size relative">
        <select
          id="page-size"
          v-model="pageSize"
          @change="handlePageSizeChange"
          class="appearance-none bg-gray-50 border border-gray-300 text-gray-700 py-2 pl-3 pr-8 rounded-md text-sm cursor-pointer"
        >
          <option v-for="size in pageSizeOptions" :key="size" :value="size">
            {{ size }}
          </option>
        </select>
        <div
          class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-500"
        >
          <svg
            class="w-4 h-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M19 9l-7 7-7-7"
            ></path>
          </svg>
        </div>
      </div>
    </div>

    <div class="h-6 w-px bg-gray-300 hidden sm:block"></div>

    <div class="flex items-center gap-2">
      <button
        @click="toggleSort('none')"
        class="itbms-brand-none p-2 rounded-md border text-sm transition-colors cursor-pointer"
        :class="
          isSortActive('none')
            ? 'bg-blue-50 border-blue-300 text-blue-600'
            : 'bg-gray-50 border-gray-300 text-gray-600 hover:bg-gray-100'
        "
        title="Default order"
        aria-label="Default order"
      >
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
          <path
            fill="none"
            stroke="currentColor"
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-miterlimit="10"
            stroke-width="1.5"
            d="M3 10h18M3 6h18M3 14h18M3 18h18"
          />
        </svg>
      </button>

      <button
        @click="toggleSort('asc')"
        class="itbms-brand-asc p-2 rounded-md border text-sm transition-colors cursor-pointer"
        :class="
          isSortActive('asc')
            ? 'bg-blue-50 border-blue-300 text-blue-600'
            : 'bg-gray-50 border-gray-300 text-gray-600 hover:bg-gray-100'
        "
        title="Sort ascending"
        aria-label="Sort ascending"
      >
        <svg
          class="w-5 h-5"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M3 4h13M3 8h9m-9 4h6m4 0l4-4m0 0l4 4m-4-4v12"
          ></path>
        </svg>
      </button>

      <button
        @click="toggleSort('desc')"
        class="itbms-brand-desc p-2 rounded-md border text-sm transition-colors cursor-pointer"
        :class="
          isSortActive('desc')
            ? 'bg-blue-50 border-blue-300 text-blue-600'
            : 'bg-gray-50 border-gray-300 text-gray-600 hover:bg-gray-100'
        "
        title="Sort descending"
        aria-label="Sort descending"
      >
        <svg
          class="w-5 h-5"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M3 4h13M3 8h9m-9 4h9m5-4v12m0 0l-4-4m4 4l4-4"
          ></path>
        </svg>
      </button>
    </div>
  </div>
</template>