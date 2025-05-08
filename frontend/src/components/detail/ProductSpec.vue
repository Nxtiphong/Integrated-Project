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
    class: 'itbms-ramGb',
  },
  {
    label: 'Screen Size',
    icon: 'fluent:phone-32-filled',
    value: props.product.screenSizeInch,
    unit: 'Inches',
    class: 'itbms-screenSizeInch',
  },
  {
    label: 'Storage',
    icon: 'fluent:storage-16-regular',
    value: props.product.storageGb,
    unit: 'GB',
    class: 'itbms-storageGb',
  },
  { label: 'Color', icon: 'tabler:color-filter', value: props.product.color, class: 'itbms-color' },
]);
</script>

<template>
  <div class="grid grid-cols-1 lg:grid-cols-2 gap-2 lg:gap-4 w-full">
    <div
      v-for="(spec, index) in specs"
      :key="index"
      class="self-stretch w-full px-4 py-2 lg:p-4 rounded-md text-sm lg:text-base bg-[#F6F6F6] flex items-center gap-4"
    >
      <Icon :icon="spec.icon" class="text-xl md:text-2xl lg:text-3xl" />
      <div class="flex flex-col">
        <p>{{ spec.label }}</p>
        <div class="flex gap-1">
          <span v-if="spec.value" :class="spec.class">
            {{ spec.value }}
          </span>
          <span v-if="spec.value" :class="spec.class + '-unit'">{{ spec.unit }}</span>
          <p v-else class="text-gray-400" :class="spec.class">-</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
