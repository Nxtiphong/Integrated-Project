<template>
    <Teleport to="body">
      <Transition name="modal-fade">
        <div v-if="modelValue" class="fixed inset-0 z-50 overflow-y-auto" @click.self="cancel">
          <div class="flex min-h-screen items-center justify-center p-4 text-center">
            <div 
              class="relative w-full max-w-md transform overflow-hidden rounded-lg bg-white p-6 text-left shadow-xl transition-all"
              @click.stop
            >
              <!-- Close button -->
              <button 
                type="button" 
                class="absolute right-4 top-4 text-gray-400 hover:text-gray-500 focus:outline-none" 
                @click="cancel"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <line x1="18" y1="6" x2="6" y2="18"></line>
                  <line x1="6" y1="6" x2="18" y2="18"></line>
                </svg>
              </button>
              
              <!-- Warning icon -->
              <div class="mx-auto flex h-12 w-12 flex-shrink-0 items-center justify-center rounded-full bg-red-100">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-red-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                  <line x1="12" y1="9" x2="12" y2="13"></line>
                  <line x1="12" y1="17" x2="12.01" y2="17"></line>
                </svg>
              </div>
              
              <!-- Title -->
              <h3 class="mt-4 text-center text-lg font-medium text-gray-900">
                {{ title }}
              </h3>
              
              <!-- Message -->
              <div class="mt-3">
                <p class="text-center text-sm text-gray-500">
                  {{ message }}
                </p>
              </div>
              
              <!-- Buttons -->
              <div class="mt-5 flex justify-center gap-3">
                <button
                  type="button"
                  class="inline-flex justify-center cursor-pointer rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                  @click="cancel"
                >
                  {{ cancelButtonText }}
                </button>
                <button
                  type="button"
                  class="inline-flex cursor-pointer justify-center rounded-md border border-transparent bg-red-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                  @click="confirm"
                >
                  {{ confirmButtonText }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </Transition>
      
      <!-- Backdrop -->
      <Transition name="backdrop-fade">
        <div v-if="modelValue" class="fixed inset-0 bg-black bg-opacity-50 z-40"></div>
      </Transition>
    </Teleport>
  </template>
  
  <script setup>
  import { onMounted, onUnmounted } from 'vue';
  
  const props = defineProps({
    modelValue: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      default: 'Confirm Delete'
    },
    message: {
      type: String,
      default: 'Are you sure you want to delete this item? This action cannot be undone.'
    },
    confirmButtonText: {
      type: String,
      default: 'Delete'
    },
    cancelButtonText: {
      type: String,
      default: 'Cancel'
    }
  });
  
  const emit = defineEmits(['update:modelValue', 'confirm', 'cancel']);
  
  const confirm = () => {
    emit('confirm');
    emit('update:modelValue', false);
  };
  
  const cancel = () => {
    emit('cancel');
    emit('update:modelValue', false);
  };
  
  // Handle ESC key press
  const handleKeyDown = (e) => {
    if (e.key === 'Escape' && props.modelValue) {
      cancel();
    }
  };
  
  onMounted(() => {
    document.addEventListener('keydown', handleKeyDown);
  });
  
  onUnmounted(() => {
    document.removeEventListener('keydown', handleKeyDown);
  });
  </script>
  
  <style scoped>
  .modal-fade-enter-active,
  .modal-fade-leave-active {
    transition: opacity 0.3s, transform 0.3s;
  }
  
  .modal-fade-enter-from,
  .modal-fade-leave-to {
    opacity: 0;
    transform: scale(0.95);
  }
  
  .backdrop-fade-enter-active,
  .backdrop-fade-leave-active {
    transition: opacity 0.3s;
  }
  
  .backdrop-fade-enter-from,
  .backdrop-fade-leave-to {
    opacity: 0;
  }
  </style>