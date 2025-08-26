<script setup>
import { ref, onMounted} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { httpRequest } from '@/utils/fetchUtils';

const route = useRoute();
const router = useRouter();

// States
const isLoading = ref(false);
const isSuccess = ref(false);
const isError = ref(false);
const isRequestingNew = ref(false);
const successMessage = ref('');
const errorMessage = ref('');

// Watch for token changes in URL
// watch(
//   () => route.query.token,
//   async (newToken) => {
//     if (newToken) {
//       await verifyEmailToken(newToken);
//     }
//   },
//   { immediate: true },
// );

onMounted(async () => {
  // Check if token exists in query params when component mounts
  const token = route.query.token;
  console.log('=== DEBUG INFO ===');
  console.log('Token from URL:', token);
  console.log('Current route:', route.fullPath);
  console.log('All query params:', route.query);
  console.log('Route name:', route.name);
  console.log('==================');

  if (token) {
    console.log('Token found, starting verification...');
    await verifyEmailToken(token);
  } else {
    console.log('No token found!');
    // If no token, show error
    isError.value = true;
    errorMessage.value = 'No verification token found. Please check your email link.';
  }
});

// Function to verify email token
const verifyEmailToken = async (token) => {
  try {
    isLoading.value = true;
    isSuccess.value = false;
    isError.value = false;

    console.log('Verifying token:', token); // Debug

    // Send request to backend to verify token
    const response = await httpRequest('post', `v2/users/verify-email?token=${token}`,);
    
    console.log('Response:', response); // Debug

    // Success case (status should be 200)
    if (response.status === 200) {
      isSuccess.value = true;
      successMessage.value =
        response.data.message || 'Your account has been successfully activated.';
    }
  } catch (error) {
    isError.value = true;

    // Handle different error cases
    if (error.response) {
      // Server responded with error status
      switch (error.response.status) {
        case 400:
          errorMessage.value = 'Invalid verification token.';
          break;
        case 410:
        case 422:
          errorMessage.value =
            'An error occurred, or the verification link has expired. Please request a new verification email.';
          break;
        default:
          errorMessage.value =
            error.response.data.message || 'An error occurred during verification.';
      }
    } else if (error.request) {
      // Network error
      errorMessage.value = 'Network error. Please check your internet connection and try again.';
    } else {
      // Other error
      errorMessage.value = 'An unexpected error occurred. Please try again.';
    }

    console.error('Email verification error:', error);
  } finally {
    isLoading.value = false;
  }
};

// Function to request new verification email
// const requestNewVerificationEmail = async () => {
//   try {
//     isRequestingNew.value = true;

//     // You might need to get user email from somewhere
//     // This could be stored in localStorage, route params, or user needs to enter it
//     const userEmail = localStorage.getItem('pendingVerificationEmail');

//     if (!userEmail) {
//       // Redirect to register if no email found
//       router.push('/register');
//       return;
//     }

//     const response = await axios.post(
//       `${import.meta.env.VITE_BASE_URL}/api/v2/user/resend-verification`,
//       {
//         email: userEmail,
//       },
//     );

//     if (response.status === 200) {
//       // Show success message and maybe redirect
//       alert('New verification email sent! Please check your inbox.');
//       // Optionally redirect to a "check email" page
//       // router.push('/check-email')
//     }
//   } catch (error) {
//     console.error('Resend verification error:', error);
//     alert('Failed to send verification email. Please try again.');
//   } finally {
//     isRequestingNew.value = false;
//   }
// };

// Watch for token changes in URL (ย้ายมาหลัง function declarations)
// watch(
//   () => route.query.token,
//   (newToken) => {
//     if (newToken) {
//       verifyEmailToken(newToken);
//     }
//   },
//   { immediate: true },
// );


</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="text-center">
        <h2 class="mt-6 text-3xl font-extrabold text-gray-900">Email Verification</h2>
        <p class="mt-2 text-sm text-gray-600">Please wait while we verify your account</p>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="text-center">
        <div
          class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"
        ></div>
        <p class="mt-4 text-gray-600">Verifying your email...</p>
      </div>

      <!-- Success State -->
      <div v-else-if="isSuccess" class="text-center">
        <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-green-100">
          <svg class="h-6 w-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M5 13l4 4L19 7"
            ></path>
          </svg>
        </div>
        <h3 class="mt-4 text-lg font-medium text-gray-900">Email Verified Successfully!</h3>
        <p class="mt-2 text-sm text-gray-600">{{ successMessage }}</p>
        <div class="mt-6">
          <router-link
            to="/login"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Go to Login
          </router-link>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="isError" class="text-center">
        <div class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100">
          <svg class="h-6 w-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </div>
        <h3 class="mt-4 text-lg font-medium text-gray-900">Verification Failed</h3>
        <p class="mt-2 text-sm text-gray-600">{{ errorMessage }}</p>
        <div class="mt-6 space-y-3">
          <button
            @click="requestNewVerificationEmail"
            :disabled="isRequestingNew"
            class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span
              v-if="isRequestingNew"
              class="inline-block animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"
            ></span>
            Request New Verification Email
          </button>
          <router-link
            to="/register"
            class="w-full flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Back to Register
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Add any custom styles if needed */
</style>
