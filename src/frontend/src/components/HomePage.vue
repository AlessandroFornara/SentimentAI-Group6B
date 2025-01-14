<template>
  <div>
    <!-- Contenitore delle nuvole animate -->
    <div class="sky">
      <div class="cloud"></div>
      <div class="cloud"></div>
    </div>

    <div class="homepage">
      <!-- Titolo e sottotitolo in alto al centro -->
      <div class="header">
        <h1 class="title">EMOTINO</h1>
        <h2 class="subtitle">Hello {{ username }}!</h2>
      </div>

      <!-- Pulsante Logout in alto a destra -->
      <div class="top-right">
        <button class="logout-button" @click="logout">Logout</button>
      </div>

      <!-- Foto in alto a sinistra -->
      <div class="top-left">
        <img src="@/assets/profile-circle-svgrepo-com.svg" width="100" alt="P">
      </div>

      <!-- Contenuto centrale con bottoni -->
      <div class="center-buttons">
        <button @click="navigateTo('profile')">My Profile</button>
        <button @click="navigateTo('history')">History</button>
      </div>

      <!-- Bottone in basso al centro -->
      <div class="bottom-center">
        <button @click="startSession">Start Session</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import {ref} from "vue"; // Importa il router

const router = useRouter(); // Ottieni l'istanza del router
const username = ref(localStorage.getItem('name')); // Nome utente

// Funzione per navigare tra le pagine
function navigateTo(page) {
  console.log(`Navigating to ${page}...`);
  router.push(`/${page}`); // Naviga dinamicamente verso la pagina specificata
}

// Funzione per iniziare la sessione
function startSession() {
  console.log('Starting a new session...');
  router.push('/session'); // Naviga alla pagina "session"
}

// Funzione per gestire il logout
function logout() {
  console.log('User logged out');
  router.push('/'); // Riporta l'utente alla schermata di login
}
</script>

<style scoped>
/* Contenitore per l'intera homepage */
.homepage {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 90vh;
  padding: 0;
  font-family: Arial, sans-serif;
  position: relative;
  overflow: hidden;
}

.cloud {
  position: absolute;
  top: 20px;
  width: 200px;
  height: 100px;
  background: white;
  border-radius: 50%;
  box-shadow: -30px 30px 0px white, 30px 30px 0px white, 0px 30px 0px white;
  animation: float 10s linear infinite;
}

.cloud:nth-child(2) {
  top: 50px;
  left: 80%;
  width: 250px;
  height: 120px;
  animation-delay: 5s;
}

@keyframes float {
  0% {
    left: -200px;
  }
  100% {
    left: 110%;
  }
}

/* Titolo e sottotitolo */
.header {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.title {
  font-size: 90px;
  font-weight: bold;
  margin: 0;
  font-family: Copperplate;
  color: #1666cb; /* Blu scuro */
}

.subtitle {
  font-size: 25px;
  font-weight: normal;
  font-family: Copperplate;
  margin: 30px 0 0 0;
  color: #1666cb; /* Blu pastello */
}

/* Pulsante Logout in alto a destra */
.top-right {
  position: absolute;
  top: 20px;
  right: 20px;
}

.logout-button {
  padding: 10px 20px;
  font-size: 18px;
  background-color: #ff6b6b; /* Rosso pastello */
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.logout-button:hover {
  background-color: #d45a5a; /* Rosso scuro */
}

/* Foto in alto a sinistra */
.top-left {
  position: absolute;
  top: 20px;
  left: 20px;
}

.profile-photo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #cccccc;
}

/* Bottoni centrali */
.center-buttons {
  display: flex;
  flex-direction: row;
  gap: 60px;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.center-buttons button {
  padding: 20px 40px;
  font-size: 24px;
  font-family: Copperplate;
  cursor: pointer;
  border: none;
  background: linear-gradient(90deg, #5cc0ff, #92d8f7); /* Blu sfumato */
  color: white;
  border-radius: 15px;
  transition: all 0.3s ease;
  min-width: 200px;
  min-height: 60px;
}

.center-buttons button:hover {
  transform: scale(1.1);
  box-shadow: 0 0 15px rgba(92, 192, 255, 0.5); /* Bagliore blu */
}

/* Bottone in basso al centro */
.bottom-center {
  position: absolute;
  bottom: 20px;
  text-align: center;
}

.bottom-center button {
  padding: 40px 80px;
  font-size: 40px;
  font-family: Copperplate;
  cursor: pointer;
  border: none;
  background: linear-gradient(90deg, #ff8c6b, #ff6ba3); /* Arancio-rosa sfumato */
  color: white;
  border-radius: 15px;
  transition: background-color 0.3s ease;
  min-width: 200px;
  min-height: 60px;
}

.bottom-center button:hover {
  background: linear-gradient(90deg, #d4735b, #d45a7c); /* Sfuma verso toni più scuri */
}
</style>

