<script setup>
import { ref, onMounted, watch, computed } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import ProductImage from '@/components/detail/ProductImage.vue';
import { useSaleItemStore } from '@/stores/useSaleItemStore';
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

const errors = ref({
  brand: '',
  model: '',
  price: '',
  description: '',
  quantity: '',
  ramGb: '',
  screenSizeInch: '',
  storageGb: '',
  color: '',
});

const validateField = (field, value) => {
  switch (field) {
    case 'brand':
  return !value || typeof value !== 'object' || !value.name || value.name.trim() === ''
    ? 'Brand must be selected.' 
    : '';
    case 'model':
      return !value || value.length === 0
        ? 'Model must be 1-60 characters long.'
        : value.length > 60
          ? 'Model must be 1-60 characters long.'
          : '';
    case 'price':
      return value === null || value === undefined || value === '' || value < 0
        ? 'Price must be non-negative integer.'
        : '';
    case 'description':
      return !value || value.length === 0
        ? 'Description must be 1-16,384 characters long.'
        : value.length > 65535
          ? 'Description must be 1-16,384 characters long.'
          : '';
    case 'quantity':
      return value !== null && value !== '' && value < 0
        ? 'Quantity must be non-negative integer.'
        : '';

    case 'ramGb':
      return value !== null && value !== '' && value <= 0
        ? 'RAM size must be positive integer or not specified.'
        : '';
    case 'screenSizeInch':
      if (value === null || value === undefined || value === '') {
        return '';
      }

      const valueStr = String(value);
      const decimalCount = valueStr.split('.')[1];

      return value <= 0 || (decimalCount && decimalCount.length > 2)
        ? 'Screen size must be positive number with at most 2 decimal points or not specified.'
        : '';
    case 'storageGb':
      return value !== null && value !== '' && value <= 0
        ? 'Storage size must be positive integer or not specified.'
        : '';
    case 'color':
      return value && value.length > 40
        ? 'Color must be 1-40 characters long or not specified.'
        : '';
    default:
      return '';
  }
};

const onChange = (field) => {
  errors.value[field] = validateField(field, product.value[field]);
};

const onInput = (field) => {
  if (errors.value[field]) {
    errors.value[field] = '';
  }
};

const hasErrors = computed(() => {
  return (
    errors.value.brand ||
    errors.value.model ||
    errors.value.price ||
    errors.value.description ||
    errors.value.quantity ||
    errors.value.ramGb ||
    errors.value.screenSizeInch ||
    errors.value.storageGb ||
    errors.value.color
  );
});

const validation = (product) => {
  errors.value.brand = validateField('brand', product.brand);
  errors.value.model = validateField('model', product.model);
  errors.value.price = validateField('price', product.price);
  errors.value.description = validateField('description', product.description);
  errors.value.quantity = validateField('quantity', product.quantity);
  errors.value.ramGb = validateField('ramGb', product.ramGb);
  errors.value.screenSizeInch = validateField('screenSizeInch', product.screenSizeInch);
  errors.value.storageGb = validateField('storageGb', product.storageGb);
  errors.value.color = validateField('color', product.color);
  
  return !hasErrors.value;
};

const saveProduct = async () => {
  console.log('Saving product:', product.value);
  if (!validation(product.value)) {
    return;
  }
  if (params) {
    await fetch(`${import.meta.env.VITE_BASE_URL}/v1/sale-items/${params}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(product.value),
    });
    saleStore.updated = true;
    router.push(`/sale-items/${params}`);
  } else {
    await fetch(`${import.meta.env.VITE_BASE_URL}/v1/sale-items`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(product.value),
    });
    saleStore.created = true;
    router.back();
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

  errors.value = {
    brand: '',
    model: '',
    price: '',
    description: '',
    quantity: '',
    ramGb: '',
    screenSizeInch: '',
    storageGb: '',
    color: '',
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

const focusNext = (nextFieldId) => {
  const nextField = document.getElementById(nextFieldId);
  if (nextField) {
    nextField.focus();
  }
};

const originalProduct = ref(null);
const isEdit = ref(false);

const isEqual = (a, b) => {
  return JSON.stringify(a) === JSON.stringify(b);
};

watch(
  product,
  (newVal) => {
    if (originalProduct.value) {
      isEdit.value = !isEqual(newVal, originalProduct.value);
    }
  },
  { deep: true },
);

const isSaveDisabled = computed(() => {
  if (hasErrors.value) {
    return true;
  }

  if (params) {
    return !isEdit.value;
  } else {
    return (
      !product.value.brand ||
      !product.value.model ||
      product.value.price === null ||
      product.value.price === undefined ||
      product.value.price === '' ||
      !product.value.description
    );
  }
});

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
      saleStore.noExist = true;
      router.push('/non-existing-path');
      return;
    }
    model.value = item.model;
    if (item) {
      const matchedBrand = saleStore.brands.find((b) => b.name === item.brandName);
      const loadedProduct = {
        brand: matchedBrand,
        model: item.model,
        price: item.price,
        description: item.description,
        ramGb: item.ramGb,
        screenSizeInch: item.screenSizeInch,
        storageGb: item.storageGb,
        color: item.color,
        quantity: item.quantity,
      };
      product.value = loadedProduct;
      originalProduct.value = JSON.parse(JSON.stringify(loadedProduct));
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
                      id="brand"
                      v-model="product.brand"
                      :class="[
                        'itbms-brand px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.brand ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onInput('brand')"
                      @focusout="onChange('brand')"
                      @keydown.enter="focusNext('model')"
                    >
                      <option value="">Select a brand</option>
                      <option v-for="brand in saleStore.brands" :key="brand.id" :value="brand">
                        {{ brand.name }}
                      </option>
                    </select>
                    <div v-if="errors.brand" class="text-red-500 text-sm itbms-message">{{ errors.brand }}</div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="model" class="text-gray-700 font-medium">Model</label>
                    <input
                      id="model"
                      type="text"
                      v-model.trim="product.model"
                      placeholder="Enter model name"
                      :class="[
                        'itbms-model px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.model ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('model')"
                      @input="onInput('model')"
                      @keydown.enter="focusNext('price')"
                    />
                    <div v-if="errors.model" class="text-red-500 text-sm itbms-message">{{ errors.model }}</div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="price" class="text-gray-700 font-medium">Price (Baht)</label>
                    <input
                      id="price"
                      type="number"
                      v-model.trim="product.price"
                      placeholder="0.00"
                      :class="[
                        'itbms-price px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.price ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('price')"
                      @input="onInput('price')"
                      @keydown.enter="focusNext('quantity')"
                    />
                    <div v-if="errors.price" class="text-red-500 text-sm itbms-message">{{ errors.price }}</div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="quantity" class="text-gray-700 font-medium">Quantity</label>
                    <input
                      id="quantity"
                      type="number"
                      v-model.trim="product.quantity"
                      placeholder="0"
                      :class="[
                        'itbms-quantity px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.quantity ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('quantity')"
                      @input="onInput('quantity')"
                      @keydown.enter="focusNext('ram')"
                    />
                    <div v-if="errors.quantity" class="text-red-500 text-sm itbms-message">
                      {{ errors.quantity }}
                    </div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="ram" class="text-gray-700 font-medium">RAM (GB)</label>
                    <input
                      id="ram"
                      type="number"
                      v-model.trim="product.ramGb"
                      placeholder="Enter RAM size"
                      :class="[
                        'itbms-ramGb px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.ramGb ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('ramGb')"
                      @input="onInput('ramGb')"
                      @keydown.enter="focusNext('screenSize')"
                    />
                    <div v-if="errors.ramGb" class="text-red-500 text-sm itbms-message">{{ errors.ramGb }}</div>
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
                      :class="[
                        'itbms-screenSizeInch px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.screenSizeInch ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('screenSizeInch')"
                      @input="onInput('screenSizeInch')"
                      @keydown.enter="focusNext('storage')"
                    />
                    <div v-if="errors.screenSizeInch" class="text-red-500 text-sm itbms-message">
                      {{ errors.screenSizeInch }}
                    </div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="storage" class="text-gray-700 font-medium">Storage (GB)</label>
                    <input
                      id="storage"
                      type="number"
                      v-model.trim="product.storageGb"
                      placeholder="Enter storage size"
                      :class="[
                        'itbms-storageGb px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.storageGb ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('storageGb')"
                      @input="onInput('storageGb')"
                      @keydown.enter="focusNext('color')"
                    />
                    <div v-if="errors.storageGb" class="text-red-500 text-sm itbms-message">
                      {{ errors.storageGb }}
                    </div>
                  </div>

                  <div class="flex flex-col gap-2">
                    <label for="color" class="text-gray-700 font-medium">Color</label>
                    <input
                      id="color"
                      type="text"
                      v-model.trim="product.color"
                      placeholder="Enter color"
                      :class="[
                        'itbms-color px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.color ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('color')"
                      @input="onInput('color')"
                      @keydown.enter="focusNext('description')"
                    />
                    <div v-if="errors.color" class="text-red-500 text-sm itbms-message">{{ errors.color }}</div>
                  </div>

                  <div class="flex flex-col gap-2 md:col-span-2">
                    <label for="description" class="text-gray-700 font-medium">Description</label>
                    <textarea
                      id="description"
                      v-model.trim="product.description"
                      rows="4"
                      placeholder="Enter product description"
                      :class="[
                        'itbms-description px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors resize-y',
                        errors.description ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('description')"
                      @input="onInput('description')"
                      @keydown.enter.prevent="focusNext('save')"
                    ></textarea>
                    <div v-if="errors.description" class="text-red-500 text-sm itbms-message">
                      {{ errors.description }}
                    </div>
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
            :disabled="isSaveDisabled"
            @keydown.enter="saveProduct"
            id="save"
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

<style scoped></style>
