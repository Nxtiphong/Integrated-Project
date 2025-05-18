import { defineStore } from "pinia";
import { ref } from "vue";

export const useBrandFormStore = defineStore('brandForm', () => {

   const brandForm = ref({
      name: '',
      websiteUrl: '',
      isActive: true,
      countryOfOrigin: ''
   })

   const resetForm = () => {
      brandForm.value = {
         name: '',
         websiteUrl: '',
         isActive: true,
         countryOfOrigin: ''
      };
   };

   return {
      ...brandForm.value,
      brandForm,
      resetForm,
   };
})