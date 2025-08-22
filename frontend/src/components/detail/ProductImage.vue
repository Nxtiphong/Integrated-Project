<script setup>
import { ref, computed, watch } from 'vue'
import Alert from '@/components/share/Alert.vue'

const props = defineProps({
  isDetailPage: { type: Boolean, default: false },
  saleItemImages: { type: Array, default: () => [] },
})
const emit = defineEmits(['imagesChanged'])

const apiBase = (
  import.meta.env.VITE_BASE_URL || (typeof window !== 'undefined' ? window.location.origin : '')
).replace(/\/+$/, '')

const toImageUrl = (fileName) =>
  fileName ? `${apiBase}/v2/sale-items/images?fileName=${encodeURIComponent(fileName)}` : null

const alertMessage = ref({
  type: '',
  message: '',
  visible: false,
  duration: 3000,
  countdownVisible: false,
})
const showAlert = (type, message, duration = 3000) => {
  alertMessage.value = { type, message, visible: true, duration, countdownVisible: false }
}

const slots = ref([
  { position: 1, fileName: null, url: null, file: null, filename: null, isDeleted: false, deletedFileName: null, deletedOrder: null },
  { position: 2, fileName: null, url: null, file: null, filename: null, isDeleted: false, deletedFileName: null, deletedOrder: null },
  { position: 3, fileName: null, url: null, file: null, filename: null, isDeleted: false, deletedFileName: null, deletedOrder: null },
  { position: 4, fileName: null, url: null, file: null, filename: null, isDeleted: false, deletedFileName: null, deletedOrder: null },
])

const selectedIndex = ref(0)
const prevOrderByFileName = ref({})

const iToOrder = (idx) => Math.min(Math.max(idx + 1, 1), 4)
const orderToIndex = (order) => Math.min(Math.max(Number(order || 1) - 1, 0), 3)

const thumbSrcAt = (idx) => {
  const s = slots.value[idx]
  if (!s) return null
  if (s.url) return s.url
  if (s.fileName) return toImageUrl(s.fileName)
  return null
}

function emitImagesPayload () {
  const payload = []

  for (const s of slots.value) {
    if (s.deletedFileName) {
      payload.push({ status: 'DELETE', order: s.deletedOrder ?? s.position, fileName: s.deletedFileName })
    }

    if (s.isDeleted) continue

    if (s.file) {
      payload.push({ status: 'NEW', order: s.position, imageFile: s.file })
      continue
    }

    if (s.fileName) {
      const prevOrder = prevOrderByFileName.value[s.fileName]
      const status = prevOrder && prevOrder !== s.position ? 'MOVE' : 'ONLINE'
      payload.push({ status, order: s.position, fileName: s.fileName })
    }
  }

  console.log('emitImagesPayload', payload)
  emit('imagesChanged', payload)
}

const mapFromSaleItemImages = (images = []) => {
  slots.value = [1,2,3,4].map(p => ({
    position: p, fileName: null, url: null, file: null, filename: null,
    isDeleted: false, deletedFileName: null, deletedOrder: null
  }))

  const m = {}
  images.forEach(img => {
    const order1 = Number(img.imageViewOrder || 1)
    const idx = orderToIndex(order1)
    if (slots.value[idx]) {
      slots.value[idx].fileName = img.fileName || null
      slots.value[idx].filename = img.fileName || null
      slots.value[idx].url = null
      slots.value[idx].file = null
      slots.value[idx].position = idx + 1
      slots.value[idx].isDeleted = false
      slots.value[idx].deletedFileName = null
      slots.value[idx].deletedOrder = null
      if (img.fileName) m[img.fileName] = order1
    }
  })
  prevOrderByFileName.value = m

  const firstHas = slots.value.findIndex((_, i) => !!thumbSrcAt(i))
  selectedIndex.value = firstHas >= 0 ? firstHas : 0
  emitImagesPayload()
}

watch(() => props.saleItemImages, v => mapFromSaleItemImages(v || []), { immediate: true, deep: true })

const fileInput = ref(null)
const currentFilesCount = computed(() =>
  slots.value.filter(s => (s.file || s.fileName) && !s.isDeleted).length
)

const triggerUpload = () => fileInput.value?.click()
const canMoveUp = (idx) => idx > 0
const canMoveDown = (idx) => idx < slots.value.length - 1

const handleUpload = (e) => {
  const files = Array.from(e.target.files || [])
  if (!files.length) return

  const valid = files.filter(f => {
    if (!f.size || f.size >= 2 * 1024 * 1024) {
      showAlert('error', 'The picture file size cannot be larger than 2MB.')
      return false
    }
    return true
  })
  if (!valid.length) { e.target.value = ''; return }

  const deletedSlots = slots.value
    .map((s, i) => (s.isDeleted && s.fileName ? i : -1))
    .filter(i => i !== -1)

  const emptySlots = slots.value
    .map((s, i) => (!s.file && !s.fileName ? i : -1))
    .filter(i => i !== -1)

  const targetIndexes = [...deletedSlots, ...emptySlots]

  if (valid.length > targetIndexes.length) {
    showAlert('error', 'Maximum 4 pictures are allowed.')
  }

  const willTake = valid.slice(0, targetIndexes.length)

  willTake.forEach((file, i) => {
    const idx = targetIndexes[i]
    const s = slots.value[idx]

    if (s.isDeleted && s.fileName) {
      s.isDeleted = false
      s.deletedFileName = s.deletedFileName || s.fileName
      s.deletedOrder = s.deletedOrder || s.position
      s.fileName = null
      s.filename = null
    }

    s.file = file
    s.url = URL.createObjectURL(file)
    s.filename = file.name

    selectedIndex.value = idx
  })

  emitImagesPayload()
  e.target.value = ''
}

const selectSlot = (idx) => { selectedIndex.value = idx }

const toggleDelete = (idx) => {
  const s = slots.value[idx]
  if (s.fileName) {
    if (!s.isDeleted) {
      s.isDeleted = true
      s.deletedFileName = s.fileName
      s.deletedOrder = s.position
    } else {
      s.isDeleted = false
      s.deletedFileName = null
      s.deletedOrder = null
    }
  } else if (s.file) {
    slots.value[idx] = {
      position: iToOrder(idx),
      fileName: null, url: null, file: null, filename: null,
      isDeleted: false, deletedFileName: null, deletedOrder: null
    }
  }
  emitImagesPayload()
}

const moveUp = (idx) => {
  if (!canMoveUp(idx)) return
  const a = slots.value
  ;[a[idx - 1], a[idx]] = [a[idx], a[idx - 1]]
  a.forEach((s, i) => (s.position = iToOrder(i)))
  emitImagesPayload()
}
const moveDown = (idx) => {
  if (!canMoveDown(idx)) return
  const a = slots.value
  ;[a[idx + 1], a[idx]] = [a[idx], a[idx + 1]]
  a.forEach((s, i) => (s.position = iToOrder(i)))
  emitImagesPayload()
}

const selectedUrl = computed(() => thumbSrcAt(selectedIndex.value))
</script>

<template>
  <div class="flex flex-col-reverse lg:flex-row justify-center items-center gap-6 lg:gap-12">
    <div class="flex flex-row lg:flex-col justify-center items-center gap-4 lg:gap-6">
      <div
        v-for="(slot, index) in slots"
        :key="slot.file?.name || slot.fileName || slot.position"
        class="relative group flex flex-col items-center"
      >
        <div class="relative">
          <img
            v-if="thumbSrcAt(index)"
            :src="thumbSrcAt(index)"
            :class="[
              `itbms-picture-file${index + 1}`,
              'w-16 h-20 object-contain cursor-pointer transition-opacity duration-200 rounded',
              selectedIndex === index ? 'opacity-100' : 'opacity-40 hover:opacity-80',
              slot.isDeleted ? 'opacity-40' : ''
            ]"
            alt="thumbnail"
            @click="selectSlot(index)"
          />
          <div
            v-else
            class="w-16 h-20 border rounded bg-gray-50 flex items-center justify-center text-[10px] text-gray-400"
          >(ว่าง)</div>

          <!-- แถบขีดฆ่า + badge DELETE -->
          <div v-if="slot.isDeleted" class="pointer-events-none absolute inset-0">
            <div class="absolute inset-0 flex items-center justify-center">
              <div class="h-0.5 w-[120%] bg-red-600 rotate-45"></div>
            </div>
            <span class="absolute top-0 left-0 m-0.5 text-[9px] px-1 py-0.5 bg-red-600 text-white rounded">DELETE</span>
          </div>
        </div>

        <div v-if="!props.isDetailPage" class="text-[10px] mt-1 max-w-16 text-center truncate" :title="slot.filename || slot.fileName || 'empty'">
          {{ slot.filename || slot.fileName || '(ว่าง)' }}
        </div>

        <div v-if="!props.isDetailPage" class="flex gap-1 mt-1">
          <button
            :class="[
              `itbms-picture-file${index + 1}-up px-2 py-0.5 rounded bg-gray-200`,
              index > 0 ? 'cursor-pointer hover:bg-gray-300' : 'opacity-50 cursor-not-allowed',
            ]"
            :disabled="index === 0"
            @click="moveUp(index)"
          >↑</button>

          <button
            :class="[
              `itbms-picture-file${index + 1}-down px-2 py-0.5 rounded bg-gray-200`,
              index < slots.length - 1 ? 'cursor-pointer hover:bg-gray-300' : 'opacity-50 cursor-not-allowed',
            ]"
            :disabled="index === slots.length - 1"
            @click="moveDown(index)"
          >↓</button>
        </div>

        <button
          v-if="(slot.file || slot.fileName) && !props.isDetailPage"
          :class="`itbms-picture-file${index + 1}-clear cursor-pointer mt-2 ${slot.isDeleted ? 'bg-orange-500 hover:bg-orange-600' : 'bg-red-500 hover:bg-red-600'} text-white px-2 py-0.5 rounded`"
          @click="toggleDelete(index)"
          :title="slot.isDeleted ? 'ยกเลิกการลบ' : 'ลบรูปนี้'"
        >
          {{ slot.isDeleted ? '↺' : '×' }}
        </button>
      </div>
    </div>

    <!-- รูปใหญ่ -->
    <img
      v-if="selectedUrl"
      class="w-full max-w-[280px] sm:max-w-[300px] md:max-w-[320px] lg:max-w-[310px] xl:max-w-[390px] lg:h-[520px] object-contain"
      :src="selectedUrl"
      alt="selected"
    />

    <input v-if="!props.isDetailPage" type="file" accept="image/*" multiple ref="fileInput" class="hidden" @change="handleUpload" />
  </div>

  <div class="mt-4 flex gap-3" v-if="!props.isDetailPage">
    <button
      :disabled="currentFilesCount >= 4"
      class="itbms-upload-button cursor-pointer flex items-center gap-2 px-5 py-2.5 rounded-md text-white transition-colors bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500/50 disabled:bg-gray-300 disabled:cursor-not-allowed"
      @click="currentFilesCount >= 4 ? showAlert('error','Maximum 4 pictures are allowed.') : triggerUpload()"
    >+ Upload Pictures</button>
  </div>

  <Alert
    class="z-50"
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
    :duration="alertMessage.duration"
    :countdownVisible="alertMessage.countdownVisible"
  />
</template>
