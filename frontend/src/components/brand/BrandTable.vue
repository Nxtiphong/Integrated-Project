<script setup>
import { ref } from 'vue';
import DeleteModal from '../share/DeleteModal.vue';
import { useRouter } from 'vue-router';
import { fetchBrandDetail } from '@/utils/brandUtils';

const props = defineProps({
  brandList: Array,
});

const emit = defineEmits(['submitDelete']);
const router = useRouter();
const deleteBrandId = ref(null);
const deleteBrandName = ref(null);
const isDelete = ref(false);
const isWarning = ref(false);

const handleDelete = () => {
  emit('submitDelete', deleteBrandId.value);
};

const showDeleteModal = async (brandId, brandName) => {
  const result = await fetchBrandDetail(brandId);

  deleteBrandName.value = brandName;

  if (result.success && result.data.noOfSaleItems > 0) {
    isDelete.value = true;
    isWarning.value = true;
  } else {
    isDelete.value = true;
    deleteBrandId.value = brandId;
  }
};

const cancelModal = () => {
  isDelete.value = false;
};

</script>

<template>
  <div>
    <div
      v-if="brandList.length > 0"
      class="overflow-x-auto w-xs xs:w-lg sm:w-2xl md:w-2xl lg:w-4xl xl:w-7xl mx-auto mb-4 rounded-box border border-base-content/10 bg-base-100"
    >
      <table class="table table-xs sm:table-sm md:table-md">
        <thead>
          <tr>
            <th class="text-center">ID</th>
            <th>Name</th>
            <th class="text-center">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="brand in brandList" :key="brand.id" class="itbms-row hover:bg-gray-200/50">
            <td class="itbms-id text-center">{{ brand.id }}</td>
            <td class="itbms-name">{{ brand.name }}</td>
            <td class="flex space-x-4 items-center justify-center">
              <button
                @click="router.push(`/brands/${brand.id}/edit`)"
                class="itbms-edit-button btn btn-sm btn-soft btn-info"
              >
                Edit
              </button>
              <button
                @click="showDeleteModal(brand.id, brand.name)"
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
        confirm-button-text="Confirm"
        :title="isWarning ? `Not Allowed` : `Delete confirm`"
        :message="
          isWarning
            ? `Delete ${deleteBrandName} is not allowed. There are sale items with ${deleteBrandName} brand.`
            : `Do you want to delete ${deleteBrandName} brand?`
        "
        :is-confirm-button="!isWarning"
        @confirm="handleDelete"
        @cancel="cancelModal"
      />
    </div>
  </div>
</template>

<style scoped></style>
