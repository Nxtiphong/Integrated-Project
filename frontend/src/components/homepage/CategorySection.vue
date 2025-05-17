<script setup>
import { ref, reactive, computed } from 'vue';

const activeCategory = ref('all');
const searchQuery = ref('');
const isFilterOpen = ref(false);
const sortOption = ref('newest');
const priceRange = reactive({ min: 0, max: 2000 });
const selectedBrands = ref([]);
const cartItems = ref(0);

// Phone data
const phones = reactive([
  {
    id: 1,
    name: 'iPhone 15 Pro',
    brand: 'Apple',
    price: 999,
    specs: {
      display: '6.1" Super Retina XDR',
      processor: 'A17 Pro',
      camera: '48MP Triple Camera',
      battery: '3200 mAh',
      storage: '128GB'
    },
    colors: ['#54514D', '#F1F2ED', '#3D3C38', '#F9E5C9'],
    rating: 4.8,
    inStock: true,
    isNew: true,
    category: 'premium'
  },
  {
    id: 2,
    name: 'Samsung Galaxy S23',
    brand: 'Samsung',
    price: 899,
    specs: {
      display: '6.8" Dynamic AMOLED 2X',
      processor: 'Snapdragon 8 Gen 2',
      camera: '200MP Quad Camera',
      battery: '5000 mAh',
      storage: '256GB'
    },
    colors: ['#1F1F1F', '#F0EDE6', '#7A7D81', '#E4D9C1'],
    rating: 4.7,
    inStock: true,
    isNew: true,
    category: 'premium'
  },
  {
    id: 3,
    name: 'Google Pixel 8',
    brand: 'Google',
    price: 699,
    specs: {
      display: '6.2" OLED',
      processor: 'Google Tensor G3',
      camera: '50MP Dual Camera',
      battery: '4500 mAh',
      storage: '128GB'
    },
    colors: ['#738E93', '#EAECEC', '#A1867E', '#BAC0BB'],
    rating: 4.6,
    inStock: true,
    isNew: true,
    category: 'premium'
  },
  {
    id: 4,
    name: 'OnePlus 11',
    brand: 'OnePlus',
    price: 699,
    specs: {
      display: '6.7" AMOLED',
      processor: 'Snapdragon 8 Gen 2',
      camera: '50MP Triple Camera',
      battery: '5000 mAh',
      storage: '128GB'
    },
    colors: ['#000000', '#FFFFFF'],
    rating: 4.5,
    inStock: true,
    isNew: false,
    category: 'premium'
  },
  {
    id: 5,
    name: 'Xiaomi 13',
    brand: 'Xiaomi',
    price: 599,
    specs: {
      display: '6.36" AMOLED',
      processor: 'Snapdragon 8 Gen 2',
      camera: '50MP Triple Camera',
      battery: '4500 mAh',
      storage: '128GB'
    },
    colors: ['#000000', '#FFFFFF', '#1E90FF'],
    rating: 4.4,
    inStock: true,
    isNew: false,
    category: 'midrange'
  },
  {
    id: 6,
    name: 'Nothing Phone (2)',
    brand: 'Nothing',
    price: 599,
    specs: {
      display: '6.7" OLED',
      processor: 'Snapdragon 8+ Gen 1',
      camera: '50MP Dual Camera',
      battery: '4700 mAh',
      storage: '128GB'
    },
    colors: ['#000000', '#FFFFFF'],
    rating: 4.3,
    inStock: true,
    isNew: false,
    category: 'midrange'
  },
  {
    id: 7,
    name: 'Samsung Galaxy A54',
    brand: 'Samsung',
    price: 449,
    specs: {
      display: '6.4" Super AMOLED',
      processor: 'Exynos 1380',
      camera: '50MP Triple Camera',
      battery: '5000 mAh',
      storage: '128GB'
    },
    colors: ['#000000', '#FFFFFF', '#90EE90'],
    rating: 4.2,
    inStock: true,
    isNew: false,
    category: 'midrange'
  },
  {
    id: 8,
    name: 'Motorola Edge 40',
    brand: 'Motorola',
    price: 399,
    specs: {
      display: '6.55" P-OLED',
      processor: 'Dimensity 8020',
      camera: '50MP Dual Camera',
      battery: '4400 mAh',
      storage: '256GB'
    },
    colors: ['#000000', '#4682B4', '#708090'],
    rating: 4.1,
    inStock: true,
    isNew: false,
    category: 'midrange'
  },
  {
    id: 9,
    name: 'Redmi Note 12',
    brand: 'Xiaomi',
    price: 249,
    specs: {
      display: '6.67" AMOLED',
      processor: 'Snapdragon 685',
      camera: '50MP Triple Camera',
      battery: '5000 mAh',
      storage: '128GB'
    },
    colors: ['#000000', '#FFFFFF', '#1E90FF'],
    rating: 4.0,
    inStock: true,
    isNew: false,
    category: 'budget'
  },
  {
    id: 10,
    name: 'Samsung Galaxy A14',
    brand: 'Samsung',
    price: 199,
    specs: {
      display: '6.6" LCD',
      processor: 'MediaTek Helio G80',
      camera: '50MP Triple Camera',
      battery: '5000 mAh',
      storage: '64GB'
    },
    colors: ['#000000', '#FFFFFF', '#90EE90'],
    rating: 3.9,
    inStock: true,
    isNew: false,
    category: 'budget'
  }
]);

// Available brands
const brands = [
  { id: 1, name: 'Apple' },
  { id: 2, name: 'Samsung' },
  { id: 3, name: 'Google' },
  { id: 4, name: 'OnePlus' },
  { id: 5, name: 'Xiaomi' },
  { id: 6, name: 'Nothing' },
  { id: 7, name: 'Motorola' }
];

// Categories
const categories = [
  { id: 'all', name: 'All Phones' },
  { id: 'premium', name: 'Premium' },
  { id: 'midrange', name: 'Mid-range' },
  { id: 'budget', name: 'Budget' },
  { id: 'new', name: 'New Arrivals' }
];

// Filter phones based on active filters
const filteredPhones = computed(() => {
  return phones.filter(phone => {
    // Filter by category
    if (activeCategory.value === 'new' && !phone.isNew) return false;
    if (activeCategory.value !== 'all' && activeCategory.value !== 'new' && phone.category !== activeCategory.value) return false;
    
    // Filter by search query
    if (searchQuery.value && !phone.name.toLowerCase().includes(searchQuery.value.toLowerCase()) && 
        !phone.brand.toLowerCase().includes(searchQuery.value.toLowerCase())) return false;
    
    // Filter by price range
    if (phone.price < priceRange.min || phone.price > priceRange.max) return false;
    
    // Filter by selected brands
    if (selectedBrands.value.length > 0 && !selectedBrands.value.includes(phone.brand)) return false;
    
    return true;
  });
});

// Sort phones based on selected option
const sortedPhones = computed(() => {
  const phones = [...filteredPhones.value];
  
  switch (sortOption.value) {
    case 'price-low':
      return phones.sort((a, b) => a.price - b.price);
    case 'price-high':
      return phones.sort((a, b) => b.price - a.price);
    case 'rating':
      return phones.sort((a, b) => b.rating - a.rating);
    case 'newest':
    default:
      return phones.sort((a, b) => b.isNew - a.isNew);
  }
});

// Toggle filter panel
const toggleFilter = () => {
  isFilterOpen.value = !isFilterOpen.value;
};

// Toggle brand selection
const toggleBrand = (brand) => {
  const index = selectedBrands.value.indexOf(brand);
  if (index === -1) {
    selectedBrands.value.push(brand);
  } else {
    selectedBrands.value.splice(index, 1);
  }
};

// Format price
const formatPrice = (price) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    minimumFractionDigits: 0
  }).format(price);
};

// Add to cart
const addToCart = (phone) => {
  cartItems.value++;
  // Show notification or update cart logic would go here
};
</script>

<template>
  <div class="min-h-screen bg-gray-50 text-gray-900">
    <!-- Header -->
    

    <!-- Search (Mobile) -->
    <div class="md:hidden bg-white p-4 shadow-sm">
      <div class="relative">
        <input 
          v-model="searchQuery"
          type="text" 
          placeholder="Search phones..." 
          class="w-full py-2 px-4 pr-10 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        />
        <button class="absolute right-3 top-1/2 transform -translate-y-1/2">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5 text-gray-400">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.3-4.3"></path>
          </svg>
        </button>
      </div>
    </div>

    <!-- Main Content -->
    <main class="container mx-auto px-4 py-6">
      <!-- Categories -->
      <div class="overflow-x-auto pb-2 mb-6">
        <div class="flex space-x-2 min-w-max">
          <button 
            v-for="category in categories" 
            :key="category.id"
            @click="activeCategory = category.id"
            class="px-4 py-2 rounded-full text-sm font-medium transition-colors whitespace-nowrap"
            :class="activeCategory === category.id ? 'bg-blue-500 text-white' : 'bg-white text-gray-700 hover:bg-gray-100'"
          >
            {{ category.name }}
          </button>
        </div>
      </div>
      
      <!-- Filter and Sort Controls -->
      <div class="flex flex-wrap justify-between items-center mb-6 gap-4">
        <div class="flex items-center">
          <button 
            @click="toggleFilter"
            class="flex items-center space-x-2 px-4 py-2 bg-white rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors"
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
              <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"></polygon>
            </svg>
            <span>Filter</span>
          </button>
        </div>
        
        <div class="flex items-center space-x-2">
          <span class="text-sm text-gray-500">Sort by:</span>
          <select 
            v-model="sortOption"
            class="px-3 py-2 bg-white rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          >
            <option value="newest">Newest</option>
            <option value="price-low">Price: Low to High</option>
            <option value="price-high">Price: High to Low</option>
            <option value="rating">Rating</option>
          </select>
        </div>
      </div>
      
      <!-- Filter Panel -->
      <div 
        v-if="isFilterOpen"
        class="bg-white rounded-lg shadow-md p-4 mb-6 border border-gray-200"
      >
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Price Range -->
          <div>
            <h3 class="font-medium mb-3">Price Range</h3>
            <div class="flex items-center space-x-4">
              <div class="flex-1">
                <label class="text-xs text-gray-500 mb-1 block">Min</label>
                <input 
                  v-model="priceRange.min"
                  type="number" 
                  min="0" 
                  step="50"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
              <div class="flex-1">
                <label class="text-xs text-gray-500 mb-1 block">Max</label>
                <input 
                  v-model="priceRange.max"
                  type="number" 
                  min="0" 
                  step="50"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>
          </div>
          
          <!-- Brands -->
          <div>
            <h3 class="font-medium mb-3">Brands</h3>
            <div class="grid grid-cols-2 gap-2">
              <div 
                v-for="brand in brands" 
                :key="brand.id"
                class="flex items-center"
              >
                <label class="flex items-center space-x-2 cursor-pointer">
                  <input 
                    type="checkbox" 
                    :checked="selectedBrands.includes(brand.name)"
                    @change="toggleBrand(brand.name)"
                    class="rounded text-blue-500 focus:ring-blue-500"
                  />
                  <span>{{ brand.name }}</span>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Results Count -->
      <div class="mb-6">
        <p class="text-sm text-gray-500">Showing {{ sortedPhones.length }} phones</p>
      </div>
      
      <!-- Phone Grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <div 
          v-for="phone in sortedPhones" 
          :key="phone.id"
          class="bg-white rounded-lg overflow-hidden border border-gray-200 hover:shadow-md transition-shadow"
        >
          <!-- Phone Visual Representation (No Image) -->
          <div class="h-48 relative overflow-hidden">
            <!-- Abstract Phone Shape -->
            <div 
              class="absolute inset-0 flex items-center justify-center"
              :class="`bg-gradient-to-br from-gray-100 to-gray-200`"
            >
              <div class="w-24 h-40 rounded-xl border-4 border-gray-300 relative flex items-center justify-center">
                <div class="absolute top-2 w-8 h-1 bg-gray-300 rounded-full"></div>
                <div 
                  class="w-16 h-16 rounded-full"
                  :style="{ backgroundColor: phone.colors[0] }"
                ></div>
              </div>
            </div>
            
            <!-- New Badge -->
            <div 
              v-if="phone.isNew"
              class="absolute top-2 left-2 bg-blue-500 text-white text-xs font-bold px-2 py-1 rounded-md"
            >
              NEW
            </div>
            
            <!-- Brand Badge -->
            <div class="absolute bottom-2 left-2 bg-white/80 backdrop-blur-sm text-xs font-medium px-2 py-1 rounded-md">
              {{ phone.brand }}
            </div>
          </div>
          
          <!-- Phone Details -->
          <div class="p-4">
            <h3 class="font-bold text-lg mb-1">{{ phone.name }}</h3>
            
            <!-- Rating -->
            <div class="flex items-center mb-2">
              <div class="flex">
                <svg 
                  v-for="i in 5" 
                  :key="i"
                  xmlns="http://www.w3.org/2000/svg" 
                  :class="i <= Math.floor(phone.rating) ? 'text-yellow-400' : 'text-gray-300'"
                  viewBox="0 0 24 24" 
                  fill="currentColor" 
                  class="w-4 h-4"
                >
                  <path fill-rule="evenodd" d="M10.788 3.21c.448-1.077 1.976-1.077 2.424 0l2.082 5.007 5.404.433c1.164.093 1.636 1.545.749 2.305l-4.117 3.527 1.257 5.273c.271 1.136-.964 2.033-1.96 1.425L12 18.354 7.373 21.18c-.996.608-2.231-.29-1.96-1.425l1.257-5.273-4.117-3.527c-.887-.76-.415-2.212.749-2.305l5.404-.433 2.082-5.006z" clip-rule="evenodd" />
                </svg>
              </div>
              <span class="text-xs text-gray-500 ml-1">{{ phone.rating }}/5</span>
            </div>
            
            <!-- Price -->
            <div class="text-xl font-bold mb-3">{{ formatPrice(phone.price) }}</div>
            
            <!-- Key Specs -->
            <div class="space-y-1 mb-4">
              <div class="flex items-center text-sm">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 mr-2 text-gray-400">
                  <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
                  <line x1="8" x2="16" y1="21" y2="21"></line>
                  <line x1="12" x2="12" y1="17" y2="21"></line>
                </svg>
                <span class="text-gray-600">{{ phone.specs.display }}</span>
              </div>
              <div class="flex items-center text-sm">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 mr-2 text-gray-400">
                  <rect x="4" y="4" width="16" height="16" rx="2"></rect>
                  <rect x="9" y="9" width="6" height="6"></rect>
                  <path d="M9 2v2"></path>
                  <path d="M15 2v2"></path>
                  <path d="M9 22v-2"></path>
                  <path d="M15 22v-2"></path>
                  <path d="M2 9h2"></path>
                  <path d="M2 15h2"></path>
                  <path d="M22 9h-2"></path>
                  <path d="M22 15h-2"></path>
                </svg>
                <span class="text-gray-600">{{ phone.specs.processor }}</span>
              </div>
              <div class="flex items-center text-sm">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-4 h-4 mr-2 text-gray-400">
                  <path d="M14.5 4h-5L7 7H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2h-3l-2.5-3z"></path>
                  <circle cx="12" cy="13" r="3"></circle>
                </svg>
                <span class="text-gray-600">{{ phone.specs.camera }}</span>
              </div>
            </div>
            
            <!-- Available Colors -->
            <div class="flex space-x-2 mb-4">
              <div 
                v-for="(color, index) in phone.colors" 
                :key="index"
                class="w-6 h-6 rounded-full border border-gray-300"
                :style="{ backgroundColor: color }"
              ></div>
            </div>
            
            <!-- Action Buttons -->
            <div class="flex space-x-2">
              <button 
                @click="addToCart(phone)"
                class="flex-1 bg-blue-500 hover:bg-blue-600 text-white py-2 rounded-lg font-medium transition-colors"
              >
                Add to Cart
              </button>
              <button class="w-10 h-10 flex items-center justify-center border border-gray-300 rounded-lg hover:bg-gray-100 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                  <path d="M20.84 4.61a5.5 5.5 0 0 0-7.78 0L12 5.67l-1.06-1.06a5.5 5.5 0 0 0-7.78 7.78l1.06 1.06L12 21.23l7.78-7.78 1.06-1.06a5.5 5.5 0 0 0 0-7.78z"></path>
                </svg>
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Empty State -->
      <div v-if="sortedPhones.length === 0" class="bg-white rounded-lg p-8 text-center">
        <div class="w-16 h-16 mx-auto mb-4 bg-gray-200 rounded-full flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-8 h-8 text-gray-400">
            <circle cx="11" cy="11" r="8"></circle>
            <path d="m21 21-4.3-4.3"></path>
          </svg>
        </div>
        <h3 class="text-lg font-medium mb-2">No phones found</h3>
        <p class="text-gray-500 mb-4">Try adjusting your filters or search query</p>
        <button 
          @click="activeCategory = 'all'; searchQuery = ''; selectedBrands = []; priceRange.min = 0; priceRange.max = 2000;"
          class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
        >
          Reset Filters
        </button>
      </div>
    </main>
    
    <!-- Footer -->
    <footer class="bg-white border-t border-gray-200 py-8">
      <div class="container mx-auto px-4">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div>
            <h3 class="font-bold text-lg mb-4">PhoneStore</h3>
            <p class="text-gray-600 mb-4">Your one-stop destination for all mobile phones. Find the latest models at competitive prices.</p>
            <div class="flex space-x-4">
              <a href="#" class="text-gray-400 hover:text-gray-600 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                  <path d="M18 2h-3a5 5 0 0 0-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 0 1 1-1h3z"></path>
                </svg>
              </a>
              <a href="#" class="text-gray-400 hover:text-gray-600 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                  <rect x="2" y="2" width="20" height="20" rx="5" ry="5"></rect>
                  <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z"></path>
                  <line x1="17.5" y1="6.5" x2="17.51" y2="6.5"></line>
                </svg>
              </a>
              <a href="#" class="text-gray-400 hover:text-gray-600 transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5">
                  <path d="M22 4s-.7 2.1-2 3.4c1.6 10-9.4 17.3-18 11.6 2.2.1 4.4-.6 6-2C3 15.5.5 9.6 3 5c2.2 2.6 5.6 4.1 9 4-.9-4.2 4-6.6 7-3.8 1.1 0 3-1.2 3-1.2z"></path>
                </svg>
              </a>
            </div>
          </div>
          
          <div>
            <h3 class="font-bold text-lg mb-4">Quick Links</h3>
            <ul class="space-y-2">
              <li><a href="#" class="text-gray-600 hover:text-blue-500 transition-colors">Home</a></li>
              <li><a href="#" class="text-gray-600 hover:text-blue-500 transition-colors">Shop</a></li>
              <li><a href="#" class="text-gray-600 hover:text-blue-500 transition-colors">About Us</a></li>
              <li><a href="#" class="text-gray-600 hover:text-blue-500 transition-colors">Contact</a></li>
              <li><a href="#" class="text-gray-600 hover:text-blue-500 transition-colors">Support</a></li>
            </ul>
          </div>
          
          <div>
            <h3 class="font-bold text-lg mb-4">Contact Us</h3>
            <ul class="space-y-2">
              <li class="flex items-start">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5 mr-2 text-gray-400">
                  <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                  <circle cx="12" cy="10" r="3"></circle>
                </svg>
                <span class="text-gray-600">123 Tech Street, Digital City, 10001</span>
              </li>
              <li class="flex items-start">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5 mr-2 text-gray-400">
                  <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path>
                </svg>
                <span class="text-gray-600">+1 (555) 123-4567</span>
              </li>
              <li class="flex items-start">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-5 h-5 mr-2 text-gray-400">
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path>
                  <polyline points="22,6 12,13 2,6"></polyline>
                </svg>
                <span class="text-gray-600">support@phonestore.com</span>
              </li>
            </ul>
          </div>
        </div>
        
        <div class="border-t border-gray-200 mt-8 pt-6 text-center">
          <p class="text-gray-500 text-sm">© 2023 PhoneStore. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<style scoped>
/* Custom styles */
</style>