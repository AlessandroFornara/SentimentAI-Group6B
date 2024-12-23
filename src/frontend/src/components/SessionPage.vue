<template>
  <div class="session-page">
    <div class="upper-left">
      <button @click="goBack">Back</button>
    </div>

    <h1 class="title">Welcome to the session</h1>
    <div class="image-container">
      <button
          v-for="(image, index) in images"
          :key="index"
          class="bubble"
          @click="selectImage(index)"
      >
        <img :src="image" alt="'Image ' + (index + 1)" />
      </button>
    </div>

    <div class="bottom-center">
      <button @click="freeModeOption">Free Mode Option</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

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

    const data = await response.json();
    console.log('Risposta dal server:', data);

    // Converti Base64 in un formato utilizzabile
    images.value = data.map((item) => `data:${item.contentType};base64,${item.base64Data}`);
  } catch (error) {
    console.error('Errore durante il caricamento delle immagini:', error);
  }
}

function selectImage(index) {
  const selectedImage = images.value[index];
  console.log(`Selected Image: ${selectedImage}`);
  router.push({ name: 'AudioPage', query: { background: selectedImage } });
}

function freeModeOption() {
  console.log('Free Mode Option Selected');
  router.push({name: 'AudioPage'});
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
  font-family: Arial, sans-serif;
  margin: 0 auto;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  background-color: #87ceeb;
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
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid #ccc;
  background-color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.bubble img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.bubble:hover {
  transform: scale(1.1);
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
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
  font-size: 18px;
  font-family: Copperplate;
  background-color: mediumpurple;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.bottom-center button:hover {
  background-color: indigo;
}

.title {
  font-size: 60px;
  font-weight: bold;
  margin: 0;
  font-family: Copperplate;
  color: mediumpurple;
}

.upper-left {
  position: fixed;
  top: 20px;
  left: 20px;
  display: flex;
  width: 100%;
}

.upper-left button {
  padding: 15px 30px;
  font-size: 18px;
  font-family: Copperplate;
  background-color: mediumpurple;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.upper-left button:hover {
  background-color: indigo;
}
</style>
