<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { httpRequest } from '@/utils/fetchUtils';
import { useRegisterFormStore } from '@/stores/useRegisterFormStore';
import Alert from '@/components/share/Alert.vue';

const router = useRouter();

// account type
const accountType = ref('BUYER');

const form = ref({
  nickName: '',
  email: '',
  password: '',
  fullName: '',
  userType: '',
  mobile: '',
  bankAccountNumber: '',
  bankName: '',
  nationalIdNumber: '',
  nationalFrontFile: null,
  nationalBackFile: null,
});

const isSubmitting = ref(false);
const passwordError = ref('');
const fullNameError = ref('');

// state สำหรับ Alert
const registerStore = useRegisterFormStore();

const alertMessage = ref({
  type: 'error',
  message: '',
  visible: false,
});

// validate
const validatePassword = (password) => {
  if (password.length < 8) return 'รหัสผ่านต้องมีอย่างน้อย 8 ตัวอักษร';
  if (!/[a-z]/.test(password)) return 'รหัสผ่านต้องมีตัวอักษรเล็กอย่างน้อย 1 ตัว';
  if (!/[A-Z]/.test(password)) return 'รหัสผ่านต้องมีตัวอักษรใหญ่อย่างน้อย 1 ตัว';
  return '';
};

const validateFullName = (fullName) => {
  if (fullName.length < 4) return 'ชื่อ-นามสกุลต้องมีอย่างน้อย 4 ตัวอักษร';
  if (fullName.length > 40) return 'ชื่อ-นามสกุลต้องไม่เกิน 40 ตัวอักษร';
  return '';
};

const isFormValid = computed(() => {
  if (!form.value.fullName || !form.value.nickName || !form.value.email || !form.value.password) {
    return false;
  }
  if (accountType.value === 'SELLER') {
    return (
      form.value.mobile &&
      form.value.bankAccountNumber &&
      form.value.bankName &&
      form.value.nationalIdNumber &&
      form.value.nationalFrontFile &&
      form.value.nationalBackFile
    );
  }
  return true;
});

const handleFileUpload = (event, field) => {
  form.value[field] = event.target.files[0];
};

const handleSubmit = async () => {
  isSubmitting.value = true;
  passwordError.value = '';
  fullNameError.value = '';

  // validate full name
  const fullNameErr = validateFullName(form.value.fullName);
  if (fullNameErr) {
    fullNameError.value = fullNameErr;
    isSubmitting.value = false;
    return;
  }

  // validate password
  const pwdErr = validatePassword(form.value.password);
  if (pwdErr) {
    passwordError.value = pwdErr;
    isSubmitting.value = false;
    return;
  }

  try {
    // create FormData
    const payload = new FormData();
    payload.append('accountType', accountType.value);
    payload.append('nickName', form.value.nickName);
    payload.append('email', form.value.email);
    payload.append('password', form.value.password);
    payload.append('fullName', form.value.fullName);
    payload.append('userType', accountType.value);

    if (accountType.value === 'SELLER') {
      payload.append('phoneNumber', form.value.mobile);
      payload.append('bankAccount', form.value.bankAccountNumber);
      payload.append('bankName', form.value.bankName);
      payload.append('idCardNumber', form.value.nationalIdNumber);

      if (form.value.nationalFrontFile) {
        payload.append('idCardImageFront', form.value.nationalFrontFile);
      }
      if (form.value.nationalBackFile) {
        payload.append('idCardImageBack', form.value.nationalBackFile);
      }
    }

    const res = await httpRequest('POST', 'v2/users/register', payload);
    console.log('register response:', res);

    // show success alert
    router.push('/');
    registerStore.isCreateSuccess = true;

    // redirect
  } catch (err) {
    console.error('Registration error:', err);
    alertMessage.value = {
      type: 'error',
      message: 'Register failed!',
      visible: true,
    };
    // show error alert
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<template>
  <div class="max-w-lg mx-auto p-6 bg-white rounded-2xl shadow-md">
    <h2 class="text-2xl font-bold mb-6 text-center">Register</h2>

    <div class="flex justify-center gap-6 mb-6">
      <label class="flex items-center gap-2">
        <input type="radio" value="BUYER" v-model="accountType" class="radio radio-primary" />
        Buyer
      </label>
      <label class="flex items-center gap-2">
        <input type="radio" value="SELLER" v-model="accountType" class="radio radio-primary" />
        Seller
      </label>
    </div>

    <form @submit.prevent="handleSubmit" class="space-y-4">
      <!-- buyer + seller -->
      <div>
        <label class="block font-medium">Full name</label>
        <input
          v-model.trim="form.fullName"
          type="text"
          class="input input-bordered w-full"
          required
        />
        <span v-if="fullNameError" class="text-red-500 text-sm mt-1">{{ fullNameError }}</span>
      </div>
      <div>
        <label class="block font-medium">NickName</label>
        <input
          v-model.trim="form.nickName"
          type="text"
          class="input input-bordered w-full"
          required
        />
      </div>
      <div>
        <label class="block font-medium">Email</label>
        <input
          v-model.trim="form.email"
          type="email"
          class="input input-bordered w-full"
          required
        />
      </div>
      <div>
        <label class="block font-medium">Password</label>
        <input
          v-model.trim="form.password"
          type="password"
          class="input input-bordered w-full"
          required
        />
        <span v-if="passwordError" class="text-red-500 text-sm mt-1">{{ passwordError }}</span>
      </div>

      <!-- seller only -->
      <template v-if="accountType === 'SELLER'">
        <div>
          <label class="block font-medium">Mobile</label>
          <input
            v-model.trim="form.mobile"
            type="text"
            class="input input-bordered w-full"
            required
          />
        </div>
        <div>
          <label class="block font-medium">Bank Account No</label>
          <input
            v-model.trim="form.bankAccountNumber"
            type="text"
            class="input input-bordered w-full"
            required
          />
        </div>
        <div>
          <label class="block font-medium">Bank Name</label>
          <input
            v-model.trim="form.bankName"
            type="text"
            class="input input-bordered w-full"
            required
          />
        </div>
        <div>
          <label class="block font-medium">National ID No</label>
          <input
            v-model.trim="form.nationalIdNumber"
            type="text"
            class="input input-bordered w-full"
            required
          />
        </div>
        <div>
          <label class="block font-medium">Card Photo Front</label>
          <input
            type="file"
            @change="(e) => handleFileUpload(e, 'nationalFrontFile')"
            accept="image/*"
          />
        </div>
        <div>
          <label class="block font-medium">Card Photo Back</label>
          <input
            type="file"
            @change="(e) => handleFileUpload(e, 'nationalBackFile')"
            accept="image/*"
          />
        </div>
      </template>

      <button type="submit" class="btn btn-primary w-full" :disabled="isSubmitting || !isFormValid">
        {{ isSubmitting ? 'Registering...' : 'Submit' }}
      </button>
      <button type="reset" class="btn btn-primary w-full" :disabled="isSubmitting">
        {{ isSubmitting ? 'Cancel' : 'Cancel' }}
      </button>
    </form>
  </div>

  <Alert
    :show="alertMessage.visible"
    :type="alertMessage.type"
    :message="alertMessage.message"
    @update:show="alertMessage.visible = $event"
  />
</template>

<style scoped>
input[type='file'] {
  display: block;
  margin-top: 4px;
}
</style>