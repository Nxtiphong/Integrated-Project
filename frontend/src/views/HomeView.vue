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
      class="container mx-auto px-4 py-16 flex flex-col md:flex-row items-center justify-between"
    >
      <div class="md:w-1/2 z-10 space-y-6">
        <h3
          :class="[
            'text-gray-400 text-xl font-light tracking-wider transform transition-all duration-700',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          Pro.Beyond.
        </h3>

        <h1
          :class="[
            'text-5xl md:text-7xl font-thin tracking-tight transition-all duration-700',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          iPhone 14 <span class="font-bold">Pro</span>
        </h1>

        <p
          :class="[
            'text-gray-400 text-lg font-light transition-all duration-700 delay-300',
            isVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          Created to change everything for the better. For everyone
        </p>

        <div
          :class="[
            'pt-4 transition-all duration-700 delay-500',
            buttonVisible ? 'translate-y-0 opacity-100' : 'translate-y-6 opacity-0',
          ]"
        >
          <button
            @click="router.push('/sale-items')"
            class="Itbms-shopnow border border-white px-8 py-3 rounded cursor-pointer hover:bg-white hover:text-black transition-all duration-300 group"
          >
            <span class="flex items-center justify-center">
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
      </div>
      <div class="z-999">
        <img src="@/assets/images/mock_phone.png" alt="IMG" class="IMGPhoneHome" width="375px" />
      </div>
    </div>
  </div>
</template>

<style>
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
</style>
