<script setup>
import BrandTable from '@/components/brand/BrandTable.vue';
import Alert from '@/components/share/Alert.vue';
import { useBrandStore } from '@/stores/useBrandStore';
import { Icon } from '@iconify/vue';
import { onMounted, ref } from 'vue';

const brandStore = useBrandStore();

const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
});

onMounted(() => {
  if (brandStore.isCreateSuccess) {
    alertMessage.value = {
      type: 'success',
      message: 'The brand has been added',
      visible: true,
    };
    brandStore.isCreateSuccess = false;
  }

  if (brandStore.isCreateFailed) {
    alertMessage.value = {
      type: 'error',
      message: 'The brand could not be added',
      visible: true,
    };
    brandStore.isCreateFailed = false;
  }

  if (brandStore.isUpdatedSuccess) {
    alertMessage.value = {
      type: 'success',
      message: 'The brand has been updated',
      visible: true,
    };
    brandStore.isUpdatedSuccess = false;
  }

  if (brandStore.isUpdatedFailed) {
    alertMessage.value = {
      type: 'error',
      message: 'The brand does not exist',
      visible: true,
    };
    brandStore.isUpdatedFailed = false;
  }
});
</script>

<template>
  <section>
    <div
      class="container mx-auto breadcrumbs flex items-center justify-between text-sm my-2 overflow-x-auto whitespace-nowrap"
    >
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items">Gallery</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items/list">Sale Item List</RouterLink>
        </li>
        <li>
          <RouterLink to="/sale-items/brand">Brand List</RouterLink>
        </li>
      </ul>

      <div class="flex items-center justify-end">
        <RouterLink to="/sale-items/brand/add">
          <button class="btn btn-primary">
            Add Brand
            <Icon icon="gg:add" class="text-xl" />
          </button>
        </RouterLink>
      </div>
    </div>
    <BrandTable :brand-list="brandStore.brandLists" />
  </section>

  <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
  />
</template>

<style scoped></style>
