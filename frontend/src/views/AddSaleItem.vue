<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { RouterLink, useRouter, useRoute } from 'vue-router'
import ProductImage from '@/components/detail/ProductImage.vue'
import Alert from '@/components/share/Alert.vue'
import Input from '@/components/Input.vue'
import { useSaleItemStore } from '@/stores/useSaleItemStore'

const route = useRoute()
const params = route.params.id
const saleStore = useSaleItemStore()
const router = useRouter()

const alertMessage = ref({ type: '', message: '', visible: false, duration: 3000, countdownVisible: false })

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
})

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
})

const saleItemImages = ref([])

const imagesPayload = ref([])
const onImagesChanged = (payload) => {
  imagesPayload.value = payload || []
  if (params) isEditImages.value = true
}

const validateField = (field, value) => {
  switch (field) {
    case 'brand':
      return !value || typeof value !== 'object' || !value.name || value.name.trim() === ''
        ? 'Brand must be selected.' : ''
    case 'model':
      return !value || value.length === 0
        ? 'Model must be 1-60 characters long.'
        : value.length > 60
        ? 'Model must be 1-60 characters long.'
        : ''
    case 'price':
      return value === null || value === undefined || value === '' || value < 0
        ? 'Price must be non-negative integer.' : ''
    case 'description':
      return !value || value.length === 0
        ? 'Description must be 1-16,384 characters long.'
        : value.length > 65535
        ? 'Description must be 1-16,384 characters long.'
        : ''
    case 'quantity':
      return value !== null && value !== '' && value < 0
        ? 'Quantity must be non-negative integer.' : ''
    case 'ramGb':
      return value !== null && value !== '' && value <= 0
        ? 'RAM size must be positive integer or not specified.' : ''
    case 'screenSizeInch': {
      if (value === null || value === undefined || value === '') return ''
      const valueStr = String(value)
      const decimalCount = valueStr.split('.')[1]
      return value <= 0 || (decimalCount && decimalCount.length > 2)
        ? 'Screen size must be positive number with at most 2 decimal points or not specified.' : ''
    }
    case 'storageGb':
      return value !== null && value !== '' && value <= 0
        ? 'Storage size must be positive integer or not specified.' : ''
    case 'color':
      return value && value.length > 40
        ? 'Color must be 1-40 characters long or not specified.' : ''
    default:
      return ''
  }
}

const onChange = (field) => { errors.value[field] = validateField(field, product.value[field]) }
const onInput  = (field) => { if (errors.value[field]) errors.value[field] = '' }
const hasErrors = computed(() => Object.values(errors.value).some((e) => e))

const validation = (p) => {
  Object.keys(errors.value).forEach((field) => {
    errors.value[field] = validateField(field, p[field])
  })
  return !hasErrors.value
}

const originalProduct = ref(null)

const isEditProduct = ref(false)
const isEditImages  = ref(false)

const normalizeProduct = (p) => ({
  brand: typeof p.brand === 'object' ? p.brand?.id : p.brand,
  model: String(p.model ?? ''),
  price: String(p.price ?? ''),
  description: String(p.description ?? ''),
  quantity: String(p.quantity ?? ''),
  ramGb: String(p.ramGb ?? ''),
  screenSizeInch: String(p.screenSizeInch ?? ''),
  storageGb: String(p.storageGb ?? ''),
  color: String(p.color ?? ''),
})
const isEqual = (a, b) =>
  JSON.stringify(normalizeProduct(a, b)) === JSON.stringify(normalizeProduct(b, b))

watch(product, (v) => {
  if (originalProduct.value) isEditProduct.value = !isEqual(v, originalProduct.value)
}, { deep: true })

const isSaveDisabled = computed(() => {
  if (hasErrors.value) return true
  if (params) return !(isEditProduct.value || isEditImages.value)
  return (
    !product.value.brand ||
    !product.value.model ||
    product.value.price === null || product.value.price === undefined || product.value.price === '' ||
    !product.value.description
  )
})

const sortBrands = (brands) => brands.sort((a, b) => a.name.localeCompare(b.name))
const model = ref('')

const apiBase = (
  import.meta.env.VITE_BASE_URL ||
  (typeof window !== 'undefined' ? window.location.origin : '')
).replace(/\/+$/, '')

const saveSaleItem = async () => {
  if (!validation(product.value)) return

  if (!params) {
    // CREATE
    const fd = new FormData()
    fd.append('model', String(product.value.model ?? ''))
    fd.append('description', String(product.value.description ?? ''))
    fd.append('price', String(product.value.price ?? 0))
    fd.append('ramGb', String(product.value.ramGb ?? ''))
    fd.append('screenSizeInch', String(product.value.screenSizeInch ?? ''))
    fd.append('quantity', String(product.value.quantity ?? 0))
    fd.append('storageGb', String(product.value.storageGb ?? ''))
    fd.append('color', String(product.value.color ?? ''))
    if (product.value.brand?.id != null) fd.append('brand.id', String(product.value.brand.id))

    // สร้างใช้รูปแบบเก่า images[] + imageViewOrders[] (ตาม BE สร้าง)
    const ordered = [...imagesPayload.value]
      .filter(x => x.status === 'NEW' && x.imageFile)
      .sort((a, b) => a.order - b.order)
      .map((x, idx) => ({ file: x.imageFile, order: idx })) // 0..3 ต่อเนื่อง

    ordered.forEach(({ file, order }) => {
      fd.append('images', file, file.name)
      fd.append('imageViewOrders', String(order))
    })

    try {
      const res = await fetch(`${apiBase}/v2/sale-items`, { method: 'POST', body: fd })
      if (!res.ok) {
        const msg = await res.text()
        alertMessage.value = { type: 'error', message: `Create failed: ${msg}`, visible: true, duration: 3000 }
        return
      }
      const detail = await res.json()
      router.push(`/sale-items/${detail.id}`)
    } catch {
      alertMessage.value = { type: 'error', message: 'Network error: cannot create sale item', visible: true, duration: 3000 }
    }
  } else {
    // UPDATE + status
    try {
      const fd = new FormData()
      if (product.value.brand?.id != null) fd.append('saleItem.brand.id', String(product.value.brand.id))
      fd.append('saleItem.model', String(product.value.model ?? ''))
      fd.append('saleItem.description', String(product.value.description ?? ''))
      fd.append('saleItem.price', String(product.value.price ?? 0))
      fd.append('saleItem.quantity', String(product.value.quantity ?? 0))
      fd.append('saleItem.ramGb', String(product.value.ramGb ?? ''))
      fd.append('saleItem.screenSizeInch', String(product.value.screenSizeInch ?? ''))
      fd.append('saleItem.storageGb', String(product.value.storageGb ?? ''))
      fd.append('saleItem.color', String(product.value.color ?? ''))

      // imagesPayload จาก ProductImage: [{status, order(1-based), fileName?, imageFile?}]
      const list = [...imagesPayload.value]
        .filter(x => x && (x.status === 'DELETE' || x.imageFile || x.fileName))
        .sort((a, b) => a.order - b.order)

      list.forEach((info, i) => {
        const base = `imageInfos[${i}]`
        fd.append(`${base}.status`, info.status)
        fd.append(`${base}.order`,  String(info.order))
        if (info.fileName)  fd.append(`${base}.fileName`, info.fileName)
        if (info.status === 'NEW' && info.imageFile) {
          fd.append(`${base}.imageFile`, info.imageFile, info.imageFile.name)
        }
      })

      const res = await fetch(`${apiBase}/v2/sale-items/${params}`, { method: 'PUT', body: fd })
      if (!res.ok) {
        const msg = await res.text()
        alertMessage.value = { type: 'error', message: `Update failed: ${msg}`, visible: true, duration: 3000 }
        return
      }

      isEditProduct.value = false
      isEditImages.value = false

      router.push(`/sale-items/${params}`)
    } catch {
      alertMessage.value = { type: 'error', message: 'Network error: cannot update sale item', visible: true, duration: 3000 }
    }
  }
}

const cancel = () => {
  product.value = { brand: '', model: '', price: null, description: '', ramGb: null, screenSizeInch: null, storageGb: null, color: '', quantity: null }
  Object.keys(errors.value).forEach((k) => (errors.value[k] = ''))
  router.push('/sale-items')
}

onMounted(async () => {
  await saleStore.fetchBrands()
  saleStore.brands = sortBrands(saleStore.brands)

  if (params) {
    const item = await saleStore.fetchSaleItemById(params)
    if (!item) {
      router.push('/non-existing-path')
      return
    }
    model.value = item.model
    saleItemImages.value = item.saleItemImages || []

    const matchedBrand = saleStore.brands.find((b) => b.name === item.brandName)
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
    }
    product.value = loadedProduct
    originalProduct.value = JSON.parse(JSON.stringify(loadedProduct))

    isEditProduct.value = false
    isEditImages.value  = false
  }
})
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
                <b>{{ params ? model : 'New Sale Item' }}</b>
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
            {{ params ? 'Edit the product details below' : 'Fill in the details to add a new product to your inventory' }}
          </p>
        </div>

        <div class="p-4">
          <div class="flex flex-col lg:flex-row gap-8">
            <div class="lg:w-2/4">
              <div class="bg-white rounded-lg border border-gray-200 p-4 h-full">
                <h2 class="font-semibold text-gray-800 mb-4">Product Images</h2>
                <ProductImage
                  :isDetailPage="false"
                  :saleItemImages="saleItemImages"
                  @imagesChanged="onImagesChanged"
                />
              </div>
            </div>

            <div class="lg:w-2/4">
              <div class="bg-white rounded-lg border border-gray-200 p-6">
                <h2 class="font-semibold text-gray-800 mb-6">Product Details</h2>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div class="flex flex-col gap-2 pb-2 relative">
                    <label for="brand" class="text-gray-700 font-semibold text-xs sm:text-sm md:text-base">
                      Brand <span class="text-red-500">*</span>
                    </label>
                    <select
                      id="brand"
                      v-model="product.brand"
                      :class="[
                        'itbms-brand text-xs sm:text-sm md:text-base px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors',
                        errors.brand ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onInput('brand')"
                      @focusout="onChange('brand')"
                    >
                      <option value="">Select a brand</option>
                      <option v-for="brand in saleStore.brands" :key="brand.id" :value="brand">
                        {{ brand.name }}
                      </option>
                    </select>
                    <p v-if="errors.brand" class="text-red-500 text-xs itbms-message">{{ errors.brand }}</p>
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="model"
                      v-model="product.model"
                      label="Model"
                      placeholder="Enter model name"
                      :error="!!errors.model"
                      :errorMessage="errors.model"
                      :required="true"
                      nextFieldId="price"
                      customClass="itbms-model"
                      @update:modelValue="onInput('model')"
                      @blur="onChange('model')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="price"
                      v-model="product.price"
                      label="Price (Baht)"
                      type="number"
                      placeholder="0.00"
                      :error="!!errors.price"
                      :errorMessage="errors.price"
                      :required="true"
                      nextFieldId="quantity"
                      customClass="itbms-price"
                      @update:modelValue="onInput('price')"
                      @blur="onChange('price')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="quantity"
                      v-model="product.quantity"
                      label="Quantity"
                      type="number"
                      placeholder="0"
                      :error="!!errors.quantity"
                      :errorMessage="errors.quantity"
                      nextFieldId="ram"
                      customClass="itbms-quantity"
                      @update:modelValue="onInput('quantity')"
                      @blur="onChange('quantity')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="ram"
                      v-model="product.ramGb"
                      label="RAM (GB)"
                      type="number"
                      placeholder="Enter RAM size"
                      :error="!!errors.ramGb"
                      :errorMessage="errors.ramGb"
                      nextFieldId="screenSize"
                      customClass="itbms-ramGb"
                      @update:modelValue="onInput('ramGb')"
                      @blur="onChange('ramGb')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="screenSize"
                      v-model="product.screenSizeInch"
                      label="Screen Size (Inches)"
                      type="number"
                      placeholder="0.0"
                      :error="!!errors.screenSizeInch"
                      :errorMessage="errors.screenSizeInch"
                      nextFieldId="storage"
                      customClass="itbms-screenSizeInch"
                      @update:modelValue="onInput('screenSizeInch')"
                      @blur="onChange('screenSizeInch')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="storage"
                      v-model="product.storageGb"
                      label="Storage (GB)"
                      type="number"
                      placeholder="Enter storage size"
                      :error="!!errors.storageGb"
                      :errorMessage="errors.storageGb"
                      nextFieldId="color"
                      customClass="itbms-storageGb"
                      @update:modelValue="onInput('storageGb')"
                      @blur="onChange('storageGb')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 pb-2 relative">
                    <Input
                      id="color"
                      v-model="product.color"
                      label="Color"
                      placeholder="Enter color"
                      :error="!!errors.color"
                      :errorMessage="errors.color"
                      nextFieldId="description"
                      customClass="itbms-color"
                      @update:modelValue="onInput('color')"
                      @blur="onChange('color')"
                    />
                  </div>

                  <div class="flex flex-col gap-2 md:col-span-2 relative">
                    <label for="description" class="text-xs sm:text-sm md:text-base text-gray-700 font-semibold">
                      Description <span class="text-red-500">*</span>
                    </label>
                    <textarea
                      id="description"
                      v-model.trim="product.description"
                      rows="4"
                      placeholder="Enter product description"
                      :class="[
                        'itbms-description text-xs sm:text-sm md:text-base px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors resize-y',
                        errors.description ? 'border-red-500' : 'border-gray-300',
                      ]"
                      @change="onChange('description')"
                      @input="onInput('description')"
                    />
                    <p v-if="errors.description" class="text-red-500 text-xs itbms-message">{{ errors.description }}</p>
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
            Cancel
          </button>
          <button
            @click="saveSaleItem"
            class="itbms-save-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md text-white transition-colors bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-primary/50 disabled:bg-gray-300 disabled:cursor-not-allowed"
            :disabled="isSaveDisabled"
            id="save"
          >
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
