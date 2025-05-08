<script setup>
import mockPhoneImage1 from '@/assets/images/mock_phone.png';
import mockPhoneImage2 from '@/assets/images/mock_phone2.png';
import mockPhoneImage3 from '@/assets/images/mock_phone3.png';
import mockPhoneImage4 from '@/assets/images/mock_phone4.png';
import { ref } from 'vue';

const images = [mockPhoneImage1, mockPhoneImage2, mockPhoneImage3, mockPhoneImage4];
const selectedImage = ref(images[0]);

const selectImage = (img) => {
  selectedImage.value = img;
};

const props = defineProps({
  isAddPage: {
    type: Boolean,
    default: false,
  },
});
</script>

<template>
  <div class="flex flex-col-reverse lg:flex-row justify-center items-center gap-6 lg:gap-12">
    <div class="flex flex-row lg:flex-col justify-center items-center gap-4 lg:gap-6">
      <img
        v-for="(img, index) in images"
        :key="index"
        class="w-16 h-20 object-contain cursor-pointer transition-opacity duration-200"
        :class="{
          'opacity-100': selectedImage === img,
          'opacity-40 hover:opacity-80': selectedImage !== img,
        }"
        :src="img"
        alt="product-thumbnail"
        @click="selectImage(img)"
      />
    </div>

    <img
      class="w-full max-w-[280px] sm:max-w-[300px] md:max-w-[320px] lg:max-w-[400px] lg:h-[520px] object-contain"
      :src="selectedImage"
      alt="product-main-image"
    />
  </div>
</template>
