<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: 'error',
    validator: (value) => ['error', 'success', 'info'].includes(value),
  },
  message: {
    type: String,
    required: true,
  },
  duration: {
    type: Number,
    default: 3000, 
  },
});

const emit = defineEmits(['update:show', 'close']);

const typeClasses = {
  error: 'bg-red-500 text-white',
  success: 'bg-green-500 text-white',
  info: 'bg-blue-500 text-white',
};

const countdown = ref(Math.floor(props.duration / 1000));
let timer = null;
let countdownInterval = null;

const closeAlert = () => {
  emit('update:show', false);
  emit('close');
};

const startTimer = () => {
  clearTimeout(timer);
  clearInterval(countdownInterval);

  countdown.value = Math.floor(props.duration / 1000);

  if (props.duration > 0) {
    countdownInterval = setInterval(() => {
      countdown.value -= 1;
      if (countdown.value <= 0) {
        clearInterval(countdownInterval);
      }
    }, 1000);

    timer = setTimeout(() => {
      closeAlert();
    }, props.duration);
  }
};

watch(
  () => props.show,
  (newValue) => {
    if (newValue) {
      startTimer();
    } else {
      clearTimeout(timer);
      clearInterval(countdownInterval);
    }
  }
);

if (props.show) {
  startTimer();
}
</script>
 
<template>
    <Transition name="slide-fade">
      <div
        v-if="show"
        :class="[
          'fixed top-4 right-4 max-w-sm p-4 rounded-lg shadow-lg flex items-center gap-3',
          typeClasses[type],
        ]"
        role="alert"
      >
        <div class="flex-shrink-0">
          <svg
            v-if="type === 'error'"
            xmlns="http://www.w3.org/2000/svg"
            class="h-5 w-5"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <circle cx="12" cy="12" r="10" />
            <line x1="12" y1="8" x2="12" y2="12" />
            <line x1="12" y1="16" x2="12.01" y2="16" />
          </svg>
        </div>
        
        <div class="flex-1">
          <p class="text-sm font-medium">
            {{ message }}
            <span v-if="countdown > 0" class=" text-xs opacity-80 block">
              (close in {{ countdown }}s)
            </span>
          </p>
        </div>
        <button
          @click="closeAlert"
          class="flex-shrink-0 ml-2 p-1 rounded-full hover:bg-black/10 focus:outline-none"
          aria-label="Close"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-4 w-4"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          >
            <line x1="18" y1="6" x2="6" y2="18" />
            <line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        </button>
      </div>
    </Transition>
  </template>
  
  <style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from {
  transform: translateX(20px);
  opacity: 0;
}

.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}
</style>
  