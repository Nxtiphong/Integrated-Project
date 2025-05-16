import { defineStore } from "pinia";
import { ref } from "vue";

export const useBrandStore = defineStore('brandLists', () => {
   const brandLists = ref([])
   const isCreateSuccess = ref(false)
   const isCreateFailed = ref(false)
   const isUpdatedSuccess = ref(false)
   const isUpdatedFailed = ref(false)

   const getBrandLists = () => brandLists.value

   const addBrandLists = (newBrand) => {
      brandLists.value.push([...brandLists, newBrand])
   }

   return { brandLists, isCreateSuccess, isCreateFailed, isUpdatedSuccess, isUpdatedFailed, getBrandLists, addBrandLists }
})