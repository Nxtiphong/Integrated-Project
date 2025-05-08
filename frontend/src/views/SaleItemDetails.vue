<script setup>
import ProductImage from '@/components/detail/ProductImage.vue';
import ProductSpec from '@/components/detail/ProductSpec.vue';
import { useRoute, useRouter } from 'vue-router';
import notFoundImg from '@/assets/images/404.png';
import { onMounted, ref } from 'vue';

const router = useRouter();
const route = useRoute();
const params = route.params.id;

const productDetail = ref(null);
const isLoading = ref(false);

const fetchProductDetail = async (id) => {
  isLoading.value = true;
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${id}`);
    if (!res.ok) throw new Error('Failed to fetch product id:' + id);
    productDetail.value = await res.json();
  } catch (error) {
    productDetail.value = null;
    console.log('Error:', error);
  } finally {
    isLoading.value = false;
  }
};

const handleEdit = () => {
  router.push(`/sale-items/${params}/edit`);
};

const handleDelete = () => {
  if (confirm('Are you sure you want to delete this item?')) {
    fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${params}`, {
      method: 'DELETE',
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to delete product id:' + params);
        alert('Product deleted successfully');
        router.push('/sale-items');
      })
      .catch((error) => {
        console.log('Error:', error);
      });
  }
};

onMounted(() => {
  fetchProductDetail(params);
});
</script>

<template>
  <div v-if="isLoading" class="flex flex-col justify-center items-center h-60">
    <span class="loading loading-ball loading-xl"></span>
    <p class="text-lg font-semibold">Loading product details...</p>
  </div>

  <div v-else-if="productDetail" class="container">
    <div class="breadcrumbs text-sm my-2">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/sale-items">Gallery</RouterLink></li>
        <li class="font-bold">{{ productDetail.model }}</li>
      </ul>
    </div>
    <div
      class="py-6 lg:py-10 self-stretch flex flex-col lg:flex-row items-center lg:justify-start gap-4 md:gap-6 lg:gap-12 itbms-row"
    >
      <ProductImage />
      <div class="self-stretch flex-1 flex flex-col justify-start items-start gap-2 lg:gap-4 mt-8">
        <h1 class="text-2xl md:text-3xl lg:text-4xl font-bold leading-10 itbms-model">
          {{ productDetail.model }}
        </h1>
        <h3 class="text-lg lg:text-xl font-bold leading-10 itbms-brand">
          {{ productDetail.brandName }}
        </h3>
        <div class="flex gap-1 items-end">
          <h2 class="text-xl md:text-2xl lg:text-3xl leading-8 tracking-wide itbms-price">
            {{ Number(productDetail.price).toLocaleString() }}
          </h2>
          <span class="text-lg lg:text-xl itbms-price-unit">Baht</span>
        </div>

        <p class="flex gap-1">
          <span class="text-lg lg:text-xl itbms-quantity">
            {{ productDetail.quantity }}
          </span>
          <span class="text-lg itbms-quantity-unit">units</span>
        </p>
        <ProductSpec :product="productDetail" />
        <p class="text-sm md:text-lg lg:text-xl text-secondary-accent itbms-description">
          {{ productDetail.description }}
        </p>
        <div class="mt-4">
          <div class="flex flex-col md:flex-row gap-2">
            <div class="itbms-edit-button">
              <button className="btn btn-outline btn-warning" @click="handleEdit">Edit</button>
            </div>
            <div class="itbms-delete-button">
              <button className="btn btn-outline btn-error" @click="handleDelete">Delete</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>


  <div
    v-else
    class="flex flex-col items-center justify-center m-10 p-5 shadow-lg border rounded-2xl bg-white border-neutral-200 gap-2"
  >
    <img :src="notFoundImg" alt="not_found" />
    <h1 class="text-3xl font-bold uppercase">Something is wrong</h1>
    <p class="itbms-message">The requested sale item does not exist.</p>
    <button @click="router.push('/sale-items')" class="itbms-button btn btn-primary my-2">
      Back
    </button>
  </div>
</template>

<style scoped></style>
