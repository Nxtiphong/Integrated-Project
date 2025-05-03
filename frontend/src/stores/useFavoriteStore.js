import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useFavoriteStore = defineStore('favorite', () => {
const favorites = ref([])

  const addFavorite = (item) => {
    if (!favorites.value.find(fav => fav.id === item.id)) {
      favorites.value.push(item)
    }
  }

  const removeFavorite = (id) => {
    favorites.value = favorites.value.filter(item => item.id !== id)
  }

  const isFavorite = (id) => {
    return favorites.value.some(item => item.id === id)
  }

  const getFavorites = () => favorites.value

  return {
    favorites,
    addFavorite,
    removeFavorite,
    isFavorite,
    getFavorites
  }
})
