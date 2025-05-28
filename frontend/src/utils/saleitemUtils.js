export const deleteSaleItem = async (id) => {
    try {
       const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/sale-items/${id}`, {
          method: 'DELETE',
       });
 
       if (!res.ok) throw new Error('Failed to create new SaleItem')
 
       return { success: true };
    } catch (error) {
       console.error('API error:', error);
       return { success: false, error };
    }
 }