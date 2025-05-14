<script setup>
import BrandTable from '@/components/brand/BrandTable.vue';
import { Icon } from '@iconify/vue';
</script>

<template>
  <section>
    <div
      class="container mx-auto breadcrumbs flex items-center justify-between text-sm my-2 overflow-x-auto whitespace-nowrap"
    >
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items/list">Sale Item List</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items/brand">Brand List</RouterLink>
        </li>
      </ul>

      <div class="flex items-center justify-end">
        <RouterLink to="/sale-items/brand/add">
          <button class="btn btn-primary">
            Add Brand
            <Icon icon="gg:add" class="text-xl" />
          </button>
        </RouterLink>
      </div>
    </div>
    <BrandTable />
  </section>
</template>

<style scoped></style>
