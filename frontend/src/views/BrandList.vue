<script setup>
import BrandTable from '@/components/brand/BrandTable.vue';
import Alert from '@/components/share/Alert.vue';
import { useBrandStore } from '@/stores/useBrandStore';
import { deleteBrand } from '@/utils/brandUtils';
import { Icon } from '@iconify/vue';
import { onMounted, ref } from 'vue';

const brandStore = useBrandStore();

const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
});
const isLoading = ref(false);

const getAllBrands = async () => {
  isLoading.value = true;
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/brands`);
    if (!res.ok) throw new Error('Failed to fetch brands');
    brandStore.brandLists = await res.json();
  } catch (error) {
    console.error('API error:', error);
    return { success: false, error };
  } finally {
    isLoading.value = false;
  }
};

const handleDeleteSubmit = async (deleteBrandId) => {
  if (!deleteBrandId) {
    alertMessage.value = {
      type: 'error',
      message: 'Brand does not exists',
      visible: true,
    };
    return;
  }

  const res = await deleteBrand(deleteBrandId);

  if (res.success) {
    alertMessage.value = {
      type: 'success',
      message: 'The brand has been deleted',
      visible: true,
    };

    brandStore.deleteBrandLists(deleteBrandId);
  } else {
    alertMessage.value = {
      type: 'error',
      message: 'An error has occurred, the brand does not exist.',
      visible: true,
    };
  }
};

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
  getAllBrands();
});
</script>

<template>
  <section>
    <div
      class="container mx-auto breadcrumbs flex flex-col sm:flex-row sm:items-center justify-between text-sm my-2 overflow-x-auto whitespace-nowrap"
    >
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="itbms-item-list">
          <RouterLink to="/sale-items/list">Sale Item List</RouterLink>
        </li>
        <li class="itbms-manage-brand">
          <RouterLink to="/sale-items/brands">Brand List</RouterLink>
        </li>
      </ul>

      <div class="flex items-center justsat mt-2 md:m-0 sm:justify-end">
        <RouterLink to="/brands/add">
          <button class="itbms-add-button btn btn-primary btn-sm md:btn-md">
            Add Brand
            <Icon icon="gg:add" class="md:text-xl" />
          </button>
        </RouterLink>
      </div>
    </div>

    <div v-if="isLoading" class="text-center py-4">
      <span class="loading loading-dots loading-lg text-primary"></span>
    </div>
    <BrandTable v-else :brand-list="brandStore.brandLists" @submitDelete="handleDeleteSubmit" />
  </section>

  <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
  />
</template>

<style scoped></style>
