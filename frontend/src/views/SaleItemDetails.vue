<script setup>
import ProductImage from '@/components/detail/ProductImage.vue';
import ProductSpec from '@/components/detail/ProductSpec.vue';
import { useRoute, useRouter } from 'vue-router';
import { mockData } from '@/data/sample';
import notFoundImg from '@/assets/images/404.png';
import { useProductDetailStore } from '@/stores/detailStore';
import { onMounted, ref } from 'vue';

// const store = useProductDetailStore();
// const { productDetail, isLoading, isError, fetchProductDetail } = store;

const router = useRouter();
const route = useRoute();
const params = route.params.id;

const productDetail = ref(null);
const isLoading = ref(false);

const fetchProductDetail = async (id) => {
  isLoading.value = true;
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/${id}`);
    if (!res.ok) throw new Error('Failed to fetch product id:' + id);
    productDetail.value = await res.json();
  } catch (error) {
    productDetail.value = null;
    console.log('Error:', error);
    throw new Error(error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  fetchProductDetail(params);
});
</script>

<template>
  <div v-if="isLoading" class="flex flex-col justify-center items-center h-60 text-primary">
    <span class="loading loading-ball loading-xl"></span>
    <p class="text-lg font-semibold">Loading product details...</p>
  </div>

  <div v-else-if="productDetail" class="container">
    <div class="breadcrumbs text-sm my-4">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/sale-items">Gallery</RouterLink></li>
        <li class="font-bold">{{ productDetail.model }}</li>
      </ul>
    </div>
    <div
      class="py-6 lg:py-10 self-stretch flex flex-col lg:flex-row items-center lg:justify-start gap-10 lg:gap-12 Itbms-row"
    >
      <ProductImage />
      <div class="self-stretch flex-1 flex flex-col justify-start items-start gap-2 lg:gap-4 mt-8">
        <h1 class="text-2xl md:text-3xl lg:text-4xl font-bold leading-10 Itbms-model">
          {{ productDetail.model }}
        </h1>
        <h3 class="text-lg lg:text-xl font-bold leading-10 Itbms-brand">
          {{ productDetail.brand }}
        </h3>
        <div class="flex gap-1 items-end">
          <h2 class="text-xl md:text-2xl lg:text-3xl leading-8 tracking-wide Itbms-price">
            {{ Number(productDetail.price).toLocaleString() }}
          </h2>
          <span class="text-lg lg:text-xl Itbms-price-unit">Baht</span>
        </div>

        <p class="flex gap-1">
          <span class="text-lg lg:text-xl Itbms-quantity">
            {{ productDetail.quantity }}
          </span>
          <span class="text-lg Itbms-quantity-unit">units</span>
        </p>
        <ProductSpec :product="productDetail" />
        <p class="text-sm md:text-lg lg:text-xl text-secondary-accent Itbms-description">
          {{ productDetail.description }}
        </p>
      </div>
    </div>
  </div>

  <div
    v-else
    class="flex flex-col items-center justify-center m-10 p-5 shadow-lg border rounded-2xl bg-white border-neutral-200 gap-2"
  >
    <img :src="notFoundImg" alt="not_found" />
    <h1 class="text-3xl font-bold uppercase">Something is wrong</h1>
    <p class="Itbms-message">The requested sale item does not exist.</p>
    <button @click="router.back()" class="Itbms-button btn btn-primary my-2">Back</button>
  </div>
</template>

<style scoped></style>
