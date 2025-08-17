<script setup>
import { ref, computed, watch } from 'vue'
import Alert from '@/components/share/Alert.vue'

const props = defineProps({
  isDetailPage: { type: Boolean, default: false },
  saleItemImages: { type: Array, default: () => [] },
})

const emit = defineEmits(['imagesChanged'])

const alertMessage = ref({ type: '', message: '', visible: false, duration: 3000, countdownVisible: false })
const showAlert = (type, message, duration = 3000) => {
  alertMessage.value = { type, message, visible: true, duration, countdownVisible: false }
}

const apiBase = (
  import.meta.env.VITE_BASE_URL ||
  (typeof window !== 'undefined' ? window.location.origin : '')
).replace(/\/+$/, '')

const toImageUrl = (fileName) => fileName
  ? `${apiBase}/v2/sale-items/images?fileName=${encodeURIComponent(fileName)}`
  : null

const slots = ref([
  { position: 1, fileName: null, url: null, file: null, filename: null },
  { position: 2, fileName: null, url: null, file: null, filename: null },
  { position: 3, fileName: null, url: null, file: null, filename: null },
  { position: 4, fileName: null, url: null, file: null, filename: null },
])

const selectedIndex = ref(0)

const oneBasedOrderToIndex = (order) => {
  const n = Number(order)
  if (!Number.isFinite(n)) return 0
  return Math.min(Math.max(n - 1, 0), 3)
}

const indexToOneBasedOrder = (idx) => Math.min(Math.max(idx + 1, 1), 4)

function emitImagesPayload () {
  const payload = slots.value
    .filter((s) => !!(s.file || s.fileName))
    .map((s) => ({
      order: s.position,
      file: s.file ?? null,
      fileName: s.fileName ?? null,
    }))
  console.log('imagesChanged payload:', payload)
  emit('imagesChanged', payload)
}

const mapFromSaleItemImages = (images = []) => {
  slots.value = [1, 2, 3, 4].map((p) => ({
    position: p, fileName: null, url: null, file: null, filename: null,
  }))

  images.forEach((img) => {
    const idx = oneBasedOrderToIndex(img?.imageViewOrder ?? 1)
    if (slots.value[idx]) {
      slots.value[idx].fileName = img.fileName ?? null
      slots.value[idx].url = null            
      slots.value[idx].file = null
      slots.value[idx].filename = img.fileName ?? null
      slots.value[idx].position = idx + 1   
    }
  })

  const firstHas = slots.value.findIndex((_, i) => !!thumbSrcAt(i))
  selectedIndex.value = firstHas >= 0 ? firstHas : 0

  emitImagesPayload()
}

const thumbSrcAt = (idx) => {
  const s = slots.value[idx]
  if (!s) return null
  if (s.url) return s.url
  if (s.fileName) return toImageUrl(s.fileName)
  return null
}

watch(() => props.saleItemImages, (v) => mapFromSaleItemImages(v || []), {
  immediate: true,
  deep: true,
})

const fileInput = ref(null)
const currentFilesCount = computed(() => slots.value.filter((s) => s.file || s.fileName).length)
const triggerUpload = () => fileInput.value?.click()
const canMoveUp = (idx) => idx > 0
const canMoveDown = (idx) => idx < slots.value.length - 1

const handleUpload = (e) => {
  const files = Array.from(e.target.files || [])
  if (!files.length) return

  const emptyIndexes = slots.value
    .map((s, i) => (!s.file && !s.fileName ? i : -1))
    .filter((i) => i !== -1)

  if (!emptyIndexes.length) {
    showAlert('error', 'อัปโหลดได้สูงสุด 4 รูป')
    e.target.value = ''
    return
  }

  const willTake = files.slice(0, emptyIndexes.length)
  if (files.length > emptyIndexes.length) {
    const list = willTake.map((f) => `• ${f.name}`).join('\n')
    showAlert('error', `เลือกได้สูงสุด 4 รูป\nระบบจะใช้เฉพาะไฟล์ต่อไปนี้:\n${list}`)
  }

  willTake.forEach((file, i) => {
    const idx = emptyIndexes[i]
    slots.value[idx].file = file
    slots.value[idx].url = URL.createObjectURL(file)
    slots.value[idx].filename = file.name
    slots.value[idx].fileName = null         
  })

  const firstHas = slots.value.findIndex((_, i) => !!thumbSrcAt(i))
  selectedIndex.value = firstHas >= 0 ? firstHas : 0

  emitImagesPayload()
  e.target.value = ''
}

const selectSlot = (idx) => { selectedIndex.value = idx }

const clearSlot = (idx) => {
  slots.value[idx] = { position: indexToOneBasedOrder(idx), fileName: null, url: null, file: null, filename: null }
  const firstHas = slots.value.findIndex((_, i) => !!thumbSrcAt(i))
  selectedIndex.value = firstHas >= 0 ? firstHas : idx
  emitImagesPayload()
}

const moveUp = (idx) => {
  if (!canMoveUp(idx)) return
  const a = slots.value
  ;[a[idx - 1], a[idx]] = [a[idx], a[idx - 1]]
  a.forEach((s, i) => (s.position = indexToOneBasedOrder(i)))
  emitImagesPayload()
}
const moveDown = (idx) => {
  if (!canMoveDown(idx)) return
  const a = slots.value
  ;[a[idx + 1], a[idx]] = [a[idx], a[idx + 1]]
  a.forEach((s, i) => (s.position = indexToOneBasedOrder(i)))
  emitImagesPayload()
}

const selectedUrl = computed(() => thumbSrcAt(selectedIndex.value))
</script>

<template>
  <div class="flex flex-col-reverse lg:flex-row justify-center items-center gap-6 lg:gap-12">
    <!-- thumbnails -->
    <div class="flex flex-row lg:flex-col justify-center items-center gap-4 lg:gap-6">
      <div
        v-for="(slot, index) in slots"
        :key="slot.file?.name || slot.fileName || slot.position"
        class="relative group flex flex-col items-center"
      >
        <img
          v-if="thumbSrcAt(index)"
          :src="thumbSrcAt(index)"
          :class="[
            `itbms-picture-file${index + 1}`,
            'w-16 h-20 object-contain cursor-pointer transition-opacity duration-200',
            selectedIndex === index ? 'opacity-100' : 'opacity-40 hover:opacity-80',
          ]"
          alt="thumbnail"
          @click="selectSlot(index)"
        />
        <div v-else class="w-16 h-20 border rounded bg-gray-50 flex items-center justify-center text-[10px] text-gray-400">
          (ว่าง)
        </div>

        <div class="text-[10px] mt-1 max-w-16 text-center truncate" :title="slot.filename || slot.fileName || 'empty slot'">
          {{ slot.filename || slot.fileName || '(ว่าง)' }}
        </div>

        <div v-if="!props.isDetailPage" class="flex gap-1 mt-1">
          <button
            :class="`itbms-picture-file${index + 1}-up px-2 py-0.5 rounded bg-gray-200 cursor-pointer hover:bg-gray-300`"
            v-if="index > 0"
            @click="moveUp(index)"
          >
            ↑
          </button>
          <button
            :class="`itbms-picture-file${index + 1}-down px-2 py-0.5 rounded bg-gray-200 cursor-pointer hover:bg-gray-300`"
            v-if="index < slots.length - 1"
            @click="moveDown(index)"
          >
            ↓
          </button>
          <button
            v-if="slot.file || slot.fileName"
            :class="`itbms-picture-file${index + 1}-clear cursor-pointer hover:bg-red-600`"
            class="px-2 py-0.5 rounded bg-red-500 text-white"
            @click="clearSlot(index)"
            title="ลบรูปนี้"
          >
            ×
          </button>
        </div>
      </div>
    </div>

    <!-- รูปใหญ่ -->
    <img
      v-if="selectedUrl"
      class="w-full max-w-[280px] sm:max-w-[300px] md:max-w-[320px] lg:max-w-[310px] xl:max-w-[390px] lg:h-[520px] object-contain"
      :src="selectedUrl"
      alt="selected"
    />

    <!-- อัปโหลด -->
    <input
      v-if="!props.isDetailPage"
      type="file"
      accept="image/*"
      multiple
      ref="fileInput"
      class="hidden"
      @change="handleUpload"
    />
  </div>

  <div class="mt-4 flex gap-3" v-if="!props.isDetailPage">
    <button
      :disabled="currentFilesCount >= 4"
      class="itbms-upload-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md text-white transition-colors bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500/50 disabled:bg-gray-300 disabled:cursor-not-allowed"
      @click="currentFilesCount >= 4 ? showAlert('error', 'อัปโหลดได้สูงสุด 4 รูป') : triggerUpload()"
    >
      + Upload Pictures
    </button>
  </div>

  <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
    :duration="alertMessage.duration"
    :countdownVisible="alertMessage.countdownVisible"
  />
</template>
