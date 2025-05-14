import { defineStore } from "pinia";

export const useBrandFormStore = defineStore('brandForm', {
   state: () => ({
      name: '',
      websiteURL: '',
      isActive: false,
      countryOfOrigin: '',
   }),

   actions: {
      resetForm() {
         this.name = '';
         this.websiteURL = '';
         this.isActive = false;
         this.countryOfOrigin = '';
      },

      async fetchBrandDetail(id) {
         try {
            const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands/${id}`);
            if (!res.ok) throw new Error('Failed to fetch sale item')
            const brandDetail = await res.json()
            this.name = brandDetail.name
            this.websiteURL = brandDetail.websiteURL
            this.isActive = brandDetail.isActive
            this.countryOfOrigin = brandDetail.countryOfOrigin
            return { success: true, data: res.json };
         } catch (error) {
            console.error('API error:', error);
            return { success: false, error };
         }
      },

      async submitCreateForm() {
         const formData = {
            name: this.name,
            websiteURL: this.websiteURL,
            isActive: this.isActive,
            countryOfOrigin: this.countryOfOrigin
         }

         console.log("Form Submitted")

         try {
            const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`, {
               method: 'POST',
               headers: {
                  'Content-Type': 'application/json',
               },
               body: JSON.stringify(formData),
            });
            this.resetForm();
            return { success: true, data: res.json };
         } catch (error) {
            console.error('API error:', error);
            return { success: false, error };
         }
      },

      async submitUpdateForm(id) {
         const formData = {
            name: this.name,
            websiteURL: this.websiteURL,
            isActive: this.isActive,
            countryOfOrigin: this.countryOfOrigin
         }

         console.log("Update Form Submitted")

         try {
            const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands/${id}`, {
               method: 'PUT',
               headers: {
                  'Content-Type': 'application/json',
               },
               body: JSON.stringify(formData),
            });
            this.resetForm();
            return { success: true, data: res.json };
         } catch (error) {
            console.error('API error:', error);
            return { success: false, error };
         }
      },
   },
})