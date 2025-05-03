<script setup>
import { useFavoriteStore } from '@/stores/useFavoriteStore';
import { Icon } from '@iconify/vue';

const favoriteStore = useFavoriteStore();

const removeFromFavorite = (id) => {
    favoriteStore.removeFavorite(id);
};
</script>

<template>
    <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">Your Favorite Products</h1>

    <div v-if="favoriteStore.favorites.length === 0">
    <p>No favorite products yet.</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
    <div
        v-for="product in favoriteStore.favorites"
        :key="product.id"
        class="border p-4 rounded-lg shadow"
    >
        <div class="flex justify-between items-start">
        <p class="font-semibold">{{ product.brand }} {{ product.model }}</p>
        <button @click="removeFromFavorite(product.id)">
            <Icon icon="hugeicons:favourite" class="text-red-500 text-xl" />
        </button>
        </div>
        <p class="text-sm text-gray-600">Ram: {{ product.ram }}</p>
        <p class="text-sm text-gray-600">Storage: {{ product.storage }}GB</p>
        <p class="text-sm text-gray-800 font-bold mt-2">{{ product.price }} ฿</p>
    </div>
    </div>
</div>
</template>
