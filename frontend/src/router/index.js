import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SaleItemGallery from '@/views/SaleItemGallery.vue'
import SaleItemDetails from '@/views/SaleItemDetails.vue'
import Favorite from '@/views/Favorite.vue'
import AddSaleItem from '@/views/AddSaleItem.vue'
import NotFoundPage from '@/components/share/NotFoundPage.vue'
import SaleItemList from '@/views/SaleItemList.vue'

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
    },
    {
      path: '/favorites',
      name: "favorites",
      component: Favorite
    },
    {
      path: '/sale-items/add',
      name: "addSaleItem",
      component: AddSaleItem
    },
    {
      path: '/sale-items/:id/edit',
      name: "editSaleItem",
      component: AddSaleItem
    },
    {
      path: '/sale-items/list',
      name: "saleItemList",
      component: SaleItemList
    },
    {
      path: '/:pathMatch(.*)*', 
      name: 'NotFound',
      component: NotFoundPage,
    }
    ],
  linkActiveClass: "text-black font-semibold"
})

export default router
