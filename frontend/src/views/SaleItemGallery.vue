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
      <div class="Itmbs- font-medium text-primary flex justify-center items-center low-h-screen " v-else >
    <p>! No sale items</p>
  </div>
  </div>
</template>

<script setup>
import { computed, ref }from 'vue'
import SaleItemCard from '@/components/gallery/SaleItemCard.vue'


const saleItems = ref([
  {
    id: 1,
    brandName: 'Apple04',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    quantity: 5,
    createdOn: "2025-04-29 18:33:34",
    updatedOn: "2025-04-29 18:33:34",
    image: '@/assets/images/mock_phone.png'

  },
  {
    id: 2,
    brandName: 'Apple05',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-05-29 17:20:00",
    updatedOn: "2025-05-29 17:20:00",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 3,
    brandName: 'Apple06',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-06-29 16:45:12",
    updatedOn: "2025-06-29 16:45:12",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 4,
    brandName: 'Apple07',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-07-29 15:15:55",
    updatedOn: "2025-07-29 15:15:55",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 5,
    brandName: 'Apple08',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-08-29 14:10:30",
    updatedOn: "2025-08-29 14:10:30",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 6,
    brandName: 'Apple09',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-09-29 13:50:25",
    updatedOn: "2025-09-29 13:50:25",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 7,
    brandName: 'Apple10',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-10-29 12:35:10",
    updatedOn: "2025-10-29 12:35:10",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 8,
    brandName: 'Apple11',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-11-29 11:00:00",
    updatedOn: "2025-11-29 11:00:00",
    image: 'https://via.placeholder.com/300x200'
  },
  {
    id: 9,
    brandName: 'Apple12',
    model: 'iPhone 14 Pro Max',
    ramGb: '6 GB',
    price: 42900,
    storageGb: '512 ',
    color: 'Space Black',
    createdOn: "2025-12-29 10:20:00",
    updatedOn: "2025-12-29 10:20:00",
    image: 'https://via.placeholder.com/300x200'
  }
].sort((a, b) => {
  const updatedB = new Date(a.createdOn)
  const createdOnA = new Date(b.createdOn)
  return createdOnA - updatedB                // เรียงData Mock
}
))
</script>

<style scoped>
</style>
