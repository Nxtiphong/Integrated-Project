<template>
  <div class="p-6">
    <h1 class="text-xl font-bold mb-4">Pagination Example</h1>

    <!-- แสดงรายการข้อมูล -->
    <ul class="mb-6 space-y-1">
      <li v-for="item in items" :key="item.id" class="border-b pb-1">
        {{ item.name }}
      </li>
    </ul>

    <!-- Pagination Controls -->
    <div class="flex justify-center space-x-2">
      <button
        @click="changePage(currentPage - 1)"
        :disabled="isFirst"
        class="px-3 py-1 rounded-md text-sm bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
      >
        Previous
      </button>

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

      <button
        @click="changePage(currentPage + 1)"
        :disabled="isLast"
        class="px-3 py-1 rounded-md text-sm bg-gray-200 hover:bg-gray-300 disabled:opacity-50"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// ตัวแปรเก็บข้อมูลจาก backend
const items = ref([])

const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)
const isFirst = ref(true)
const isLast = ref(false)

// โหลดข้อมูลจาก backend
async function fetchItems(page = 1) {
  try {
    const response = await fetch(`/sale-items?page=${page}&size=${pageSize.value}`)
    const data = await response.json()

    items.value = data.content
    currentPage.value = data.pageNumber
    pageSize.value = data.pageSize
    totalPages.value = data.totalPages
    isFirst.value = data.isFirst
    isLast.value = data.isLast
  } catch (err) {
    console.error('Error loading items:', err)
  }
}

// เปลี่ยนหน้า
function changePage(page) {
  if (page >= 1 && page <= totalPages.value) {
    fetchItems(page)
  }
}

// แสดงหมายเลขหน้า
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

// เรียกตอนโหลด component
onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
ul {
  list-style-type: disc;
  padding-left: 1.5rem;
}
</style>
