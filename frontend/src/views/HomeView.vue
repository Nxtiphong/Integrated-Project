<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const isVisible = ref(false);
const phoneVisible = ref(false);
const buttonVisible = ref(false);

const router = useRouter();

function getRandomStarStyle() {
  const startPositionX = Math.random() * 100;
  const startPositionY = Math.random() * 50;
  const animationDuration = 0.3 + Math.random() * 0.7;
  const animationDelay = Math.random() * 5;

  return {
    left: `${startPositionX}%`,
    top: `${startPositionY}%`,
    animationDuration: `${animationDuration}s`,
    animationDelay: `${animationDelay}s`,
  };
}

onMounted(() => {
  setTimeout(() => (isVisible.value = true), 400);
  setTimeout(() => (phoneVisible.value = true), 800);
  setTimeout(() => (buttonVisible.value = true), 1200);
});
</script>

<template>
  <div
    class="bg-black text-white min-h-screen flex items-center justify-center overflow-hidden relative"
  >
    <div class="shooting-stars-container">
      <div v-for="n in 30" :key="n" class="shooting-star" :style="getRandomStarStyle()"></div>
    </div>

    <div class="absolute top-1/2 right-1/4 w-64 h-64 bg-purple-600/20 rounded-full blur-3xl"></div>
    <div class="absolute bottom-1/3 left-1/4 w-48 h-48 bg-blue-600/10 rounded-full blur-3xl"></div>

    <div
      class="container mx-auto px-4 md:px-8 py-8 md:py-16 flex flex-col md:flex-row items-center justify-between"
    >
      <div class="w-full md:w-1/2 z-10 space-y-4 md:space-y-6 text-center md:text-left mb-8 md:mb-0">
        <h3
          :class="[
            'text-gray-400 text-lg md:text-xl font-light tracking-wider transform transition-all duration-700',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          Pro.Beyond.
        </h3>

        <h1
          :class="[
            'text-4xl sm:text-5xl md:text-6xl lg:text-7xl font-thin tracking-tight transition-all duration-700',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          iPhone 14 <span class="font-bold">Pro</span>
        </h1>

        <p
          :class="[
            'text-gray-400 text-base md:text-lg font-light transition-all duration-700 delay-300 max-w-md mx-auto md:mx-0',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          Created to change everything for the better. For everyone
        </p>

        <div class="flex gap-3">
          <div
          :class="[
            'pt-4 transition-all duration-700 delay-500',
            buttonVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          <button
            @click="router.push('/sale-items')"
            class="itbms-shopnow border border-white px-6 sm:px-8 py-2 sm:py-3 rounded cursor-pointer bg-white hover:bg-gray-300 hover:text-black transition-all duration-300 group"
          >
            <span class="flex items-center justify-center text-black">
              Shop Now
              <svg
                class="w-4 h-4 ml-2 transform transition-transform duration-300 group-hover:translate-x-1"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M14 5l7 7m0 0l-7 7m7-7H3"
                ></path>
              </svg>
            </span>
          </button>
        </div>
        <div
          :class="[
            'pt-4 transition-all duration-700 delay-500',
            buttonVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          <button
            @click="router.push('/sale-items/list')"
            class="itbms-shopnow border border-white px-6 sm:px-8 py-2 sm:py-3 rounded cursor-pointer hover:bg-white hover:text-black transition-all duration-300 group"
          >
            <span class="flex items-center justify-center">
              Sale Items list
              <svg
                class="w-4 h-4 ml-2 transform transition-transform duration-300 group-hover:translate-x-1"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M14 5l7 7m0 0l-7 7m7-7H3"
                ></path>
              </svg>
            </span>
          </button>
        </div>
        </div>
      </div>

      <!-- Phone image section -->
      <div 
        class="z-10 w-full md:w-1/2 flex justify-center"
        :class="[
          'transition-all duration-700 delay-400',
          phoneVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
        ]"
      >
        <img 
          src="@/assets/images/mock_phone.png" 
          alt="iPhone 14 Pro" 
          class="IMGPhoneHome w-64 sm:w-72 md:w-80 lg:w-96 max-w-full h-auto" 
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes pulse-slow {
  0%,
  100% {
    opacity: 0.5;
  }
  50% {
    opacity: 0.8;
  }
}

.animate-pulse-slow {
  animation: pulse-slow 3s ease-in-out infinite;
}

.shooting-stars-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.shooting-star {
  position: absolute;
  top: -5px;
  background: linear-gradient(to bottom, #fff, rgba(255, 255, 255, 0.2));
  border-radius: 0;
  opacity: 0;
  animation: shooting-star-animation linear infinite;
  box-shadow: 0 0 2px #fff;
  height: 10px !important;
  width: 1px !important;
}

@keyframes shooting-star-animation {
  0% {
    transform: translateY(0);
    opacity: 0;
  }
  10% {
    opacity: 1;
    height: 10px;
  }
  100% {
    transform: translateY(20vh);
    opacity: 0;
    height: 15px;
  }
}

button:hover {
  box-shadow: 0 0 15px rgba(255, 255, 255, 0.5);
}

@media (max-width: 640px) {
  .shooting-stars-container {
    opacity: 0.7;
  }
}

@media (max-width: 480px) {
  .container {
    padding-top: 2rem;
    padding-bottom: 2rem;
  }
}
</style>