<script setup>
import { ref } from 'vue';
import DeleteModal from '../share/DeleteModal.vue';
import Alert from '../share/Alert.vue';
import { useRouter } from 'vue-router';

const props = defineProps({
  brandList: Array,
});

const emit = defineEmits(['submitDelete']);
const router = useRouter();
const deleteBrandId = ref(null);
const isDelete = ref(false);
const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
});

const handleDelete = () => {
  emit('submitDelete', deleteBrandId.value);
};

const showDeleteModal = (id) => {
  isDelete.value = true;
  deleteBrandId.value = id;
};

const cancelModal = () => {
  isDelete.value = false;
};
</script>

<template>
  <div>
    <div
      v-if="brandList"
      class="overflow-x-auto w-7xl mx-auto mb-4 rounded-box border border-base-content/10 bg-base-100"
    >
      <table class="table">
        <thead>
          <tr>
            <th class="text-center">ID</th>
            <th>Name</th>
            <th class="text-center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="brand in brandList" :key="brand.id" class="itbms-row hover:bg-gray-200/50">
            <td class="text-center">{{ brand.id }}</td>
            <td>{{ brand.name }}</td>
            <td class="flex space-x-4 items-center justify-center">
              <button
                @click="router.push(`/brands/${brand.id}`)"
                class="itbms-edit-button btn btn-sm btn-soft btn-info"
              >
                Edit
              </button>
              <button
                @click="showDeleteModal(brand.id)"
                class="itbms-delete-button btn btn-sm btn-soft btn-error"
              >
                Delete
              </button>
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

    <Alert
      :show="alertMessage.visible"
      :type="alertMessage.type"
      :message="alertMessage.message"
      @update:show="alertMessage.visible = $event"
    />
  </div>
</template>

<style scoped></style>
