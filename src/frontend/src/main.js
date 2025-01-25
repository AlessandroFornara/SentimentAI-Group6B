import { createApp } from 'vue'
import App from './App.vue'
import router from './routes'; // Importa il index
import { createI18n } from 'vue-i18n';
import en from './languages/en.json';
import it from './languages/it.json';
import '@/styles/shared-styles.css';

export const i18n = createI18n({
    locale: 'en-US',
    fallbackLocale: 'en-US',
    messages: {
        'en-US': en,
        'it-IT': it,
    },
});

export async function changeLanguage(lang) {
    i18n.global.locale = lang;
    try {
        const response = await fetch('/api/worker/change_language', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(lang)
        });

        if (!response.ok) {
            console.error('Errore durante la modifica della lingua sul server:', response.statusText);
        }
    } catch (error) {
        console.error('Errore durante la richiesta al server:', error);
    }
}

// Crea l'app Vue e usa il index
createApp(App).use(router).use(i18n).mount('#app');