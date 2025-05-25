import { defineStore } from "pinia";
import { ref, watch } from "vue";

export const useGalleryFilterStore = defineStore('saleGalleryFilter', () => {

   const saved = JSON.parse(sessionStorage.getItem('saleGalleryState') || '{}');

   const filterLists = ref(saved.filterLists || []);
   const page = ref(saved.page || 1);
   const totalPages = ref(saved.totalPages || 1);
   const sortField = ref(saved.sortField || 'id');
   const sortDirection = ref(saved.sortDirection || 'none');
   const pageSize = ref(saved.pageSize || 10);

   watch(
      [filterLists, page, totalPages, sortField, sortDirection, pageSize],
      () => {
         sessionStorage.setItem('saleGalleryState', JSON.stringify({
            filterLists: filterLists.value,
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
      filterLists.value = [];
      page.value = 1;
      totalPages.value = 1;
      sortField.value = 'id';
      sortDirection.value = 'none';
      pageSize.value = 10;
      sessionStorage.removeItem('saleGalleryFilter');
   }

   function resetPageOnly() {
      page.value = 1;
   }

   return {
      filterLists,
      page,
      totalPages,
      sortField,
      sortDirection,
      pageSize,
      resetAll,
      resetPageOnly,
   };
});