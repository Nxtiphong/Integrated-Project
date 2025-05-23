import { defineStore } from "pinia";
import { ref } from "vue";

export const useGalleryFilterStore = defineStore('saleGalleryFilter', () => {
   const filterLists = ref([])

   return { filterLists }
})