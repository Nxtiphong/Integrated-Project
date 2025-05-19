import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import SaleItemGallery from '@/views/SaleItemGallery.vue'
import SaleItemDetails from '@/views/SaleItemDetails.vue'
import Favorite from '@/views/Favorite.vue'
import AddSaleItem from '@/views/AddSaleItem.vue'
import NotFoundPage from '@/components/share/NotFoundPage.vue'
import SaleItemList from '@/views/SaleItemList.vue'
import BrandList from '@/views/BrandList.vue'
import AddBrand from '@/views/AddBrand.vue'

const router = createRouter({
  history: createWebHistory('/tt2'),
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
      path: '/brands',
      name: "brandList",
      component: BrandList
    },
    {
      path: '/brands/add',
      name: "addBrand",
      component: AddBrand
    },
    {
      path: '/brands/:id/edit',
      name: "editBrand",
      component: AddBrand
    },
    {
      path: '/brands/:pathMatch(.*)*',
      name: 'brandNotFound',
      component: NotFoundPage,
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
