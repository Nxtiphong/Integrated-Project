import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    specPattern: 'cypress/e2e/**/*.{cy,spec}.{js,jsx,ts,tsx}',
    // baseUrl: 'http://ip24tt2.sit.kmutt.ac.th',
    baseUrl: 'http://localhost:5173/tt2',
    baseAPI: 'http://ip24tt2.sit.kmutt.ac.th:8080/itb-mshop',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
