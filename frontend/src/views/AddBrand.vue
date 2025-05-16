<script setup>
import { useBrandFormStore } from '@/stores/useBrandFormStore';
import { useBrandStore } from '@/stores/useBrandStore';
import { fetchBrandDetail, submitCreateForm, submitUpdateForm } from '@/utils/brandUtils';
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const params = route.params.id;

const { brandForm, resetForm } = useBrandFormStore();
const brandStore = useBrandStore();

const brandName = ref('');
const originalForm = ref({});

const isSubmitting = ref(false);

const isDisabled = computed(() => {
  const nameInvalid = brandForm.name.trim() === '' || isSubmitting.value;

  if (params) {
    const noChanges =
      JSON.stringify({
        name: brandForm.name,
        websiteUrl: brandForm.websiteUrl,
        isActive: brandForm.isActive,
        countryOfOrigin: brandForm.countryOfOrigin,
      }) === JSON.stringify(originalForm.value);

    return nameInvalid || noChanges;
  }

  return nameInvalid;
});

const isError = ref(false);

const focusNext = (refName) => {
  const refInputField = document.getElementById(refName);
  refInputField.focus();
};

const validateForm = () => {
  if (!brandForm.name.trim()) {
    isError.value = true;
    return false;
  }
  isError.value = false;
  return true;
};

const handleCancel = () => {
  resetForm();
  router.push('/sale-items/brand');
};

const handleCreate = async () => {
  if (!validateForm()) return;
  isSubmitting.value = true;
  const result = await submitCreateForm({
    name: brandForm.name,
    websiteUrl: brandForm.websiteUrl || null,
    isActive: brandForm.isActive,
    countryOfOrigin: brandForm.countryOfOrigin || null,
  });

  if (result.success) {
    brandStore.isCreateSuccess = true;
    console.log(brandStore.isCreateSuccess);
    router.push('/sale-items/brand');
  } else {
    brandStore.isCreateFailed = true;
  }

  if (!result.success) {
    setTimeout(() => {
      isSubmitting.value = false;
    }, 3000);
  }
};

const handleEdit = async () => {
  if (!validateForm()) return;
  isSubmitting.value = true;
  const result = await submitUpdateForm(params, {
    name: brandForm.name,
    websiteUrl: brandForm.websiteUrl || null,
    isActive: brandForm.isActive,
    countryOfOrigin: brandForm.countryOfOrigin || null,
  });

  if (result.success) {
    router.push('/sale-items/brand');
    brandStore.isUpdatedSuccess = true;
  } else {
    brandStore.isUpdatedFailed = true;
  }

  if (!result.success) {
    setTimeout(() => {
      isSubmitting.value = false;
    }, 3000);
  }
};

const handleSubmit = () => {
  if (params) {
    handleEdit();
    resetForm();
  } else {
    handleCreate();
    resetForm();
  }
};

onMounted(async () => {
  if (params) {
    const result = await fetchBrandDetail(params);
    if (result.success) {
      const data = result.data;
      brandForm.name = data.name;
      brandForm.websiteUrl = data.websiteUrl;
      brandForm.isActive = data.isActive;
      brandForm.countryOfOrigin = data.countryOfOrigin;
      brandName.value = data.name;

      originalForm.value = {
        name: data.name,
        websiteUrl: data.websiteUrl,
        isActive: data.isActive,
        countryOfOrigin: data.countryOfOrigin,
      };
    } else {
      router.push('/non-existing-path');
      return;
    }
  } else {
    resetForm();
  }
});
</script>

<template>
  <div class="container mx-auto">
    <div
      class="breadcrumbs flex items-center justify-between text-sm mt-4 overflow-x-auto whitespace-nowrap"
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
        <li>
          <RouterLink :to="params ? `/sale-items/brand/${params}/edit` : '/sale-items/brand/add'">{{
            params ? brandName : 'New Brand'
          }}</RouterLink>
        </li>
      </ul>
    </div>

    <div class="lg:max-w-[1450px] mx-auto mt-1">
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="p-6 border-b border-gray-200">
          <h1 class="text-2xl font-bold text-gray-900">
            {{ params ? 'Edit Brand' : 'Create New Brand' }}
          </h1>
          <p class="text-gray-500 mt-1">
            {{
              params
                ? 'Edit the brand details below'
                : 'Fill in the details to add a new brand to your inventory'
            }}
          </p>
        </div>

        <form @submit.prevent="handleSubmit" class="flex flex-col space-y-5 p-6">
          <div class="flex flex-col space-y-2 relative">
            <label class="font-semibold">Brand Name <span class="text-error">*</span></label>
            <input
              id="name"
              type="text"
              placeholder="Name"
              v-model.trim="brandForm.name"
              @keydown.enter.prevent="focusNext('websiteUrl')"
              :class="[
                'px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors',
                isError
                  ? 'border-red-500 focus:ring-red-200 focus:border-red-500'
                  : 'border-gray-300 focus:ring-primary/20 focus:border-primary',
              ]"
              maxlength="30"
            />
            <p v-if="isError" class="text-error text-xs absolute z-10 top-2 right-2">
              Name is required
            </p>
          </div>

          <div class="flex flex-col space-y-2">
            <label class="font-semibold">Website URL</label>
            <input
              id="websiteUrl"
              type="text"
              placeholder="https://example.com"
              v-model.trim="brandForm.websiteUrl"
              @keydown.enter.prevent="focusNext('country')"
              class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
              maxlength="40"
            />
          </div>

          <div class="flex flex-col space-y-2">
            <label class="font-semibold">Country of Origin</label>
            <input
              id="country"
              type="text"
              placeholder="Country of Origin"
              v-model.trim="brandForm.countryOfOrigin"
              @keydown.enter.prevent="focusNext('isActive')"
              class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary transition-colors"
              maxlength="80"
            />
          </div>

          <div class="flex space-x-2 justify-start items-center">
            <input
              @keydown.enter.prevent="focusNext('save')"
              id="isActive"
              type="checkbox"
              v-model="brandForm.isActive"
              class="checkbox checkbox-primary"
            />
            <label class="label">Is Active</label>
          </div>

          <div class="flex flex-col space-y-3">
            <button id="save" type="submit" :disabled="isDisabled" class="btn btn-primary">
              {{ isSubmitting ? 'Saving...' : 'Save' }}
            </button>
            <button type="button" @click="handleCancel" class="btn bg-white">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
