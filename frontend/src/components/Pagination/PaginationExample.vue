<template>
  <div class="p-6">
    <h1 class="text-xl font-bold mb-4">Pagination Example</h1>

    <!-- แสดงรายการข้อมูล -->
    <ul class="mb-6 space-y-1">
      <li v-for="item in paginatedItems" :key="item" class="border-b pb-1">
        {{ item }}
      </li>
    </ul>

    <!-- Pagination Controls -->
    <div class="flex justify-center space-x-2">
      <!-- Previous Button -->
      <button
        @click="previousPage"
        :disabled="currentPage === 1"
        class="px-3 py-1 rounded-md text-sm bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
      >
        Previous
      </button>

      <!-- Page Number Buttons -->
      <button
        v-for="page in displayedPageNumbers"
        :key="'page-' + page"
        @click="changePage(page)"
        :class="[
          'px-3 py-1 rounded-md text-sm',
          currentPage === page ? 'bg-blue-500 text-white' : 'bg-white hover:bg-blue-100'
        ]"
      >
        {{ page }}
      </button>

      <!-- Next Button -->
      <button
        @click="nextPage"
        :disabled="currentPage === totalPages"
        class="px-3 py-1 rounded-md text-sm bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// จำนวนรายการต่อหน้า
const itemsPerPage = 10

// รายการทั้งหมด (สมมุติข้อมูลไว้ก่อน)
const items = ref(Array.from({ length: 100 }, (_, i) => `Item ${i + 1}`))

// หน้าปัจจุบัน
const currentPage = ref(1)

// คำนวณจำนวนหน้าทั้งหมด
const totalPages = computed(() => Math.ceil(items.value.length / itemsPerPage))

// คำนวณรายการที่แสดงในหน้าปัจจุบัน
const paginatedItems = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return items.value.slice(start, end)
})

// เปลี่ยนหน้า
function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

// หน้า Previous / Next
function previousPage() {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

function nextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

// สร้าง array ของหมายเลขหน้าที่จะแสดง
const displayedPageNumbers = computed(() => {
  const pages = []
  const total = totalPages.value
  const current = currentPage.value
  const maxShown = 5

  let start = Math.max(current - Math.floor(maxShown / 2), 1)
  let end = start + maxShown - 1

  if (end > total) {
    end = total
    start = Math.max(end - maxShown + 1, 1)
  }

  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})
</script>

<style scoped>
ul {
  list-style-type: disc;
  padding-left: 1.5rem;
}
</style>
