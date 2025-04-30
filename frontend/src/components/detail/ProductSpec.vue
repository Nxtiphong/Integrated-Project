<script setup>
import { Icon } from '@iconify/vue';
import { ref } from 'vue';
const props = defineProps({
  product: {
    type: Object,
    required: true,
  },
});

const specs = ref([
  { label: 'RAM', icon: 'mingcute:chip-line', value: `${props.product.ramGb} GB` },
  {
    label: 'Screen Size',
    icon: 'fluent:phone-32-filled',
    value: `${props.product.screenSizeInch}"`,
  },
  { label: 'Storage', icon: 'fluent:storage-16-regular', value: `${props.product.storageGb} GB` },
  { label: 'Color', icon: 'tabler:color-filter', value: props.product.color },
]);
</script>

<template>
  <div class="grid grid-cols-2 gap-4 w-full">
    <div
      v-for="(spec, index) in specs"
      :key="index"
      class="self-stretch w-full p-4 rounded-md bg-base-200 flex items-center gap-4"
    >
      <Icon :icon="spec.icon" width="35" />
      <div class="flex flex-col">
        <p>{{ spec.label }}</p>
        <p>{{ spec.value }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
