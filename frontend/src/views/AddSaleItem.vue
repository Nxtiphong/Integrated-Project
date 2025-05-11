<script setup>
import { ref, onMounted } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import ProductImage from '@/components/detail/ProductImage.vue';
import { useSaleItemStore } from '@/stores/saleItemStore';
import Alert from '@/components/share/Alert.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const params = route.params.id;
const saleStore = useSaleItemStore();
const router = useRouter();

const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
  duration: 3000,
});
const product = ref({
  brand: '',
  model: '',
  price: null,
  description: '',
  ramGb: null,
  screenSizeInch: null,
  storageGb: null,
  color: '',
  quantity: null,
});

const validation = (product) => {
  if (!product.brand) {
    alertMessage.value = {
      type: 'error',
      message: 'Please select a brand',
      visible: true,
    };
    return false;
  }
  if (!product.model) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a model name',
      visible: true,
    };
    return false;
  }
  if (!product.price || product.price <= 0) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a valid price',
      visible: true,
    };
    return false;
  }
  if (product.quantity < 0) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a valid quantity, quantity should be greater than 0',
      visible: true,
    };

    return false;
  }
  if (product.ramGb < 0) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a valid RAM size, RAM size should be greater than 0',
      visible: true,
    };
    return false;
  }
  if (product.screenSizeInch < 0) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a valid screen size, screen size should be greater than 0',
      visible: true,
    };
    return false;
  }
  if (product.storageGb < 0) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a valid storage size, storage size should be greater than 0',
      visible: true,
    };
    return false;
  }
  if (!product.color) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a color',
      visible: true,
    };
    return false;
  }
  if (!product.description) {
    alertMessage.value = {
      type: 'error',
      message: 'Please enter a description',
      visible: true,
    };
    return false;
  }
  return true;
};

const saveProduct = async () => {
  console.log('Adding product:', product.value);
  if (!validation(product.value)) {
    return;
  }
  if (params) {
    await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items/${params}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(product.value),
    });
    saleStore.updated = true;
      router.push(`/sale-items/${params}`);
  } else {
    await fetch(`${import.meta.env.VITE_BASE_URL}/itb-mshop/v1/sale-items`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(product.value),
    });
    saleStore.created = true;
    router.push('/sale-items');
  }
};

const cancel = () => {
  product.value = {
    brand: '',
    model: '',
    price: null,
    description: '',
    ramGb: null,
    screenSizeInch: null,
    storageGb: null,
    color: '',
    quantity: null,
  };
  alertMessage.value = {
    type: 'info',
    message: 'Action cancelled',
    visible: true,
    duration: 500,
    countdownVisible: false,
  };
  setTimeout(() => {
    router.push('/sale-items');
  }, 1000);
};

const sortBrands = (brands) => {
  return brands.sort((a, b) => a.name.localeCompare(b.name));
};
const model = ref('');
onMounted(async () => {
  await saleStore.fetchBrands();
  saleStore.brands = sortBrands(saleStore.brands);

  if (params) {
    const item = await saleStore.fetchSaleItemById(params);
    if (!item) {
      router.push('/non-existing-path');
      return;
    }
    model.value = item.model;
    if (item) {
      const matchedBrand = saleStore.brands.find((b) => b.name === item.brandName);
      product.value = {
        brand: matchedBrand || '',
        model: item.model,
        price: item.price,
        description: item.description,
        ramGb: item.ramGb,
        screenSizeInch: item.screenSizeInch,
        storageGb: item.storageGb,
        color: item.color,
        quantity: item.quantity,
      };
    }
  }
});
</script>

<template>
  <div class="bg-gray-50 min-h-screen pb-10">
    <div class="container mx-auto px-4 sm:px-6 lg:px-8">
      <div class="breadcrumbs text-sm my-2 overflow-x-auto whitespace-nowrap">
        <ul class="flex">
          <li class="flex items-center">
            <div class="itbms-home-button">
              <RouterLink to="/">Home</RouterLink>
            </div>
          </li>
          <li>
            <div class="itbms-back-button">
              <RouterLink :to="`/sale-items${params ? `/${params}` : ''}`">
                <b>
                  {{ params ? model : 'New Sale Item' }}
                </b>
              </RouterLink>
            </div>
          </li>
        </ul>
      </div>
    </div>

    <div class="lg:max-w-[1450px] mx-auto px-4 sm:px-6 lg:px-8 mt-6">
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6 border-b border-gray-200">
          <h1 class="text-2xl font-bold text-gray-900">
            {{ params ? 'Edit Product' : 'Add New Product' }}
          </h1>
          <p class="text-gray-500 mt-1">
            {{
              params
                ? 'Edit the product details below'
                : 'Fill in the details to add a new product to your inventory'
            }}
          </p>
        </div>

        <div class="p-4">
          <div class="flex flex-col lg:flex-row gap-8">
            <div class="lg:w-2/4">
              <div class="bg-white rounded-lg border border-gray-200 p-4 h-full">
                <h2 class="font-semibold text-gray-800 mb-4">Product Images</h2>
                <ProductImage :isAddPage="false" />
              </div>
            </div>

            <div class="lg:w-2/4">
              <div class="bg-white rounded-lg border border-gray-200 p-6">
                <h2 class="font-semibold text-gray-800 mb-6">Product Details</h2>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div class="flex flex-col gap-2">
                    <label for="brand" class="text-gray-700 font-medium">Brand</label>
                    <select
                      v-model="product.brand"
                      class="itbms-brand px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    >
                      <option disabled value="">Select a brand</option>
                      <option v-for="brand in saleStore.brands" :key="brand.id" :value="brand">
                        {{ brand.name }}
                      </option>
                    </select>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="model" class="text-gray-700 font-medium">Model</label>
                    <input
                      id="model"
                      type="text"
                      v-model.trim="product.model"
                      placeholder="Enter model name"
                      class="itbms-model px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                      @input="(e) => console.log(e.target.value)"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="price" class="text-gray-700 font-medium">Price (Baht)</label>
                    <input
                      id="price"
                      type="number"
                      v-model.trim="product.price"
                      placeholder="0.00"
                      class="itbms-price px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="quantity" class="text-gray-700 font-medium">Quantity</label>
                    <input
                      id="quantity"
                      type="number"
                      v-model.trim="product.quantity"
                      placeholder="0"
                      class="itbms-quantity px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="ram" class="text-gray-700 font-medium">RAM (GB)</label>
                    <input
                      id="ram"
                      type="number"
                      v-model.trim="product.ramGb"
                      placeholder="Enter RAM size"
                      class="itbms-ramGb px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="screenSize" class="text-gray-700 font-medium"
                      >Screen Size (Inches)</label
                    >
                    <input
                      id="screenSize"
                      type="number"
                      step="0.1"
                      v-model.trim="product.screenSizeInch"
                      placeholder="0.0"
                      class="itbms-screenSizeInch px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="storage" class="text-gray-700 font-medium">Storage (GB)</label>
                    <input
                      id="storage"
                      type="number"
                      v-model.trim="product.storageGb"
                      placeholder="Enter storage size"
                      class="itbms-storageGb px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="color" class="text-gray-700 font-medium">Color</label>
                    <input
                      id="color"
                      type="text"
                      v-model.trim="product.color"
                      placeholder="Enter color"
                      class="itbms-color px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
                    />
                  </div>

                  <div class="flex flex-col gap-2 md:col-span-2">
                    <label for="description" class="text-gray-700 font-medium">Description</label>
                    <textarea
                      id="description"
                      v-model="product.description"
                      rows="4"
                      placeholder="Enter product description"
                      class="itbms-description px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors resize-y"
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-gray-50 px-6 py-4 flex justify-end gap-3 border-t border-gray-200">
          <button
            @click="cancel"
            class="itbms-cancel-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-200 transition-colors"
          >
            <svg
              class="w-4 h-4"
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
            >
              <path
                fill="none"
                stroke="currentColor"
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="1.5"
                d="M19 5L5 19M5 5l14 14"
                color="currentColor"
              />
            </svg>
            Cancel
          </button>
          <button
            @click="saveProduct"
            class="itbms-save-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md text-white transition-colors bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-primary/50 disabled:bg-gray-300 disabled:cursor-not-allowed"
            :disabled="
              !product.brand ||
              !product.model ||
              !product.price ||
              !product.color ||
              !product.description
            "
          >
            <svg
              class="w-4 h-4"
              xmlns="http://www.w3.org/2000/svg"
              width="32"
              height="32"
              viewBox="0 0 32 32"
            >
              <path
                fill="currentColor"
                d="m27.71 9.29l-5-5A1 1 0 0 0 22 4H6a2 2 0 0 0-2 2v20a2 2 0 0 0 2 2h20a2 2 0 0 0 2-2V10a1 1 0 0 0-.29-.71M12 6h8v4h-8Zm8 20h-8v-8h8Zm2 0v-8a2 2 0 0 0-2-2h-8a2 2 0 0 0-2 2v8H6V6h4v4a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V6.41l4 4V26Z"
              />
            </svg>
            Save Product
          </button>
        </div>
        <Alert
          :show="alertMessage.visible"
          :type="alertMessage.type"
          :message="alertMessage.message"
          @update:show="alertMessage.visible = $event"
          :duration="alertMessage.duration"
          :countdownVisible="alertMessage.countdownVisible"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>