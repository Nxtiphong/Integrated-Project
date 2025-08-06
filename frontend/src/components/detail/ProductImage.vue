<script setup>
import { ref } from 'vue';
import mockPhoneImage1 from '@/assets/images/mock_phone.png';
import mockPhoneImage2 from '@/assets/images/mock_phone2.png';
import mockPhoneImage3 from '@/assets/images/mock_phone3.png';
import mockPhoneImage4 from '@/assets/images/mock_phone4.png';

const props = defineProps({
  isDetailPage: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['imagesChanged']);

const uploadedImages = ref([]);
const selectedImage = ref(null);
const fileInput = ref(null);

const defaultMockImages = [
  { file: null, url: mockPhoneImage1 },
  { file: null, url: mockPhoneImage2 },
  { file: null, url: mockPhoneImage3 },
  { file: null, url: mockPhoneImage4 },
];

uploadedImages.value = [...defaultMockImages];
selectedImage.value = defaultMockImages[0].url;

const triggerUpload = () => {
  fileInput.value?.click();
};
const handleUpload = (event) => {
  const files = event.target.files;
  if (!files || !files.length) return;

  const file = files[0];

  if (uploadedImages.value.length >= 4) {
    alert('อัปโหลดได้สูงสุด 4 รูป');
    return;
  }

  const url = URL.createObjectURL(file);
  uploadedImages.value.push({ file, url });

  if (!selectedImage.value) {
    selectedImage.value = url;
  }

  emit('imagesChanged', uploadedImages.value.map(img => img.file));

  event.target.value = '';
};

const selectImage = (url) => {
  selectedImage.value = url;
};

const removeImage = (index) => {
  const removedUrl = uploadedImages.value[index].url;
  uploadedImages.value.splice(index, 1);
  if (selectedImage.value === removedUrl) {
    selectedImage.value = uploadedImages.value[0]?.url || null;
  }
};
</script>

<template>
  <div class="flex flex-col-reverse lg:flex-row justify-center items-center gap-6 lg:gap-12">
    <div class="flex flex-row lg:flex-col justify-center items-center gap-4 lg:gap-6">
      <div v-for="(img, index) in uploadedImages" :key="index" class="relative group">
        <img
          :class="[
            `itbms-picture-file${index+1}`,
            'w-16 h-20 object-contain cursor-pointer transition-opacity duration-200',
            selectedImage === img.url ? 'opacity-100' : 'opacity-40 hover:opacity-80',
          ]"
          :src="img.url"
          alt="thumbnail"
          @click="selectImage(img.url)"
        />
        <button
          v-if="!props.isDetailPage"
          @click="removeImage(index)"
          :class="`itbms-picture-file${index+1}-clear`"
          class="absolute top-1 right-1 bg-red-500 text-white rounded-full px-2 py-1 text-xs opacity-80 hover:opacity-100"
        >
          ลบ
        </button>
      </div>
    </div>

    <img
      v-if="selectedImage"
      class="w-full max-w-[280px] sm:max-w-[300px] md:max-w-[320px] lg:max-w-[310px] xl:max-w-[390px] lg:h-[520px] object-contain"
      :src="selectedImage"
      alt="selected"
    />

    <input type="file" accept="image/*" ref="fileInput" class="hidden" @change="handleUpload" />
  </div>

  <button
    v-if="!props.isDetailPage && uploadedImages.length < 4"
    class="itbms-upload-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md text-white transition-colors bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-primary/50 disabled:bg-gray-300 disabled:cursor-not-allowed"
    @click="triggerUpload"
  >
    + เพิ่มรูป
  </button>
</template>
