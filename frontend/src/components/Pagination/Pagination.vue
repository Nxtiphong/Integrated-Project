<template>
  <div v-show="hasData && totalPages > 1" class="flex justify-center mt-6">
    <nav class="inline-flex items-center gap-2 bg-white shadow-lg border border-gray-200 px-4 py-2 rounded-xl">
      <button 
        @click="changePage(1)" 
        :disabled="currentPage === 1"
        class="itbms-page-first flex items-center cursor-pointer justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        First
      </button>

      <button
        @click="goToPreviousPage"
        :disabled="currentPage === 1"
        class="itbms-page-prev flex items-center cursor-pointer justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Prev
      </button>

      <div class="flex items-center gap-1">
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
          class="flex items-center justify-center cursor-pointer w-10 h-10 text-sm font-medium border rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
        >
          {{ page }}
        </button>
      </div>

      <button
        @click="goToNextPage"
        :disabled="currentPage === totalPages"
        class="itbms-page-next flex items-center cursor-pointer justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Next
      </button>

      <button
        @click="changePage(totalPages)"
        :disabled="currentPage === totalPages"
        class="itbms-page-last flex items-center cursor-pointer justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
      >
        Last
      </button>
    </nav>

    <div class="ml-4 flex items-center text-xs text-gray-600">
      <span class="bg-slate-100 px-3 py-2 rounded-lg">
        page {{ currentPage }} of {{ totalPages }}
      </span>
    </div>
  </div>
</template>

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

const emit = defineEmits(['update:currentPage']);

const startPage = ref(1);

const shouldShowPagination = computed(() => {
  return Boolean(props.hasData && props.totalPages && props.totalPages > 1)
})

function changePage(page) {
  if (page >= 1 && page <= props.totalPages && page !== props.currentPage) {
    emit('update:currentPage', page)
  }
}

const visiblePages = computed(() => {
  if (props.totalPages <= 0) return []
  
  const maxVisiblePages = Math.min(10, props.totalPages)
  const pages = []
  
  const safeStartPage = Math.max(1, Math.min(startPage.value, props.totalPages - maxVisiblePages + 1))
  
  for (let i = 0; i < maxVisiblePages; i++) {
    const pageNumber = safeStartPage + i
    if (pageNumber >= 1 && pageNumber <= props.totalPages) {
      pages.push(pageNumber)
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
);

watch(() => props.totalPages, (newTotalPages) => {
  if (newTotalPages > 0) {
    const maxStartPage = Math.max(1, newTotalPages - 9)
    if (startPage.value > maxStartPage) {
      startPage.value = maxStartPage
    }
  } else {
    startPage.value = 1
  }
})
</script>
