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
  const hasErrors = Object.values(isError.value).some(Boolean);

  if (params) {
    const noChanges =
      JSON.stringify({
        name: brandForm.name,
        websiteUrl: brandForm.websiteUrl,
        isActive: brandForm.isActive,
        countryOfOrigin: brandForm.countryOfOrigin,
      }) === JSON.stringify(originalForm.value);

    return hasErrors || noChanges;
  }

  return hasErrors;
});

const isFocused = ref({
  name: false,
  websiteUrl: false,
  countryOfOrigin: false,
});

const isError = computed(() => ({
  name: isFocused.value.name && (!brandForm.name.trim() || brandForm.name.trim().length > 30),
  websiteUrl: isFocused.value.websiteUrl && !isValidUrl(brandForm.websiteUrl),
  countryOfOrigin:
    isFocused.value.countryOfOrigin &&
    (!brandForm.countryOfOrigin.trim() || brandForm.countryOfOrigin.trim().length > 80),
}));

const isValidUrl = (value) => {
  if (!value.trim()) return true;
  try {
    const url = new URL(value);
    return url.protocol === 'http:' || url.protocol === 'https:';
  } catch {
    return false;
  }
};

const markFocused = (field) => {
  isFocused.value[field] = true;
};

const focusNext = (refName) => {
  const refInputField = document.getElementById(refName);
  refInputField.focus();
};

const handleCancel = () => {
  resetForm();
  router.push('/brands');
};

const handleCreate = async () => {
  isSubmitting.value = true;
  const result = await submitCreateForm({
    name: brandForm.name,
    websiteUrl: brandForm.websiteUrl || null,
    isActive: brandForm.isActive,
    countryOfOrigin: brandForm.countryOfOrigin || null,
  });

  if (result.success) {
    brandStore.isCreateSuccess = true;
    router.push('/brands');
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
    router.push('/brands');
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
  resetForm();

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
      router.push('/brands/non-existing-path');
      return;
    }
  }
});
</script>

<template>
  <div class="">
    <div
      class="breadcrumbs container mx-auto flex items-center justify-between text-xs lg:text-sm mt-4 overflow-x-auto whitespace-nowrap"
    >
      <ul class="flex">
        <li class="flex items-center">
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li class="itbms-item-list">
          <RouterLink to="/sale-items/list">Sale Item List</RouterLink>
        </li>
        <li class="itbms-manage-brand">
          <RouterLink to="/brands">Brand List</RouterLink>
        </li>
        <li>
          <RouterLink :to="params ? `/brands/${params}/edit` : '/brands/add'">{{
            params ? brandName : 'New Brand'
          }}</RouterLink>
        </li>
      </ul>
    </div>

    <div class="text-xs px-4 md:text-base lg:max-w-[1450px] mx-auto mt-2 lg:mt-1">
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="py-2 px-6 lg:px-6 lg:py-6 border-b border-gray-200">
          <h1 class="text-base md:text-2xl font-bold text-gray-900">
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
              @blur="markFocused('name')"
              @keydown.enter.prevent="focusNext('websiteUrl')"
              :class="[
                'itbms-name px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors',
                isError.name
                  ? 'border-red-500 focus:ring-red-200 focus:border-red-500'
                  : 'border-gray-300 focus:ring-primary/20 focus:border-primary',
              ]"
            />
            <p v-if="isError.name" class="text-error text-xs absolute z-10 -bottom-3 right-2">
              Brand name must be 1-30 characters long.
            </p>
          </div>

          <div class="flex flex-col space-y-2 relative">
            <label class="font-semibold">Website URL</label>
            <input
              id="websiteUrl"
              type="text"
              placeholder="https://example.com"
              v-model.trim="brandForm.websiteUrl"
              @blur="markFocused('websiteUrl')"
              @keydown.enter.prevent="focusNext('country')"
              :class="[
                'itbms-websiteUrl px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors',
                isError.websiteUrl
                  ? 'border-red-500 focus:ring-red-200 focus:border-red-500'
                  : 'border-gray-300 focus:ring-primary/20 focus:border-primary',
              ]"
            />
            <p v-if="isError.websiteUrl" class="text-error text-xs absolute z-10 -bottom-3 right-2">
              Brand URL must be a valid URL or not specified.
            </p>
          </div>

          <div class="flex flex-col space-y-2 relative">
            <label class="font-semibold">Country of Origin</label>
            <input
              id="country"
              type="text"
              placeholder="Country of Origin"
              v-model.trim="brandForm.countryOfOrigin"
              @blur="markFocused('countryOfOrigin')"
              @keydown.enter.prevent="focusNext('isActive')"
              :class="[
                'itbms-countryOfOrigin px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors',
                isError.countryOfOrigin
                  ? 'border-red-500 focus:ring-red-200 focus:border-red-500'
                  : 'border-gray-300 focus:ring-primary/20 focus:border-primary',
              ]"
            />
            <p
              v-if="isError.countryOfOrigin"
              class="text-error text-xs absolute z-10 -bottom-3 right-2"
            >
              Brand country of origin must be 1-80 characters long or not specified.
            </p>
          </div>

          <div class="flex space-x-2 justify-start items-center">
            <input
              @keydown.enter.prevent="focusNext('save')"
              id="isActive"
              type="checkbox"
              v-model="brandForm.isActive"
              class="itbms-isActive checkbox checkbox-primary"
            />
            <label class="label">Is Active</label>
          </div>

          <div class="flex flex-col space-y-3">
            <button
              id="save"
              type="submit"
              :disabled="isDisabled"
              class="itbms-save-button btn btn-primary btn-sm md:btn-md"
            >
              {{ isSubmitting ? 'Saving...' : 'Save' }}
            </button>
            <button
              type="button"
              @click="handleCancel"
              class="itbms-cancel-button btn bg-white btn-sm md:btn-md"
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
