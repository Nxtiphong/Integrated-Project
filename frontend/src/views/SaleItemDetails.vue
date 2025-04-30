<script setup>
import ProductImage from '@/components/detail/ProductImage.vue';
import ProductSpec from '@/components/detail/ProductSpec.vue';
import { useRoute } from 'vue-router';
import { mockData } from '@/data/sample';

const route = useRoute();

const params = route.params.id;
</script>

<template>
  <div class="container">
    <div class="breadcrumbs text-sm my-4">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li><RouterLink to="/sale-items">Gallery</RouterLink></li>
        <li class="font-bold">{{ mockData.model }}</li>
      </ul>
    </div>
    <div class="py-10 self-stretch flex items-center justify-start gap-12">
      <ProductImage />
      <div class="self-stretch flex-1 flex flex-col justify-start items-start gap-8 w-">
        <h1 class="text-4xl font-bold leading-10">{{ mockData.model }}</h1>
        <h2 class="text-3xl leading-[48px] tracking-wide">
          {{ mockData.price }} Baht
          <span class="text-xl text-neutral">{{ mockData.quantity }} qty in stock</span>
        </h2>
        <ProductSpec :product="mockData" />
        <p class="text-xl text-secondary-accent">{{ mockData.description }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
