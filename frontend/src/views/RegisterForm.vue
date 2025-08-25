<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { httpRequest } from "@/utils/fetchUtils";

const router = useRouter();

// accounttype
const accountType = ref("BUYER");


const form = ref({
    nickName: "",
    email: "",
    password: "",
    fullName: "",
    userType: "",
    mobile: "",
    bankAccountNumber: "",
    bankName: "",
    nationalIdNumber: "",
    nationalFrontFile: null,
    nationalBackFile: null,
});


const isSubmitting = ref(false);
const errorMessage = ref("");
const successMessage = ref("");


const handleFileUpload = (event, field) => {
    form.value[field] = event.target.files[0];
};


const handleSubmit = async () => {
    isSubmitting.value = true;
    errorMessage.value = "";
    successMessage.value = "";

    try {
    // use FormData Only (text + file)
    const payload = new FormData();
    payload.append("accountType", accountType.value);
    payload.append("nickName", form.value.nickName);
    payload.append("email", form.value.email);
    payload.append("password", form.value.password);
    payload.append("fullName", form.value.fullName);
    payload.append("userType", accountType.value);

    if (accountType.value === "SELLER") {
        payload.append("phoneNumber", form.value.mobile);
        payload.append("bankAccount", form.value.bankAccountNumber);
        payload.append("bankName", form.value.bankName);
        payload.append("idCardNumber", form.value.nationalIdNumber);

        if (form.value.nationalFrontFile) {
        payload.append("idCardImageFront", form.value.nationalFrontFile);
        }
        if (form.value.nationalBackFile) {
        payload.append("idCardImageBack", form.value.nationalBackFile);
        }
    }

    const res = await httpRequest("POST", "v2/users/register", payload);

    successMessage.value = "Register success!";
    console.log("register response:", res);

    // redirect 
    setTimeout(() => {
        router.push("/");
    }, 1500);
    } catch (err) {
    errorMessage.value = err.message || "Registration failed!";
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
        <input
            type="radio"
            value="BUYER"
            v-model="accountType"
            class="radio radio-primary"
        />
        Buyer
        </label>
        <label class="flex items-center gap-2">
        <input
            type="radio"
            value="SELLER"
            v-model="accountType"
            class="radio radio-primary"
        />
        Seller
        </label>
    </div>

    <form @submit.prevent="handleSubmit" class="space-y-4">
        <!-- buyer + seller -->
        <div>
            <label class="block font-medium">Full name</label>
                <input v-model="form.fullName" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">NickName</label>
                <input v-model="form.nickName" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">Email</label>
                <input v-model="form.email" type="email" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">Password</label>
                <input v-model="form.password" type="password" class="input input-bordered w-full" required />
        </div>
    
        <!-- seller only -->
        <template v-if="accountType === 'SELLER'">
        <div>
            <label class="block font-medium">Mobile</label>
                <input v-model="form.mobile" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">Bank Account No</label>
                <input v-model="form.bankAccountNumber" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">Bank Name</label>
                <input v-model="form.bankName" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">National ID No</label>
                <input v-model="form.nationalIdNumber" type="text" class="input input-bordered w-full" required />
        </div>
        <div>
            <label class="block font-medium">Card Photo Front</label>
                <input type="file" @change="e => handleFileUpload(e, 'nationalFrontFile')" accept="image/*" />
        </div>
        <div>
            <label class="block font-medium">Card Photo Back</label>
                <input type="file" @change="e => handleFileUpload(e, 'nationalBackFile')" accept="image/*" />
        </div>
        </template>

        <button
        type="submit"
        class="btn btn-primary w-full"
        :disabled="isSubmitting"
        >
        {{ isSubmitting ? "Registering..." : "Register" }}
        </button>
    </form>

    <p v-if="errorMessage" class="text-red-500 mt-4">{{ errorMessage }}</p>
    <p v-if="successMessage" class="text-green-500 mt-4">{{ successMessage }}</p>
    </div>
</template>

<style scoped>
input[type="file"] {
    display: block;
    margin-top: 4px;
}
</style>
