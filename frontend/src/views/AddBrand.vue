<script setup>
import Input from '@/components/Input.vue';
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

const isFocused = ref({
  name: false,
  websiteUrl: false,
  countryOfOrigin: false,
});

const isValidUrl = (value) => {
  if (!value?.trim()) return true;
  try {
    const url = new URL(value);
    return url.protocol === 'http:' || url.protocol === 'https:';
  } catch {
    return false;
  }
};

const isError = computed(() => ({
  name: isFocused.value.name && (!brandForm.name?.trim() || brandForm.name.trim().length > 30),
  websiteUrl: isFocused.value.websiteUrl && !isValidUrl(brandForm.websiteUrl),
  countryOfOrigin: isFocused.value.countryOfOrigin && brandForm.countryOfOrigin.trim().length > 80,
}));

const isDisabled = computed(() => {
  const nameInvalid = !brandForm.name?.trim() || isSubmitting.value;
  const hasErrors = Object.values(isError.value).some(Boolean);

  if (params) {
    const currentForm = {
      name: brandForm.name === '' ? null : brandForm.name,
      websiteUrl: brandForm.websiteUrl === '' ? null : brandForm.websiteUrl,
      isActive: brandForm.isActive,
      countryOfOrigin: brandForm.countryOfOrigin === '' ? null : brandForm.countryOfOrigin,
    };
    const noChanges = JSON.stringify(currentForm) === JSON.stringify(originalForm.value);
    return nameInvalid || hasErrors || noChanges;
  }

  return nameInvalid || hasErrors;
});

const markFocused = (field) => {
  isFocused.value[field] = true;
};

const focusNext = (refName) => {
  document.getElementById(refName)?.focus();
};

const handleCancel = () => {
  resetForm();
  router.push('/brands');
};

const handleSubmit = async () => {
  isSubmitting.value = true;

  const formData = {
    name: brandForm.name,
    websiteUrl: brandForm.websiteUrl || null,
    isActive: brandForm.isActive,
    countryOfOrigin: brandForm.countryOfOrigin || null,
  };

  const result = params
    ? await submitUpdateForm(params, formData)
    : await submitCreateForm(formData);

  if (result.success) {
    if (params) {
      brandStore.isUpdatedSuccess = true;
    } else {
      brandStore.isCreateSuccess = true;
    }
    resetForm();
    router.push('/brands');
  } else {
    if (params) {
      brandStore.isUpdatedFailed = true;
    } else {
      brandStore.isCreateFailed = true;
    }
    setTimeout(() => {
      isSubmitting.value = false;
    }, 3000);
  }
};

onMounted(async () => {
  resetForm();

  if (params) {
    const result = await fetchBrandDetail(params);
    if (result.success) {
      const { name, websiteUrl, isActive, countryOfOrigin } = result.data;

      Object.assign(brandForm, { name, websiteUrl, isActive, countryOfOrigin });
      brandName.value = name;
      originalForm.value = { name, websiteUrl, isActive, countryOfOrigin };
    } else {
      router.push('/brands/non-existing-path');
    }
  }
});
</script>

<template>
  <section>
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
          <RouterLink :to="params ? `/brands/${params}/edit` : '/brands/add'">
            {{ params ? brandName : 'New Brand' }}
          </RouterLink>
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
            <Input
              id="name"
              v-model="brandForm.name"
              label="Brand Name"
              placeholder="Name"
              :required="true"
              :error="isError.name"
              :errorMessage="'Brand name must be 1-30 characters long.'"
              :focused="isFocused.name"
              :nextFieldId="'websiteUrl'"
              @blur="markFocused('name')"
              @keydownEnter="focusNext"
              :custom-class="'itbms-name'"
            />
          </div>

          <div class="flex flex-col space-y-2 relative">
            <Input
              id="websiteUrl"
              v-model="brandForm.websiteUrl"
              label="Website URL"
              placeholder="https://example.com"
              :error="isError.websiteUrl"
              :errorMessage="'Brand URL must be a valid URL or not specified.'"
              :focused="isFocused.websiteUrl"
              :nextFieldId="'country'"
              @blur="markFocused('websiteUrl')"
              @keydownEnter="focusNext"
              :custom-class="'itbms-websiteUrl'"
            />
          </div>

          <div class="flex flex-col space-y-2 relative">
            <Input
              id="country"
              v-model="brandForm.countryOfOrigin"
              label="Country of Origin"
              placeholder="Country of Origin"
              :error="isError.countryOfOrigin"
              :errorMessage="'Brand country of origin must be 1-80 characters long or not specified.'"
              :focused="isFocused.countryOfOrigin"
              :nextFieldId="'isActive'"
              @blur="markFocused('countryOfOrigin')"
              @keydownEnter="focusNext"
              :custom-class="'itbms-countryOfOrigin'"
            />
          </div>

          <div class="flex space-x-2 justify-start items-center">
            <input
              id="isActive"
              type="checkbox"
              v-model="brandForm.isActive"
              class="itbms-isActive checkbox checkbox-primary"
              @keydown.enter.prevent="focusNext('save')"
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
  </section>
</template>

<style scoped></style>
