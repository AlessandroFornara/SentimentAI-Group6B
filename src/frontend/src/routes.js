import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/components/HomePage.vue';
import SessionPage from '@/components/SessionPage.vue';
import AudioPage from '@/components/AudioPage.vue';
import LoginPage from '@/components/LoginPage.vue';

const routes = [
    { path: '/', name: 'LoginPage', component: LoginPage }, // Pagina di Login come predefinita
    { path: '/home', name: 'HomePage', component: HomePage }, // HomePage spostata su '/home'
    { path: '/session', name: 'SessionPage', component: SessionPage },
    { path: '/audio', name: 'AudioPage', component: AudioPage }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;

