export const fetchBrandDetail = async (id) => {
   try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands/${id}`);
      if (!res.ok) throw new Error('Failed to fetch brand detail')
      return { success: true, data: await res.json() };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
}

export const submitCreateForm = async (formData) => {
   console.log(formData)
   try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`, {
         method: 'POST',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify(formData),
      });

      if (!res.ok) throw new Error('Failed to create new brand')

      return { success: true, data: await res.json() };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
};

export const submitUpdateForm = async (id, formData) => {
   console.log(formData)
   try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands/${id}`, {
         method: 'PUT',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify(formData),
      });
      const data = await res.json();
      return { success: true, data };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
};

export const deleteBrand = async (id) => {
   try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands/${id}`, {
         method: 'DELETE',
         headers: { 'Content-Type': 'application/json' },
      });

      if (!res.ok) throw new Error('Failed to create new brand')

      return { success: true };
   } catch (error) {
      console.error('API error:', error);
      return { success: false, error };
   }
}