import { httpRequest } from "./fetchUtils";

export const fetchBrandDetail = async (id) => {
   try {
      const res = await httpRequest('GET', `v1/brands/${id}`);
      return { success: true, data: res.data };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
}

export const submitCreateForm = async (formData) => {
   try {
      const res = await httpRequest('POST', `v1/brands`, formData)
      if (res.status !== 201) {
         throw new Error(`HTTP error ${res.status}`)
      }
      return { success: true, data: res.data };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
};

export const submitUpdateForm = async (id, formData) => {
   try {
      const res = await httpRequest('PUT', `v1/brands/${id}`, formData)
      if (res.status !== 200) {
         throw new Error(`HTTP error ${res.status}`)
      }
      return { success: true, data: res.data };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
};

export const deleteBrand = async (id) => {
   try {
      const res = await httpRequest('DELETE', `v1/brands/${id}`)
      if (res.status !== 204) {
         throw new Error(`HTTP error ${res.status}`)
      }
      return { success: true };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
}

