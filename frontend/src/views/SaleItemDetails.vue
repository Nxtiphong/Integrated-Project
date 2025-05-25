<script setup>
import ProductImage from '@/components/detail/ProductImage.vue';
import ProductSpec from '@/components/detail/ProductSpec.vue';
import { useRoute, useRouter } from 'vue-router';
import notFoundImg from '@/assets/images/404.png';
import { onMounted, ref } from 'vue';
import DeleteModal from '@/components/share/DeleteModal.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import Alert from '@/components/share/Alert.vue';
import { useGalleryFilterStore } from '@/stores/useGalleryFilterStore';

const saleGalleryFilter = useGalleryFilterStore();

const saleStore = useSaleItemStore();
const router = useRouter();
const route = useRoute();
const params = route.params.id;

const productDetail = ref(null);
const isLoading = ref(false);

const fetchProductDetail = async (id) => {
  isLoading.value = true;
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${id}`);
    if (!res.ok) throw new Error('Failed to fetch product id:' + id);
    productDetail.value = await res.json();
  } catch (error) {
    productDetail.value = null;
    console.error('Error:', error);
  } finally {
    isLoading.value = false;
  }
};

const showModal = () => {
  showDeleteModal.value = true;
};

const cancelModal = () => {
  showDeleteModal.value = false;
};

const handleEdit = () => {
  router.push(`/sale-items/${params}/edit`);
};

const showDeleteModal = ref(false);

const handleDelete = async () => {
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${params}`, {
      method: 'DELETE',
    });

    if (res.status === 404) {
      alertMessage.value = {
        type: 'error',
        message: 'The requested sale item does not exist.',
        visible: true,
        duration: 3000,
      };
      setTimeout(() => {
        router.back();
      }, 3000);
    } else if (!res.ok) {
      throw new Error('Failed to delete item');
    } else {
      saleStore.deleted = true;
      saleGalleryFilter.resetPageOnly();

      router.push('/sale-items');
    }
  } catch (error) {
    alertMessage.value = {
      type: 'error',
      message: 'Something went wrong while deleting the sale item.',
      visible: true,
      duration: 3000,
    };
    console.error('Delete Error:', error);
  } finally {
    showDeleteModal.value = false;
  }
};

const alertMessage = ref({
  type: '',
  message: '',
  visible: false,
  duration: 3000,
});

onMounted(() => {
  fetchProductDetail(params);

  if (saleStore.updated) {
    alertMessage.value = {
      type: 'success',
      message: 'The sale item has been updated.',
      visible: true,
      duration: 3000,
    };
    saleStore.updated = false;
  }
});
</script>

<template>
  <div v-if="isLoading" class="flex flex-col justify-center items-center min-h-screen">
    <span class="loading loading-ball loading-xl"></span>
    <p class="text-lg font-semibold">Loading product details...</p>
  </div>

  <div v-else-if="productDetail" class="container">
    <div class="breadcrumbs text-sm my-2">
      <ul>
        <li><RouterLink to="/">Home</RouterLink></li>
        <li class="itbms-home-button"><RouterLink to="/sale-items">Gallery</RouterLink></li>
        <li class="font-bold">{{ productDetail.model }}</li>
      </ul>
    </div>
    <div
      class="py-6 lg:py-10 self-stretch flex flex-col lg:flex-row items-center lg:justify-start gap-4 md:gap-6 lg:gap-12 itbms-row"
    >
      <ProductImage />
      <div class="self-stretch flex-1 flex flex-col justify-start items-start gap-2 lg:gap-4 mt-8">
        <h1 class="text-2xl md:text-3xl lg:text-4xl font-bold leading-10 itbms-model">
          {{ productDetail.model }}
        </h1>
        <h3 class="text-lg lg:text-xl font-bold leading-10 itbms-brand">
          {{ productDetail.brandName }}
        </h3>
        <div class="flex gap-1 items-end">
          <h2 class="text-xl md:text-2xl lg:text-3xl leading-8 tracking-wide itbms-price">
            {{ Number(productDetail.price).toLocaleString() }}
          </h2>
          <span class="text-lg lg:text-xl itbms-price-unit">Baht</span>
        </div>

        <p class="flex gap-1">
          <span class="text-lg lg:text-xl itbms-quantity">
            {{ productDetail.quantity }}
          </span>
          <span class="text-lg itbms-quantity-unit">units</span>
        </p>
        <ProductSpec :product="productDetail" />
        <p class="text-sm md:text-lg lg:text-xl text-secondary-accent itbms-description">
          {{ productDetail.description }}
        </p>
        <div class="mt-4">
          <div class="flex flex-col md:flex-row gap-2">
            <div class="itbms-edit-button">
              <button className="btn btn-outline btn-warning" @click="handleEdit">Edit</button>
            </div>
            <div class="itbms-delete-button">
              <button className="btn btn-outline btn-error" @click="showModal">Delete</button>
            </div>
          </div>
        </div>
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

    <div class="itbms-message">
      <DeleteModal
        v-model="showDeleteModal"
        :title="`Delete ${productDetail.model}`"
        :message="`Do you want to delete this sale item?`"
        @confirm="handleDelete"
        @cancel="cancelModal"
      />
    </div>
  </div>

  <div
    v-else
    class="flex flex-col items-center justify-center m-10 p-5 shadow-lg border rounded-2xl bg-white border-neutral-200 gap-2"
  >
    <img :src="notFoundImg" alt="not_found" />
    <h1 class="text-3xl font-bold uppercase">Something is wrong</h1>
    <p class="itbms-message">The requested sale item does not exist.</p>
    <button @click="router.push('/sale-items')" class="itbms-button btn btn-primary my-2">
      Back
    </button>
  </div>
</template>

<style scoped></style>
