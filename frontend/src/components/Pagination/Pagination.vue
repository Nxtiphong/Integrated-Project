<template>
  <div class="pagination-container">
    <!-- แสดงข้อมูลสรุป -->
    <div class="pagination-info">
      <span class="text-sm text-gray-600">
        แสดง {{ startItem }}-{{ endItem }} จาก {{ totalElements }} รายการ
      </span>
    </div>

    <!-- ปุ่ม Pagination -->
    <nav class="pagination-nav" v-if="totalPages > 1">
      <ul class="pagination-list">
        <!-- ปุ่ม First -->
        <li class="pagination-item">
          <button 
            @click="goToPage(0)" 
            :disabled="isFirst || loading"
            class="pagination-btn"
            :class="{ 'disabled': isFirst || loading }"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 19l-7-7 7-7m8 14l-7-7 7-7"/>
            </svg>
          </button>
        </li>

        <!-- ปุ่ม Previous -->
        <li class="pagination-item">
          <button 
            @click="goToPage(currentPage - 1)" 
            :disabled="isFirst || loading"
            class="pagination-btn"
            :class="{ 'disabled': isFirst || loading }"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/>
            </svg>
          </button>
        </li>

        <!-- หมายเลขหน้า -->
        <li 
          v-for="pageNum in visiblePages" 
          :key="pageNum" 
          class="pagination-item"
        >
          <button 
            @click="goToPage(pageNum - 1)"
            :disabled="loading"
            class="pagination-btn pagination-number"
            :class="{ 
              'active': pageNum - 1 === currentPage,
              'disabled': loading
            }"
          >
            {{ pageNum }}
          </button>
        </li>

        <!-- ปุ่ม Next -->
        <li class="pagination-item">
          <button 
            @click="goToPage(currentPage + 1)" 
            :disabled="isLast || loading"
            class="pagination-btn"
            :class="{ 'disabled': isLast || loading }"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"/>
            </svg>
          </button>
        </li>

        <!-- ปุ่ม Last -->
        <li class="pagination-item">
          <button 
            @click="goToPage(totalPages - 1)" 
            :disabled="isLast || loading"
            class="pagination-btn"
            :class="{ 'disabled': isLast || loading }"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 5l7 7-7 7M5 5l7 7-7 7"/>
            </svg>
          </button>
        </li>
      </ul>
    </nav>

    <!-- Page Size Selector -->
    <div class="page-size-selector" v-if="showPageSizeSelector">
      <label class="text-sm text-gray-600">
        แสดง:
        <select 
          v-model="selectedPageSize" 
          @change="changePageSize"
          :disabled="loading"
          class="page-size-select"
        >
          <option v-for="size in pageSizeOptions" :key="size" :value="size">
            {{ size }}
          </option>
        </select>
        รายการต่อหน้า
      </label>
    </div>
  </div>
</template>

<script>
export default {
  name: 'VuePaging',
  props: {
    // ข้อมูลจาก PageDTO
    currentPage: { type: Number, default: 0 },
    pageSize: { type: Number, default: 10 },
    totalElements: { type: Number, default: 0 },
    totalPages: { type: Number, default: 0 },
    isFirst: { type: Boolean, default: true },
    isLast: { type: Boolean, default: true },
    
    // การตั้งค่า UI
    maxVisiblePages: { type: Number, default: 5 },
    showPageSizeSelector: { type: Boolean, default: true },
    pageSizeOptions: { type: Array, default: () => [5, 10, 20, 50] },
    loading: { type: Boolean, default: false }
  },
  
  data() {
    return {
      selectedPageSize: this.pageSize
    }
  },
  
  computed: {
    // คำนวณรายการแรกและสุดท้ายที่แสดง
    startItem() {
      return this.totalElements === 0 ? 0 : (this.currentPage * this.pageSize) + 1;
    },
    
    endItem() {
      const end = (this.currentPage + 1) * this.pageSize;
      return Math.min(end, this.totalElements);
    },
    
    // สร้างรายการหน้าที่จะแสดง
    visiblePages() {
      const pages = [];
      const half = Math.floor(this.maxVisiblePages / 2);
      let start = Math.max(1, this.currentPage + 1 - half);
      let end = Math.min(this.totalPages, start + this.maxVisiblePages - 1);
      
      // ปรับให้แสดงครบตาม maxVisiblePages ถ้าเป็นไปได้
      if (end - start + 1 < this.maxVisiblePages) {
        start = Math.max(1, end - this.maxVisiblePages + 1);
      }
      
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      
      return pages;
    }
  },
  
  watch: {
    pageSize(newSize) {
      this.selectedPageSize = newSize;
    }
  },
  
  methods: {
    goToPage(page) {
      if (page < 0 || page >= this.totalPages || page === this.currentPage || this.loading) {
        return;
      }
      
      this.$emit('page-change', {
        page: page,
        size: this.selectedPageSize
      });
    },
    
    changePageSize() {
      this.$emit('page-size-change', {
        page: 0, // รีเซ็ตกลับไปหน้าแรก
        size: this.selectedPageSize
      });
    }
  }
}
</script>

<style scoped>
.pagination-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 1rem 0;
}

.pagination-info {
  color: #6b7280;
  font-size: 0.875rem;
}

.pagination-nav {
  flex: 1;
  display: flex;
  justify-content: center;
}

.pagination-list {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  list-style: none;
  margin: 0;
  padding: 0;
}

.pagination-item {
  display: flex;
}

.pagination-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  min-width: 2.5rem;
  height: 2.5rem;
  border: 1px solid #d1d5db;
  background-color: white;
  color: #374151;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 0.375rem;
  font-size: 0.875rem;
  font-weight: 500;
}

.pagination-btn:hover:not(.disabled) {
  background-color: #f3f4f6;
  border-color: #9ca3af;
}

.pagination-btn.active {
  background-color: #3b82f6;
  border-color: #3b82f6;
  color: white;
}

.pagination-btn.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background-color: #f9fafb;
}

.pagination-number {
  min-width: 2.5rem;
}

.page-size-selector {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #6b7280;
}

.page-size-select {
  padding: 0.25rem 0.5rem;
  border: 1px solid #d1d5db;
  border-radius: 0.375rem;
  background-color: white;
  cursor: pointer;
}

.page-size-select:focus {
  outline: none;
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* Responsive */
@media (max-width: 768px) {
  .pagination-container {
    flex-direction: column;
    align-items: stretch;
    text-align: center;
  }
  
  .pagination-nav {
    order: 2;
  }
  
  .pagination-info {
    order: 1;
    text-align: center;
  }
  
  .page-size-selector {
    order: 3;
    justify-content: center;
  }
  
  .pagination-btn {
    min-width: 2rem;
    height: 2rem;
    padding: 0.25rem;
  }
}
</style>