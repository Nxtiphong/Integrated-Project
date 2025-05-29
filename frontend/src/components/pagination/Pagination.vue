<script setup>
import { computed, ref, watch } from 'vue';

const props = defineProps({
  currentPage: {
    type: Number,
    required: true,
  },
  totalPages: {
    type: Number,
    required: true,
  },
  hasData: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(['update:currentPage', 'lastPage']);

const startPage = ref(1);

const shouldShowPagination = computed(() => {
  return props.hasData && props.totalPages > 1;
});

function changePage(page) {
  if (page >= 1 && page <= props.totalPages) {
    emit('update:currentPage', page);
  }
}

const goToLastPage = () => {
  emit('lastPage');
};

const visiblePages = computed(() => {
  const maxVisiblePages = Math.min(10, props.totalPages);
  const pages = [];

  for (let i = 0; i < maxVisiblePages; i++) {
    const pageNumber = startPage.value + i;
    if (pageNumber <= props.totalPages) {
      pages.push(pageNumber);
    }
  }

  return pages;
});

const currentPageIndex = computed(() => {
  return props.currentPage - startPage.value;
});

function goToNextPage() {
  if (props.currentPage < props.totalPages) {
    const nextPage = props.currentPage + 1;

    if (currentPageIndex.value === 9 && nextPage <= props.totalPages) {
      startPage.value += 1;
    }

    changePage(nextPage);
  }
}

function goToPreviousPage() {
  if (props.currentPage > 1) {
    const prevPage = props.currentPage - 1;

    if (currentPageIndex.value === 0 && prevPage >= 1) {
      startPage.value -= 1;
    }

    changePage(prevPage);
  }
}

watch(
  () => props.currentPage,
  (newPage) => {
    if (newPage < startPage.value || newPage >= startPage.value + 10) {
      const newStartPage = Math.max(1, newPage - 5);
      const maxStartPage = Math.max(1, props.totalPages - 9);
      startPage.value = Math.min(newStartPage, maxStartPage);
    }
  },
  { immediate: true },
);

watch(
  () => props.totalPages,
  (newTotalPages) => {
    const maxStartPage = Math.max(1, newTotalPages - 9);

    if (startPage.value > maxStartPage) {
      startPage.value = maxStartPage;
    }

    if (props.currentPage < startPage.value || props.currentPage >= startPage.value + 10) {
      const newStartPage = Math.max(1, props.currentPage - 5);
      startPage.value = Math.min(newStartPage, maxStartPage);
    }
  },
  { immediate: true },
);
</script>

<template>
  <div v-show="shouldShowPagination" class="flex justify-center flex-col items-center gap-1.5 mt-3">
    <nav
      class="inline-flex items-center gap-1 md:gap-2 bg-white shadow-md border border-gray-200 px-2.5 md:px-4 md:py-2 py-1 rounded-md"
    >
      <button
        @click="changePage(1)"
        :disabled="currentPage === 1"
        class="itbms-page-first flex items-center cursor-pointer justify-center w-8 h-7 md:w-12 md:h-10 text-xs md:text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        First
      </button>

      <button
        @click="goToPreviousPage"
        :disabled="currentPage === 1"
        class="itbms-page-prev flex items-center cursor-pointer justify-center w-8 h-7 md:w-12 md:h-10 text-xs md:text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Prev
      </button>

      <div class="flex items-center gap-0.5 md:gap-2">
        <button
          v-for="(page, index) in visiblePages"
          :key="`page-${index}`"
          @click="changePage(page)"
          :class="[
            `itbms-page-${index}`,
            {
              'bg-blue-600 border-blue-600 text-white shadow-md': currentPage === page,
              'text-gray-500 bg-white border-gray-300 hover:bg-gray-100 hover:text-gray-700':
                currentPage !== page,
            },
          ]"
          class="flex items-center justify-center cursor-pointer w-7 h-7 md:w-10 md:h-10 text-xs md:text-sm font-medium border rounded transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
        >
          {{ page }}
        </button>
      </div>

      <button
        @click="goToNextPage"
        :disabled="currentPage === totalPages"
        class="itbms-page-next flex items-center cursor-pointer justify-center w-8 h-7 md:w-12 md:h-10 text-xs md:text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Next
      </button>

      <button
        @click="goToLastPage"
        :disabled="currentPage === totalPages"
        class="itbms-page-last flex items-center cursor-pointer justify-center w-8 h-7 md:w-12 md:h-10 text-xs md:text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Last
      </button>
    </nav>

    <div class="flex items-center text-xs text-gray-600">
      <span class="bg-slate-100 px-2 md:px-3 md:py-2 py-1 rounded text-xs">
        page {{ currentPage }} of {{ totalPages }}
      </span>
    </div>
  </div>
</template>
