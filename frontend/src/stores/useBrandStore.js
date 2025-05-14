import { defineStore } from "pinia";

export const useBrandStore = defineStore('brandLists', {
   state: () => ({
      name: '',
      websiteURL: '',
      isActive: false,
      countryOfOrigin: '',
   }),

   actions: {
      async fetchAllBrands() {
         try {
            
         } catch (error) {
            
         }
      }
   },
})