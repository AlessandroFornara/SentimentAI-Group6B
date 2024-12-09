<template>
  <div class="session-page">


    <!-- Pulsante "Back" in alto a sinistra -->
    <div class = "upper-left">
      <button @click="goBack">Back</button>
    </div>

    <h1 class="title">Welcome to the session</h1>
    <div class="image-container">
      <!-- Ogni immagine è un bottone selezionabile -->
      <button
          v-for="(image, index) in images"
          :key="index"
          class="bubble"
          @click="selectImage(index)"
      >
        <img :src="image" :alt="'Image ' + (index + 1)" />
      </button>
    </div>

    <!-- Pulsante in basso al centro -->
    <div class="bottom-center">
      <button @click="freeModeOption">Free Mode Option</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // Importa il router

const router = useRouter(); // Ottieni l'istanza del router

// Elenco delle immagini
const images = ref([
  'path-to-image1.jpg',
  'path-to-image2.jpg',
  'path-to-image3.jpg',
  'path-to-image4.jpg',
  'path-to-image5.jpg'
]);

// Funzione per selezionare un'immagine
function selectImage(index) {
  console.log(`Selected Image ${index + 1}`);
  // Aggiungi la logica per gestire la selezione dell'immagine
}


// Funzione per il pulsante Free Mode Option
function freeModeOption() {
  console.log('Free Mode Option Selected');
  // Aggiungi logica per il pulsante Free Mode
}

// Funzione per tornare alla homepage
function goBack() {
  console.log('Navigating back to HomePage...');
  router.push('/'); // Naviga alla HomePage
}
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

/* Contenitore per le immagini */
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

/* Ogni immagine a forma di bolla */
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

/* Effetto al passaggio del mouse */
.bubble:hover {
  transform: scale(1.1);
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

/* Pulsante in basso al centro */
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