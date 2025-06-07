<script setup>
import { computed } from 'vue';

const props = defineProps({
  id: String,
  modelValue: [String, Number],
  label: String,
  type: {
    type: String,
    default: 'text',
  },
  placeholder: String,
  error: Boolean,
  errorMessage: String,
  required: Boolean,
  focused: Boolean,
  nextFieldId: String,
  customClass: [String, Number],
});

const emit = defineEmits(['update:modelValue', 'blur', 'keydownEnter']);

const inputClass = computed(() => [
  `${props.customClass} text-xs sm:text-sm md:text-base px-3 py-2 border rounded-md focus:outline-none focus:ring-2 transition-colors w-full`,
  props.error
    ? 'border-red-500 focus:ring-red-200 focus:border-red-500'
    : 'border-gray-300 focus:ring-primary/20 focus:border-primary',
]);

const handleInput = (event) => {
  const value = event.target.value;
  emit('update:modelValue', value);
};

const handleBlur = (event) => {
  const value = event.target.value.trim();
  emit('update:modelValue', value);
  emit('blur');
};

const handleKeydown = (e) => {
  if (e.key === 'Enter' && props.nextFieldId) {
    e.preventDefault();
    const value = e.target.value.trim();
    emit('update:modelValue', value);
    emit('keydownEnter', props.nextFieldId);
  }
};
</script>

<template>
  <label :for="id" class="font-semibold text-xs sm:text-sm md:text-base">
    {{ label }}
    <span v-if="required" class="text-error">*</span>
  </label>

  <input
    :id="id"
    :type="type"
    :placeholder="placeholder"
    :value="modelValue"
    @input="handleInput"
    @blur="handleBlur"
    @keydown="handleKeydown"
    :class="inputClass"
  />

  <p v-if="error" class="text-error text-xs z-10 itbms-message">
    {{ errorMessage }}
  </p>
</template>

<style scoped></style>
