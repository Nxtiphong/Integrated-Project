import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useRegisterFormStore = defineStore('registerForm', () => {
  const isCreateSuccess = ref(false);
  const isCreateFailed = ref(false);
  const isUpdatedSuccess = ref(false);
  const isUpdatedFailed = ref(false);

  return {
    isCreateSuccess,
    isCreateFailed,
    isUpdatedSuccess,
    isUpdatedFailed,
  };
});
