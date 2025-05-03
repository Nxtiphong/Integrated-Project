<script setup>
import { RouterLink } from 'vue-router';
</script>

<template>
  <nav class="navbar px-10 lg:px-16 xl:px-20 flex justify-between border-b-black-200 w-full py-2 shadow-md">
    <div class="flex items-center space-x-4">
      <div class="flex">
        <span class="text-xl font-semibold"><RouterLink to="/">ITBMS</RouterLink></span>
      </div>
      <div class="hidden md:block">
        <input
          type="text"
          placeholder="Search"
          class="bg-[oklch(90%_0.02_0)] focus:bg-white focus:outline-none focus:ring-2 focus:ring-primary/50 px-4 py-3 rounded-md w-96 transition duration-200"
        />
      </div>
    </div>
    <div>
      <ul class="flex gap-4 text-neutral">
        <li class="transition duration-300 hover:opacity-50">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="transition duration-300 hover:opacity-50">
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
      </ul>
    </div>
  </nav>
</template>

<style scoped></style>
