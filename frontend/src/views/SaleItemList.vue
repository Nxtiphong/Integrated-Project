<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import DeleteModal from '@/components/share/DeleteModal.vue';
import { deleteSaleItem } from '@/utils/saleitemUtils';
import Alert from '@/components/share/Alert.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';

const router = useRouter();
const saleStore = useSaleItemStore();
const products = ref([]);
const showDeleteModal = ref(false);
const isLoading = ref(true);
const selectedProduct = ref(null);
const alertMessage = ref({
  type: 'success',
  message: '',
  visible: false,
  duration: 3000,
});

const fetchSaleItems = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`);
    if (!response.ok) throw new Error('Failed to fetch sale items');
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching sale items:', error);
    return [];
  } finally {
    isLoading.value = false;
  }
};

const handleDeleteSubmit = async () => {
  const Id = selectedProduct.value?.id;
  if (!Id) {
    alertMessage.value = {
      type: 'error',
      message: 'Sale Item does not exist',
      visible: true,
    };
    return;
  }

  const res = await deleteSaleItem(Id);

  if (res.success) {
    products.value = products.value.filter((item) => item.id !== Id);
    alertMessage.value = {
      type: 'success',
      message: 'The sale item has been deleted.',
      visible: true,
    };
  } else {
    alertMessage.value = {
      type: 'error',
      message: 'Failed to delete Sale Item',
      visible: true,
    };
  }

  showDeleteModal.value = false;
  selectedProduct.value = null;
};

const handleEdit = (id) => {
  router.push(`/sale-items/${id}/edit`);
};

const showUiDelete = (product) => {
  selectedProduct.value = product;
  showDeleteModal.value = true;
};

const cancelModal = () => {
  showDeleteModal.value = false;
};

const addSaleItem = () => {
  router.push(`/sale-items/add`);
};

const manageBrand = () => {
  router.push(`/brands`);
};

const handleDelete = async (id) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${id}`, {
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
      router.push('/sale-items/list');
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

onMounted(async () => {
  products.value = await fetchSaleItems();
  if (saleStore.created) {
    alertMessage.value = {
      type: 'success',
      message: 'The sale item has been successfully added.',
      visible: true,
      duration: 3000,
    };
    saleStore.created = false;
  }
});
</script>

<template>
  <div class="bg-white shadow-lg rounded-lg overflow-hidden border border-gray-200">
    <div
      class="px-6 py-4 flex flex-wrap justify-between items-center bg-gray-50 border-b border-gray-200"
    >
      <div class="flex space-x-3">
        <button
          @click="addSaleItem"
          class="itbms-sale-item-add cursor-pointer bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md flex items-center transition-colors shadow-sm"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
            class="mr-1.5"
          >
            <path
              fill="none"
              stroke="currentColor"
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M5 12h7m7 0h-7m0 0V5m0 7v7"
            />
          </svg>
          Add Sale Item
        </button>

        <button
          @click="manageBrand"
          class="itbms-manage-brand border cursor-pointer border-gray-300 bg-white hover:bg-gray-50 text-gray-700 px-4 py-2 rounded-md flex items-center transition-colors shadow-sm"
        >
          <svg
            class="mr-1.5"
            xmlns="http://www.w3.org/2000/svg"
            width="24"
            height="24"
            viewBox="0 0 24 24"
          >
            <path
              fill="currentColor"
              d="m9.25 22l-.4-3.2q-.325-.125-.612-.3t-.563-.375L4.7 19.375l-2.75-4.75l2.575-1.95Q4.5 12.5 4.5 12.338v-.675q0-.163.025-.338L1.95 9.375l2.75-4.75l2.975 1.25q.275-.2.575-.375t.6-.3l.4-3.2h5.5l.4 3.2q.325.125.613.3t.562.375l2.975-1.25l2.75 4.75l-2.575 1.95q.025.175.025.338v.674q0 .163-.05.338l2.575 1.95l-2.75 4.75l-2.95-1.25q-.275.2-.575.375t-.6.3l-.4 3.2zM11 20h1.975l.35-2.65q.775-.2 1.438-.587t1.212-.938l2.475 1.025l.975-1.7l-2.15-1.625q.125-.35.175-.737T17.5 12t-.05-.787t-.175-.738l2.15-1.625l-.975-1.7l-2.475 1.05q-.55-.575-1.212-.962t-1.438-.588L13 4h-1.975l-.35 2.65q-.775.2-1.437.588t-1.213.937L5.55 7.15l-.975 1.7l2.15 1.6q-.125.375-.175.75t-.05.8q0 .4.05.775t.175.75l-2.15 1.625l.975 1.7l2.475-1.05q.55.575 1.213.963t1.437.587zm1.05-4.5q1.45 0 2.475-1.025T15.55 12t-1.025-2.475T12.05 8.5q-1.475 0-2.488 1.025T8.55 12t1.013 2.475T12.05 15.5M12 12"
            />
          </svg>
          Manage Brand
        </button>
      </div>

      <div class="mt-3 md:mt-0 text-sm text-gray-500">Showing {{ products.length }} products</div>
    </div>
    <div v-if="isLoading" class="text-center py-10 text-gray-500">Loading...</div>

    <div v-else-if="products.length > 0" class="overflow-x-auto">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Id
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Brand
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Model
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Ram
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Storage
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Color
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Price
            </th>
            <th
              scope="col"
              class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Action
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr
            v-for="product in products"
            :key="product.id"
            class="itbms-row hover:bg-gray-50 transition-colors"
          >
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900 itbms-id">#{{ product.id }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900 itbms-brand">
                {{ product.brandName }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900 font-medium itbms-model">{{ product.model }}</div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900 itbms-ramGb">
                {{ product.ramGb != null ? `${product.ramGb} GB` : '-' }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm text-gray-900 itbms-storageGb">
                {{ product.storageGb != null ? `${product.storageGb} GB` : '-' }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="flex items-center itbms-color">
                <span class="text-sm text-gray-900">{{
                  product.color != null ? `${product.color}` : '-'
                }}</span>
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <div class="text-sm font-medium text-gray-900 itbms-price">
                ฿{{
                  product.price.toLocaleString() != null ? `${product.price.toLocaleString()}` : '-'
                }}
              </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
              <div class="flex space-x-2">
                <button
                  @click="handleEdit(product.id)"
                  class="itbms-edit-button cursor-pointer text-blue-600 hover:text-blue-900 transition-colors"
                  title="Edit"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                  >
                    <g
                      fill="none"
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                    >
                      <path stroke-dasharray="20" stroke-dashoffset="20" d="M3 21h18">
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          dur="0.2s"
                          values="20;0"
                        />
                      </path>
                      <path
                        stroke-dasharray="48"
                        stroke-dashoffset="48"
                        d="M7 17v-4l10 -10l4 4l-10 10h-4"
                      >
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          begin="0.2s"
                          dur="0.6s"
                          values="48;0"
                        />
                      </path>
                      <path stroke-dasharray="8" stroke-dashoffset="8" d="M14 6l4 4">
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          begin="0.8s"
                          dur="0.2s"
                          values="8;0"
                        />
                      </path>
                    </g>
                    <path fill="currentColor" fill-opacity="0" d="M14 6l4 4L21 7L17 3Z">
                      <animate
                        fill="freeze"
                        attributeName="fill-opacity"
                        begin="1.1s"
                        dur="0.15s"
                        values="0;0.3"
                      />
                    </path>
                  </svg>
                </button>
                <button
                  @click="showUiDelete(product)"
                  class="itbms-delete-button cursor-pointer text-red-600 hover:text-red-900 transition-colors"
                  title="Delete"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                  >
                    <g
                      fill="none"
                      stroke="currentColor"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                    >
                      <path
                        stroke-dasharray="24"
                        stroke-dashoffset="24"
                        d="M12 20h5c0.5 0 1 -0.5 1 -1v-14M12 20h-5c-0.5 0 -1 -0.5 -1 -1v-14"
                      >
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          dur="0.4s"
                          values="24;0"
                        />
                      </path>
                      <path stroke-dasharray="20" stroke-dashoffset="20" d="M4 5h16">
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          begin="0.4s"
                          dur="0.2s"
                          values="20;0"
                        />
                      </path>
                      <path stroke-dasharray="8" stroke-dashoffset="8" d="M10 4h4M10 9v7M14 9v7">
                        <animate
                          fill="freeze"
                          attributeName="stroke-dashoffset"
                          begin="0.6s"
                          dur="0.2s"
                          values="8;0"
                        />
                      </path>
                    </g>
                  </svg>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="text-center py-10 text-gray-500">No sale item found.</div>
  </div>
  <div class="itbms-message">
    <DeleteModal
      v-model="showDeleteModal"
      :title="`Delete ${selectedProduct?.model || ''}`"
      :message="`Do you want to delete this sale item?`"
      @confirm="handleDeleteSubmit"
      @cancel="cancelModal"
    />
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

<style scoped>
</style>