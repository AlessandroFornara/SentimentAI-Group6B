import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '@/components/HomePage.vue';
import SessionPage from '@/components/SessionPage.vue';
import AudioPage from '@/components/AudioPage.vue';
import LoginPage from '@/components/LoginPage.vue';
import HistoryPage from "@/components/HistoryPage.vue";
import ProfilePage from "@/components/ProfilePage.vue";
import ResultPage from "@/components/ResultPage.vue";

const routes = [
    { path: '/', name: 'LoginPage', component: LoginPage }, // Pagina di Login come predefinita
    { path: '/home', name: 'HomePage', component: HomePage }, // HomePage spostata su '/home'
    { path: '/session', name: 'SessionPage', component: SessionPage },
    { path: '/audio', name: 'AudioPage', component: AudioPage },
    { path: '/history', name: 'HistoryPage', component: HistoryPage },
    { path: '/profile', name: 'ProfilePage', component: ProfilePage },
    { path: '/result', name: 'ResultPage', component: ResultPage },
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

export default router;

