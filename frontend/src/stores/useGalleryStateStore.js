import { defineStore } from "pinia";
import { ref, watch } from "vue";

export const useGalleryStateStore = defineStore('saleGalleryState', () => {

   const saved = JSON.parse(sessionStorage.getItem('saleGalleryState') || '{}');

   const filterBrandLists = ref(saved.filterBrandLists || []);
   const filterPrice = ref(saved.filterPrice || []);
   const filterStorageSize = ref(saved.filterStorageSize || []);
   const page = ref(saved.page || 1);
   const totalPages = ref(saved.totalPages || 1);
   const sortField = ref(saved.sortField || 'id');
   const sortDirection = ref(saved.sortDirection || 'none');
   const pageSize = ref(saved.pageSize || 10);

   watch(
      [filterBrandLists, filterPrice, filterStorageSize, page, totalPages, sortField, sortDirection, pageSize],
      () => {
         sessionStorage.setItem('saleGalleryState', JSON.stringify({
            filterBrandLists: filterBrandLists.value,
            filterPrice: filterPrice.value,
            filterStorageSize: filterStorageSize.value,
            page: page.value,
            totalPages: totalPages.value,
            sortField: sortField.value,
            sortDirection: sortDirection.value,
            pageSize: pageSize.value,
         }));
      },
      { deep: true }
   );

   function resetAll() {
      filterBrandLists.value = [];
      filterPrice.value = [];
      filterStorageSize.value = [];
      page.value = 1;
      totalPages.value = 1;
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
      filterPrice,
      filterStorageSize,
      page,
      totalPages,
      sortField,
      sortDirection,
      pageSize,
      resetAll,
      resetPageOnly,
   };
});