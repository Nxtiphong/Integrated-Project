<script setup>
import { ref } from 'vue';
import DeleteModal from '../share/DeleteModal.vue';

const props = defineProps({
  brandList: Array,
});

const isDelete = ref(false);

const handleDelete = () => {
  console.log('Delete');
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
      v-if="brandList"
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
            <td>Cy Ganderton</td>
            <td>example.com</td>
            <td>True</td>
            <td>UK</td>
            <td>DATE</td>
            <td>DATE</td>
            <td class="flex space-x-4 items-center justify-center">
              <button class="btn btn-sm btn-soft btn-info">
                <RouterLink to="/sale-items/brand/1/edit">Edit</RouterLink>
              </button>
              <button @click="showDeleteModal" class="btn btn-sm btn-soft btn-error">Delete</button>
            </td>

            <div class="itbms-message">
              <DeleteModal
                v-model="isDelete"
                :title="`Delete confirm`"
                :message="`Do you want to delete this brand?`"
                @confirm="handleDelete"
                @cancel="cancelModal"
              />
            </div>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="flex items-center justify-center">
      <p class="text-center text-error">No brand</p>
    </div>
  </div>
</template>

<style scoped></style>
