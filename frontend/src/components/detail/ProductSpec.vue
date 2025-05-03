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
  {
    label: 'RAM',
    icon: 'mingcute:chip-line',
    value: props.product.ramGb,
    unit: 'GB',
    class: 'Itbms-ramGb',
  },
  {
    label: 'Screen Size',
    icon: 'fluent:phone-32-filled',
    value: props.product.screenSizeInch,
    unit: 'inches',
    class: 'Itbms-screenSizeInch',
  },
  {
    label: 'Storage',
    icon: 'fluent:storage-16-regular',
    value: props.product.storageGb,
    unit: 'GB',
    class: 'Itbms-storageGb',
  },
  { label: 'Color', icon: 'tabler:color-filter', value: props.product.color, class: 'Itbms-color' },
]);
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-2 lg:gap-4 w-full">
    <div
      v-for="(spec, index) in specs"
      :key="index"
      class="self-stretch w-full p-4 rounded-md bg-[#F6F6F6] flex items-center gap-4"
    >
      <Icon :icon="spec.icon" width="35" />
      <div class="flex flex-col">
        <p>{{ spec.label }}</p>
        <div class="flex gap-1">
          <span v-if="spec.value" :class="spec.class">
            {{ spec.value }}
          </span>
          <span v-if="spec.value" :class="spec.class + '-unit'">{{ spec.unit }}</span>
          <p v-else class="text-gray-400">-</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
