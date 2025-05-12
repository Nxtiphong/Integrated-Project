
<script setup>
import { useRouter } from 'vue-router';
import notFoundImg from '@/assets/images/404.png';
import Alert from './Alert.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import { onMounted } from 'vue';
import { ref } from 'vue';

const router = useRouter();
const saleStore = useSaleItemStore();
const alertMessage = ref({
  type: '',
  message: '',
  visible: false,
  duration: 3000,
});

onMounted(() => {
  if (saleStore.noExist) {
    alertMessage.value = {
      type: 'error',
      message: 'The requested sale item does not exist.',
      visible: true,
      duration: 3000,
    };
    saleStore.noExist = false;
  }

});
</script>

<template>
  <div>
    <div
      class="flex flex-col items-center justify-center m-10 p-5 shadow-lg border rounded-2xl bg-white border-neutral-200 gap-2"
    >
      <img :src="notFoundImg" alt="not_found" />
      <h1 class="text-3xl font-bold uppercase">Something is wrong</h1>
      <p class="itbms-message">The requested sale item does not exist.</p>
      <button @click="router.push('/sale-items')" class="itbms-button btn btn-primary my-2">
        Back
      </button>
    </div>
  </div>
  <div class="itbms-message">
    <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
    :duration="alertMessage.duration"
  />
  </div>
</template>

<style scoped></style>
