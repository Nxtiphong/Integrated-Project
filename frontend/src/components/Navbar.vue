<script setup></script>

<template>
  <nav class="navbar container border-b border-b-base-300">
    <div class="flex-1">
      <span class="text-xl font-semibold"><router-link to="/">Logo</router-link></span>
    </div>
    <div class="flex-none">
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
