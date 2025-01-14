import { createApp } from 'vue'
import App from './App.vue'
import router from './routes'; // Importa il index
import './assets/styles/shared-styles.css';

// Crea l'app Vue e usa il index
createApp(App).use(router).mount('#app');