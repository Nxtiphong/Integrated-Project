import { defineStore } from "pinia";
import { ref } from "vue";

export const useBrandFormStore = defineStore('brandForm', () => {

   const brandForm = ref({
      name: '',
      websiteUrl: '',
      isActive: false,
      countryOfOrigin: ''
   })

   const resetForm = () => {
      brandForm.value = {
         name: '',
         websiteUrl: '',
         isActive: false,
         countryOfOrigin: ''
      };
   };

   return {
      ...brandForm.value,
      brandForm,
      resetForm,
   };
})