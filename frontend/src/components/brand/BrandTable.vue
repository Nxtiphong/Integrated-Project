<script setup>
import { ref } from 'vue';
import DeleteModal from '../share/DeleteModal.vue';
import { deleteBrand } from '@/utils/brandUtils';
import Alert from '../share/Alert.vue';

const props = defineProps({
  brandList: Array,
});

const isDelete = ref(false);
const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
});

const handleDelete = async (id) => {
  const res = await deleteBrand(id);

  if (res.success) {
    alertMessage.value = {
      type: 'success',
      message: 'Brand has been deleted',
      visible: true,
    };
  } else {
    alertMessage.value = {
      type: 'success',
      message: 'Brand has been deleted',
      visible: true,
    };
  }
};

const showDeleteModal = () => {
  isDelete.value = true;
};

const cancelModal = () => {
  isDelete.value = false;
};
</script>

<template>
  <div>
    <div
      v-if="brandList.length > 0"
      class="overflow-x-auto mx-8 rounded-box border border-base-content/10 bg-base-100"
    >
      <table class="table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Website URL</th>
            <th>isActive</th>
            <th>Country of Origin</th>
            <th>Created On</th>
            <th>Updated On</th>
            <th class="text-center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="brand in brandList" class="hover:bg-gray-200/50">
            <td>{{ brand.name }}</td>
            <td>{{ brand.websiteUrl }}</td>
            <td>{{ brand.isActive }}</td>
            <td>{{ brand.countryOfOrigin }}</td>
            <td>{{ brand.createdOn }}</td>
            <td>{{ brand.updatedOn }}</td>
            <td class="flex space-x-4 items-center justify-center">
              <button class="btn btn-sm btn-soft btn-info">
                <RouterLink :to="{ name: 'editBrand', params: { id: brand.id } }">Edit</RouterLink>
              </button>
              <button @click="showDeleteModal" class="btn btn-sm btn-soft btn-error">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="flex items-center justify-center">
      <p class="text-center text-error">No brand</p>
    </div>

    <div class="itbms-message">
      <DeleteModal
        v-model="isDelete"
        :title="`Delete confirm`"
        :message="`Do you want to delete this brand?`"
        @confirm="handleDelete"
        @cancel="cancelModal"
      />
    </div>

    <Alert :show="alertMessage.visible" :type="alertMessage.type" :message="alertMessage.message" />
  </div>
</template>

<style scoped></style>
