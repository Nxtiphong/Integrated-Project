<script setup>
import HomeBannerImage from '@/assets/images/home_banner.png';
import { useRouter } from 'vue-router';

const router = useRouter();
</script>

<template>
  <div class="relative w-full min-h-[400px] lg:min-h-auto">
    <img
      :src="HomeBannerImage"
      alt="Home_Banner"
      class="lg:w-full h-[400px] lg:h-full object-cover lg:object-contain"
    />
    <div class="absolute inset-0 bg-black/20 flex items-center justify-center p-4">
      <section class="text-white text-center px-4 max-w-4xl mx-auto">
        <h1
          class="text-3xl sm:text-4xl md:text-5xl lg:text-6xl xl:text-7xl font-thin leading-tight sm:leading-snug md:leading-[1.1] lg:leading-[1.05] xl:leading-[72px] font-['Inter'] mb-4 sm:mb-6"
        >
          Big Summer <span class="font-medium">Sale</span>
        </h1>
        <p
          class="text-sm sm:text-base md:text-lg lg:text-xl mb-6 sm:mb-8 max-w-xs sm:max-w-md lg:max-w-lg mx-auto leading-relaxed"
        >
          Discover unbeatable deals on the latest smartphones this summer. Upgrade your phone and
          save big during our limited-time sale!
        </p>
        <button
          @click="router.push('/sale-items')"
          class="border border-white px-4 sm:px-6 py-2 sm:py-3 text-sm sm:text-base cursor-pointer rounded hover:bg-white hover:text-black transition-all duration-300 inline-flex items-center gap-2 font-medium"
        >
          Shop Now
          <svg
            class="w-4 h-4 transform transition-transform duration-300 group-hover:translate-x-1"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M14 5l7 7m0 0l-7 7m7-7H3"
            ></path>
          </svg>
        </button>
      </section>
    </div>
  </div>
</template>

<style scoped>
@media (max-width: 320px) {
  .text-3xl {
    font-size: 1.75rem;
  }
}

button {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

button:hover svg {
  transform: translateX(4px);
}

@media (max-width: 640px) {
  .absolute.inset-0 {
    background: linear-gradient(
      180deg,
      rgba(0, 0, 0, 0.3) 0%,
      rgba(0, 0, 0, 0.5) 50%,
      rgba(0, 0, 0, 0.3) 100%
    );
  }
}
</style>
