<template>
  <div class="flex justify-center mt-4">
    <nav class="inline-flex gap-1 bg-gray-200 px-3 py-1 rounded">
      <button @click="changePage(1)" :disabled="currentPage === 1">First</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">Prev</button>

      <button v-for="page in visiblePages" :key="page"
              :class="{'font-bold underline': currentPage === page}"
              @click="changePage(page)">
        {{ page }}
      </button>

      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">Next</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">Last</button>
    </nav>
  </div>
</template>

<script setup>
import { computed, watch } from 'vue'

const props = defineProps({
  currentPage: Number,
  totalPages: Number,
})

const emit = defineEmits(['update:currentPage'])

function changePage(page) {
  if (page >= 1 && page <= props.totalPages) {
    emit('update:currentPage', page)
  }
}

const visiblePages = computed(() => {
  const range = []
  const start = Math.max(1, props.currentPage - 2)
  const end = Math.min(props.totalPages, props.currentPage + 2)

  for (let i = start; i <= end; i++) {
    range.push(i)
  }

  return range
})
</script>
