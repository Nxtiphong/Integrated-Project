<script setup>
import { Icon } from '@iconify/vue';
import { RouterLink } from 'vue-router';
import { ref } from 'vue';

const isMobileMenuOpen = ref(false);

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
};
</script>

<template>
  <nav class="navbar px-4 sm:px-6 md:px-8 lg:px-12 xl:px-20 flex justify-between border-b-black-200 w-full py-2 shadow-md relative">
    <div class="flex items-center space-x-2 sm:space-x-4">
      <div class="flex items-center">
        <Icon icon="ic:baseline-apple" class="text-xl sm:text-2xl" />
        <span class="text-lg sm:text-xl font-semibold"><RouterLink to="/">ITBMS</RouterLink></span>
      </div>
      
      <div class="relative hidden md:block">
        <Icon icon="hugeicons:search-02" class="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500" />
        <input
          type="text"
          placeholder="Search"
          class="bg-[#F5F5F5] focus:bg-white focus:outline-none focus:ring-2 focus:ring-primary/50 pl-10 pr-4 py-2 rounded-md w-40 lg:w-64 xl:w-96 transition duration-200"
        />
      </div>
    </div>
    <button @click="toggleMobileMenu" class="md:hidden text-2xl focus:outline-none">
      <Icon icon="heroicons:bars-3" v-if="!isMobileMenuOpen" />
      <Icon icon="heroicons:x-mark" v-else />
    </button>

    <div class="hidden md:flex items-center space-x-4 lg:space-x-6">
      <ul class="flex gap-2 lg:gap-4 text-neutral">
        <li class="transition duration-300 hover:opacity-50">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="transition duration-300 hover:opacity-50">
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
      </ul>
      <div class="flex gap-2 lg:gap-4 text-neutral">
        <Icon icon="mdi-light:account" class="text-xl lg:text-2xl transition duration-300 hover:opacity-50" />
        <RouterLink to="/favorites">
          <Icon icon="hugeicons:favourite" class="text-xl lg:text-2xl transition duration-300 hover:opacity-50" />
        </RouterLink>
        <Icon icon="mdi-light:cart" class="text-xl lg:text-2xl transition duration-300 hover:opacity-50" />
      </div>
    </div>
    
    <div 
      v-if="isMobileMenuOpen" 
      class="absolute top-full left-0 right-0 bg-white shadow-lg md:hidden z-50"
    >
      <div class="px-4 py-3 relative">
        <Icon icon="hugeicons:search-02" class="absolute left-7 top-1/2 transform -translate-y-1/2 text-gray-500" />
        <input
          type="text"
          placeholder="Search"
          class="bg-[oklch(90%_0.02_0)] focus:bg-white focus:outline-none focus:ring-2 focus:ring-primary/50 pl-10 pr-4 py-2 rounded-md w-full transition duration-200"
        />
      </div>
      
      <ul class="px-4 py-2">
        <li class="py-2 border-b border-gray-100">
          <RouterLink @click="isMobileMenuOpen = false" to="/" class="block w-full">Home</RouterLink>
        </li>
        <li class="py-2 border-b border-gray-100">
          <RouterLink @click="isMobileMenuOpen = false" to="/sale-items" class="block w-full">Gallery</RouterLink>
        </li>
      </ul>
      
      <div class="flex justify-around px-4 py-3 border-t border-gray-100">
        <div class="flex flex-col items-center">
          <Icon icon="mdi-light:account" class="text-2xl" />
          <span class="text-xs mt-1">Account</span>
        </div>
        <RouterLink @click="isMobileMenuOpen = false" to="/favorites" class="flex flex-col items-center">
          <Icon icon="hugeicons:favourite" class="text-2xl" />
          <span class="text-xs mt-1">Favorites</span>
        </RouterLink>
        <div class="flex flex-col items-center">
          <Icon icon="mdi-light:cart" class="text-2xl" />
          <span class="text-xs mt-1">Cart</span>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
@media (max-width: 768px) {
  .navbar {
    padding-top: 0.75rem;
    padding-bottom: 0.75rem;
  }
}
</style>