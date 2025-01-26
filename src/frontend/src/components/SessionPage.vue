<template>
  <div class="session-page">
    <div class="upper-left">
      <button @click="goBack">Back</button>
    </div>
    <div class="top-right">
      <div class="language-buttons">
        <button @click="changeLanguage('en-US')">English</button>
        <button @click="changeLanguage('it-IT')">Italiano</button>
      </div>
    </div>

    <h1 class="title">{{ $t('titleSessionPage') }}</h1>
    <h2 style="font-size: 20px; font-weight: bold">{{ $t('subtitleSessionPage') }}</h2>
    <div>
      <img src="@/assets/mic_emotino_noBack.png" width="150" alt="P">
    </div>
    <div class="image-container">
      <button
          v-for="(image, index) in images"
          :key="image.name"
          class="bubble"
          @click="selectImage(index)"
      >
        <img :src="image.src" :alt="image.name" />
      </button>
    </div>

    <div class="bottom-center">
      <button @click="freeModeOption">{{ $t('freeModeButton') }}</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import freeModeImage from '@/assets/OfficeWorkerThinking-NoBG.png';
import { changeLanguage } from '@/main';

const router = useRouter();
const images = ref([]);

// Funzione per caricare le immagini dal server
async function loadImages() {
  try {
    const response = await fetch('/api/worker/create_session', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Errore durante il caricamento delle immagini.');
    }

    const responseData = await response.json();
    console.log('Risposta dal server:', responseData);

    // Converti Base64 in un formato utilizzabile
    images.value = responseData.map((item) => ({
      name: item.name,
      src: `data:${item.contentType};base64,${item.data}`
    }));
  } catch (error) {
    console.error('Errore durante il caricamento delle immagini:', error);
  }
}

async function selectImage(index) {
  const selectedImage = images.value[index];
  console.log(selectedImage.name);
  //console.log(`Selected Image: ${selectedImage}`);
  const response = await fetch('/api/worker/start_session', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
    body: JSON.stringify(selectedImage.name),
  });
  if (!response.ok) {
    throw new Error('Errore durante l\'avvio della sessione.');
  }else{
    await router.push({name: 'AudioPage', query: {background: selectedImage.src}});
  }
}

function freeModeOption() {
  console.log('Free Mode Option Selected');
  router.push({name: 'AudioPage', query: {background: freeModeImage, mask: false}});
}

function goBack() {
  console.log('Navigating back to HomePage...');
  router.push('/home');
}

onMounted(() => {
  loadImages();
});
</script>

<style scoped>
/* Stile generale della pagina */
.session-page {
  position: relative;
  text-align: center;
  margin: 0 auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  overflow: hidden;
}

.image-container {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
  height: 70%;
  bottom: 40px;
  position: relative;
}

.bubble {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  overflow: hidden;
  border: 3px solid #ccc;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
  cursor: pointer;
  animation: float 4s ease-in-out infinite;
}

.bubble img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.bubble:hover {
  transform: scale(1.1);
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.2);
  border-color: #1666cb;
}

/* Animazione fluttuante */
@keyframes float {
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
  100% {
    transform: translateY(0);
  }
}

.bottom-center {
  position: fixed;
  bottom: 20px;
  display: flex;
  justify-content: center;
  width: 100%;
}

.bottom-center button {
  padding: 15px 30px;
  font-size: 15px;
  background-color: #1666cb;
  font-family: 'Lobster', cursive;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.bottom-center button:hover {
  transform: scale(1.05);
  background-color: #0f4da8;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.bottom-center button:active {
  transform: scale(0.95);
  background-color: #0a3680;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.title {
  font-size: 40px;
  font-weight: bold;
  margin: 0;
}

</style>
