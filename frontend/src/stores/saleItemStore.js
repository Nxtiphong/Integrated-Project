// src/stores/saleItemStore.js
import { defineStore } from 'pinia'

export const useSaleItemStore = defineStore('saleItem', {
  state: () => ({
    brands: [],
    saleItems: [],
  }),

  actions: {
    async fetchBrands() {
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`)
        if (!res.ok) throw new Error('Failed to fetch brands')
        this.brands = await res.json()
      } catch (error) {
        console.error('Error fetching brands:', error)
      } finally {
      }
    },

    async fetchSaleItems() {
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`)
        if (!res.ok) throw new Error('Failed to fetch sale items')
        this.saleItems = await res.json()
      } catch (error) {
        console.error('Error fetching sale items:', error)
      } finally {
      }
    },
    async fetchSaleItemById(id) {
        try {
            const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${id}`)
            if (!res.ok) throw new Error('Failed to fetch sale item')
            return await res.json()
        } catch (error) {
            console.error('Error fetching sale item:', error)
        }
        },

    async createSaleItem(payload) {
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        })
        if (!res.ok) throw new Error('Failed to create sale item')
        const newItem = await res.json()
        this.saleItems.push(newItem)
      } catch (error) {
        console.error('Error creating sale item:', error)
      }
    },

    async updateSaleItem(id, payload) {
      try {
        const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(payload),
        })
        if (!res.ok) throw new Error('Failed to update sale item')
        const updated = await res.json()
        const index = this.saleItems.findIndex(item => item.id === id)
        if (index !== -1) this.saleItems[index] = updated
      } catch (error) {
        console.error('Error updating sale item:', error)
      }
    }
  }
})
