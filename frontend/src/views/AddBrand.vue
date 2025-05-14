<script setup>
import Alert from '@/components/share/Alert.vue';
import { useBrandFormStore } from '@/stores/useBrandFormStore';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const params = route.params.id;

const brandForm = useBrandFormStore();

const isError = ref(false);
const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
  duration: 3000,
});

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
  brandForm.resetForm();
  router.push('/brand');
};

const handleCreate = async () => {
  if (!validateForm()) return;
  const result = await brandForm.submitCreateForm();
  if (result.success) {
    alertMessage.value = {
      type: 'success',
      message: 'The brand has been added',
      visible: true,
    };
    setTimeout(() => {
      router.push('/brand');
    }, 3000);
  } else {
    alertMessage.value = {
      type: 'error',
      message: 'The brand could not be added',
      visible: true,
    };
  }
};

const handleEdit = async () => {
  if (!validateForm()) return;
  const result = await brandForm.submitUpdateForm(params);
  if (result.success) {
    alertMessage.value = {
      type: 'success',
      message: 'The brand has been updated',
      visible: true,
    };
    setTimeout(() => {
      router.push('/brand');
    }, 3000);
  } else {
    alertMessage.value = {
      type: 'error',
      message: 'The brand does not exist',
      visible: true,
    };
  }
};

const handleSubmit = () => {
  if (params) {
    handleEdit();
  } else {
    handleCreate();
  }
};

onMounted(() => {
  if (params) {
    brandForm.fetchBrandDetail(params);
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
            params ? brandForm.name : 'New Brand'
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
              @keydown.enter.prevent="focusNext('websiteURL')"
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
              id="websiteURL"
              type="text"
              placeholder="https://example.com"
              v-model.trim="brandForm.websiteURL"
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
            <button
              id="save"
              type="submit"
              :disabled="!brandForm.name.trim()"
              class="btn btn-primary"
            >
              Save
            </button>
            <button type="button" @click="handleCancel" class="btn bg-white">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <Alert
      :show="alertMessage.visible"
      :type="alertMessage.type"
      :message="alertMessage.message"
      :duration="alertMessage.duration"
    />
  </div>
</template>

<style scoped></style>
