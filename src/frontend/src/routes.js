import { createRouter, createWebHistory } from 'vue-router';

// Importa le tue pagine o componenti
import HomePage from './components/HomePage.vue'; // La tua pagina attuale
import SessionPage from './components/SessionPage.vue'; // La pagina di sessione

// Definisci le rotte
const routes = [
    { path: '/', name: 'HomePage', component: HomePage }, // Pagina iniziale
    { path: '/SessionPage', name: 'SessionPage', component: SessionPage } // Pagina sessione
];

// Crea il index
const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

export default router;
