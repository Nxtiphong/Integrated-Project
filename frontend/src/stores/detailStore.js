import { ref } from 'vue'
import { defineStore } from 'pinia'

export const useProductDetailStore = defineStore('productDetail', () => {
  const productDetail = ref(null)
  const isLoading = ref(false)
  const isError = ref(null)

  const fetchProductDetail = async (id) => {
    isLoading.value = true
    isError.value = null
    productDetail.value = null
    
    try {
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/${id}`)
      if (!res.ok) throw new Error("Failed to fetch product id: " + id)
      productDetail.value = await res.json()
    } catch (error) {
      productDetail.value = null
      isError.value = error.message || 'Failed to fetch product id: ' + id
      throw error
    } finally {
      isLoading.value = false
    }
  }

  return { productDetail, isLoading, isError, fetchProductDetail }
})
