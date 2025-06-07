import { httpRequest } from "./fetchUtils";

export const deleteSaleItem = async (id) => {
   try {
      const res = await httpRequest('DELETE', `v1/sale-items/${id}`)
      return { success: true };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
}