<template>
  <div class="container">
    <div class="breadcrumbs text-sm my-4">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/sale-items">Gallery</RouterLink></li>
      </ul>
    </div>
    <div v-if="saleItems.length > 0" class="grid grid-cols-5 gap-4 py-5">
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
import { onMounted, ref } from 'vue';
import SaleItemCard from '@/components/gallery/SaleItemCard.vue'
// const saleItems = ref([
//   {
//     id: 1,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: '@/assets/images/mock_phone.png'
//   },
//   {
//     id: 2,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 3,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 4,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 5,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 6,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 7,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 8,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   },
//   {
//     id: 9,
//     brandName: 'Apple',
//     model: 'iPhone 14 Pro Max',
//     ramGb: '6 GB',
//     price: 42900,
//     storageGb: '512 ',
//     color: 'Space Black',
//     image: 'https://via.placeholder.com/300x200'
//   }
  
]).sort((a, b) => {
  const updatedB = new Date(a.createdOn)
  const createdOnA = new Date(b.createdOn)
  return createdOnA - updatedB   
})
const fetchSaleItems = async () => {
  const response = await fetch('http://ip24tt2.sit.kmutt.ac.th:8080/itb-mshop/v1/sale-item');
  const data = await response.json();
  return data;
};
const saleItems = ref([]);
const loadSaleItems = async () => {
  try {
    const items = await fetchSaleItems();
    saleItems.value = items;
    console.log('Sale items:', saleItems.value);
  } catch (error) {
    console.error('Error fetching sale items:', error);
  }
};

onMounted(() => {
  loadSaleItems();
});
</script>

<style scoped></style>
