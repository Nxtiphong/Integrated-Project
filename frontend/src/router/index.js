import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SaleItemGallery from '@/views/SaleItemGallery.vue'
import SaleItemDetails from '@/views/SaleItemDetails.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/sale-items',
      name: "saleItems",
      component: SaleItemGallery
    },
    {
      path: '/sale-items/:id',
      name: "saleItemDetail",
      component: SaleItemDetails
    }
  ],
  linkActiveClass: "text-black font-semibold"
})

export default router
