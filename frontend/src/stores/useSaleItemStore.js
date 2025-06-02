import { httpRequest } from '@/utils/fetchUtils'
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
        const res = await httpRequest('GET', `v1/brands`)
        if (res.status !== 200) {
          throw new Error("Failed to fetch")
        }
        this.brands = res.data
      } catch (error) {
        console.error('Error fetching brands:', error)
      } finally {
        this.isLoading = false
      }
    },

    async fetchSaleItems() {
      this.isLoading = true
      try {
        const res = await httpRequest('GET', `v1/sale-items`)
        if (res.status !== 200) {
          throw new Error("Failed to fetch")
        }
        this.saleItems = res.data
      } catch (error) {
        console.error('Error fetching sale items:', error)
      } finally {
        this.isLoading = false
      }
    },
    async fetchSaleItemById(id) {
      try {
        const res = await httpRequest('GET', `v1/sale-items/${id}`)
        if (res.status !== 200) {
          throw new Error("Failed to fetch")
        }
        return res.data
      } catch (error) {
        console.error('Error fetching sale item:', error)
      }
    },

  }
})
