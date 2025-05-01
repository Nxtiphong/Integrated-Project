<template>
  <div>
    <h1 class=" m-3">Sale Items</h1>
    <div class="breadcrumbs text-sm my-1 m-10">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/sale-items">Gallery</RouterLink></li>
      </ul>
    </div>
    <div v-if="saleItems.length > 0" class=" grid grid-cols-5 gap-4 p-5">
      <SaleItemCard
        v-for="item in saleItems"
        :key="item.id"
        :brand="item.brandName"
        :model="item.model"
        :ram="item.ramGb"
        :price="item.price"
        :storage="item.storageGb"
        :color="item.color"
        :image="item.image"
        :id="item.id"
      />
    </div>
      <div class="Itmbs-" v-else >
    <p>No sale items</p>
  </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import SaleItemCard from '@/components/gallery/SaleItemCard.vue'
const saleItems = ref([
  {
    id: 1,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: '@/assets/images/mock_phone.png'
  },
  {
    id: 2,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 3,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 4,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 5,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 6,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 7,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 8,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 9,
    brandName: 'Apple',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    image: 'https://via.placeholder.com/300x200'
  }
  
])
</script>

<style scoped>
</style>
