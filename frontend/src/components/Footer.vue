<script setup>
import { Icon } from '@iconify/vue';
</script>

<template>
  <footer class="footer sm:footer-horizontal bg-black text-slate-200 p-10">
    <aside>
      <Icon icon="tdesign:apple-filled" width="50" height="50" />
      <p>
        IP24 TT2.
        <br />
        INT221 Integrated Project TT2
      </p>
    </aside>
    <nav>
      <h6 class="footer-title">Services</h6>
      <a class="link link-hover">Branding</a>
      <a class="link link-hover">Design</a>
      <a class="link link-hover">Marketing</a>
      <a class="link link-hover">Advertisement</a>
    </nav>
    <nav>
      <h6 class="footer-title">Company</h6>
      <a class="link link-hover">About us</a>
      <a class="link link-hover">Contact</a>
      <a class="link link-hover">Jobs</a>
      <a class="link link-hover">Press kit</a>
    </nav>
    <nav>
      <h6 class="footer-title">Legal</h6>
      <a class="link link-hover">Terms of use</a>
      <a class="link link-hover">Privacy policy</a>
      <a class="link link-hover">Cookie policy</a>
    </nav>
  </footer>
</template>

<style scoped></style>
