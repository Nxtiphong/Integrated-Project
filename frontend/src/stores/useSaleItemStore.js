import { defineStore } from 'pinia'

export const useSaleItemStore = defineStore('saleItem', {
  state: () => ({
    brands: [],
    saleItems: [],
    isLoading: false,
    created: false,
    deleted: false,
    updated: false,
    noExist: false,
  }),

  actions: {
    async fetchBrands() {
      if (this.brands.length > 0) return
      this.isLoading = true
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/brands`)
        if (!res.ok) throw new Error('Failed to fetch brands')
        this.brands = await res.json()
      } catch (error) {
        console.error('Error fetching brands:', error)
      } finally {
          this.isLoading = false
      }
    },

    async fetchSaleItems() {
      this.isLoading = true
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/sale-items`)
        if (!res.ok) throw new Error('Failed to fetch sale items')
        this.saleItems = await res.json()
      } catch (error) {
        console.error('Error fetching sale items:', error)
      } finally {
          this.isLoading = false
      }
    },
    async fetchSaleItemById(id) {
        try {
            const res = await fetch(`${import.meta.env.VITE_BASE_URL}/v1/sale-items/${id}`)
            if (!res.ok) throw new Error('Failed to fetch sale item')
            return await res.json()
        } catch (error) {
            console.error('Error fetching sale item:', error)
        }
        },

  }
})
