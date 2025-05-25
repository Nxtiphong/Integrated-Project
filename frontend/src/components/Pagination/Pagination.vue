<template>
  <div class="flex justify-center mt-6">
    <nav class="inline-flex items-center gap-2 bg-white shadow-lg border border-gray-200 px-4 py-2 rounded-xl">
      <button 
        @click="changePage(1)" 
        :disabled="currentPage === 1"
        class="flex items-center justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
        title="First page"
      >
        First
      </button>

      <button 
        @click="goToPreviousPage" 
        :disabled="currentPage === 1"
        class="flex items-center justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
        title="Previous page"
      >
        Prev
      </button>

      <div class="flex items-center gap-1">
        <button 
          v-for="(page, index) in visiblePages" 
          :key="`page-${index}`"
          @click="changePage(page)"
          :class="{
            'bg-blue-600 border-blue-600 text-white shadow-md': currentPage === page,
            'text-gray-500 bg-white border-gray-300 hover:bg-gray-100 hover:text-gray-700': currentPage !== page
          }"
          class="flex items-center justify-center w-10 h-10 text-sm font-medium border rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
        >
          {{ page }}
        </button>
      </div>

      <button 
        @click="goToNextPage" 
        :disabled="currentPage === totalPages"
        class="flex items-center justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
        title="Next page"
      >
        Next
      </button>

      <button 
        @click="changePage(totalPages)" 
        :disabled="currentPage === totalPages"
        class="flex items-center justify-center w-12 h-10 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-lg hover:bg-gray-100 hover:text-gray-700 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:text-gray-500 transition-all duration-200"
        title="Last page"
      >
        Last
      </button>
    </nav>

    <div class="ml-4 flex items-center text-sm text-gray-600">
      <span class="bg-gray-100 px-3 py-2 rounded-lg">
        หน้า {{ currentPage }} จาก {{ totalPages }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  currentPage: {
    type: Number,
    required: true
  },
  totalPages: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:currentPage'])

const startPage = ref(1)

function changePage(page) {
  if (page >= 1 && page <= props.totalPages) {
    emit('update:currentPage', page)
  }
}

const visiblePages = computed(() => {
  const maxVisiblePages = Math.min(10, props.totalPages)
  const pages = []
  
  for (let i = 0; i < maxVisiblePages; i++) {
    const pageNumber = startPage.value + i
    if (pageNumber <= props.totalPages) {
      pages.push(pageNumber)
    }
  }
  
  return pages
})

const currentPageIndex = computed(() => {
  return props.currentPage - startPage.value
})

function goToNextPage() {
  if (props.currentPage < props.totalPages) {
    const nextPage = props.currentPage + 1
    
    if (currentPageIndex.value === 9 && nextPage <= props.totalPages) {
      startPage.value += 1
    }
    
    changePage(nextPage)
  }
}

function goToPreviousPage() {
  if (props.currentPage > 1) {
    const prevPage = props.currentPage - 1
    
    if (currentPageIndex.value === 0 && prevPage >= 1) {
      startPage.value -= 1
    }
    
    changePage(prevPage)
  }
}

watch(() => props.currentPage, (newPage) => {
  if (newPage < startPage.value || newPage >= startPage.value + 10) {
    const newStartPage = Math.max(1, newPage - 5)
    const maxStartPage = Math.max(1, props.totalPages - 9)
    startPage.value = Math.min(newStartPage, maxStartPage)
  }
})

watch(() => props.totalPages, () => {
  const maxStartPage = Math.max(1, props.totalPages - 9)
  if (startPage.value > maxStartPage) {
    startPage.value = maxStartPage
  }
})
</script>