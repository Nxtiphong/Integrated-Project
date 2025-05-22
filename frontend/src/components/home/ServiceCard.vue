<script setup>
import { Icon } from '@iconify/vue';
</script>

<template>
  <section class="py-16 bg-gradient-to-b from-gray-50 to-white">
    <div class="container mx-auto px-4">
      <div class="text-center mb-12">
        <h2 class="text-3xl md:text-4xl font-light text-gray-800 mb-4">Why Choose Our Store</h2>
        <p class="text-gray-600 text-lg max-w-2xl mx-auto">
          Experience premium service and convenience with every purchase
        </p>
      </div>

      <div class="grid md:grid-cols-4 gap-6">
        <div
          class="service-card group bg-white/80 backdrop-blur-sm border border-gray-200 rounded-2xl p-6 text-center hover:bg-white hover:border-blue-300 hover:shadow-xl transition-all duration-300 hover:transform hover:scale-105"
        >
          <div
            class="w-16 h-16 bg-gradient-to-r from-blue-500 to-cyan-500 rounded-full flex items-center justify-center mx-auto mb-4 float-icon shadow-lg"
          >
            <Icon icon="tabler:box" class="w-8 h-8 text-white" />
          </div>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">Free Shipping</h3>
          <p class="text-gray-600 text-sm">
            Free delivery on orders over $99. Express shipping available nationwide.
          </p>
          <div
            class="mt-4 text-xs text-blue-600 font-medium bg-blue-50 px-3 py-1 rounded-full inline-block"
          >
            2-3 Business Days
          </div>
        </div>

        <div
          class="service-card group bg-white/80 backdrop-blur-sm border border-gray-200 rounded-2xl p-6 text-center hover:bg-white hover:border-green-300 hover:shadow-xl transition-all duration-300 hover:transform hover:scale-105"
        >
          <div
            class="w-16 h-16 bg-gradient-to-r from-green-500 to-emerald-500 rounded-full flex items-center justify-center mx-auto mb-4 float-icon shadow-lg"
          >
            <Icon icon="fluent:payment-20-regular" class="w-8 h-8 text-white" />
          </div>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">Flexible Payments</h3>
          <p class="text-gray-600 text-sm">
            Split your purchase into 4 interest-free payments. Pay over time with ease.
          </p>
          <div
            class="mt-4 text-xs text-green-600 font-medium bg-green-50 px-3 py-1 rounded-full inline-block"
          >
            0% Interest
          </div>
        </div>

        <div
          class="service-card group bg-white/80 backdrop-blur-sm border border-gray-200 rounded-2xl p-6 text-center hover:bg-white hover:border-purple-300 hover:shadow-xl transition-all duration-300 hover:transform hover:scale-105"
        >
          <div
            class="w-16 h-16 bg-gradient-to-r from-purple-500 to-pink-500 rounded-full flex items-center justify-center mx-auto mb-4 float-icon shadow-lg"
          >
            <Icon icon="material-symbols:contact-support-rounded" class="w-8 h-8 text-white" />
          </div>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">24/7 Support</h3>
          <p class="text-gray-600 text-sm">
            Expert customer service available around the clock via chat, phone, or email.
          </p>
          <div
            class="mt-4 text-xs text-purple-600 font-medium bg-purple-50 px-3 py-1 rounded-full inline-block"
          >
            Always Available
          </div>
        </div>
        <div
          class="service-card group bg-white/80 backdrop-blur-sm border border-gray-200 rounded-2xl p-6 text-center hover:bg-white hover:border-orange-300 hover:shadow-xl transition-all duration-300 hover:transform hover:scale-105"
        >
          <div
            class="w-16 h-16 bg-gradient-to-r from-orange-500 to-red-500 rounded-full flex items-center justify-center mx-auto mb-4 float-icon shadow-lg"
          >
            <Icon icon="ri:loop-left-fill" class="w-8 h-8 text-white" />
          </div>
          <h3 class="text-xl font-semibold text-gray-800 mb-2">Easy Returns</h3>
          <p class="text-gray-600 text-sm">
            30-day hassle-free returns. Not satisfied? Get your money back, no questions asked.
          </p>
          <div
            class="mt-4 text-xs text-orange-600 font-medium bg-orange-50 px-3 py-1 rounded-full inline-block"
          >
            30-Day Policy
          </div>
        </div>
      </div>

      <div class="mt-12 flex flex-wrap justify-center items-center gap-8 opacity-70">
        <div class="flex items-center space-x-2">
          <Icon icon="lets-icons:check-fill" class="w-5 h-5 text-green-600" />
          <span class="text-gray-600 text-sm">SSL Secured</span>
        </div>
        <div class="flex items-center space-x-2">
          <Icon icon="lets-icons:check-fill" class="w-5 h-5 text-blue-600" />
          <span class="text-gray-600 text-sm">Money Back Guarantee</span>
        </div>
        <div class="flex items-center space-x-2">
          <Icon icon="lets-icons:check-fill" class="w-5 h-5 text-purple-600" />
          <span class="text-gray-600 text-sm">Authorized Retailer</span>
        </div>
        <div class="flex items-center space-x-2">
          <Icon icon="iconoir:star-solid" class="w-5 h-5 text-yellow-600" />
          <span class="text-gray-600 text-sm">5 Star Rated</span>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped></style>
