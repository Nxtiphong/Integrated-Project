import { defineStore } from "pinia";
import { ref, watch } from "vue";

export const useGalleryStateStore = defineStore('saleGalleryState', () => {

   const saved = JSON.parse(sessionStorage.getItem('saleGalleryState') || '{}');

   const filterBrandLists = ref(saved.filterBrandLists || []);
   const minPrice = ref(saved.minPrice || null)
   const maxPrice = ref(saved.maxPrice || null)
   const filterStorageSize = ref(saved.filterStorageSize || []);
   const page = ref(saved.page || 1);
   const totalPages = ref(saved.totalPages || 1);
   const sortField = ref(saved.sortField || 'id');
   const sortDirection = ref(saved.sortDirection || 'none');
   const pageSize = ref(saved.pageSize || 10);
   const keywords = ref(saved.keywords || null)

   watch(
      [filterBrandLists, filterStorageSize, minPrice, maxPrice, page, totalPages, keywords, sortField, sortDirection, pageSize],
      () => {
         sessionStorage.setItem('saleGalleryState', JSON.stringify({
            filterBrandLists: filterBrandLists.value,
            minPrice: minPrice.value,
            maxPrice: maxPrice.value,
            filterStorageSize: filterStorageSize.value,
            page: page.value,
            totalPages: totalPages.value,
            sortField: sortField.value,
            sortDirection: sortDirection.value,
            keywords: keywords.value,
            pageSize: pageSize.value,
         }));
      },
      { deep: true }
   );

   function resetAll() {
      filterBrandLists.value = [];
      filterStorageSize.value = [];
      minPrice.value = null;
      maxPrice.value = null;
      page.value = 1;
      totalPages.value = 1;
      keywords.value = ''
      sortField.value = 'id';
      sortDirection.value = 'none';
      pageSize.value = 10;
      sessionStorage.removeItem('saleGalleryState');
   }

   function resetPageOnly() {
      page.value = 1;
   }

   return {
      filterBrandLists,
      filterStorageSize,
      minPrice,
      maxPrice,
      page,
      keywords,
      totalPages,
      sortField,
      sortDirection,
      pageSize,
      resetAll,
      resetPageOnly,
   };
});