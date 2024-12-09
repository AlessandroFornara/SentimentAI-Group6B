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
        <h2 class="subtitle">Welcome, "User"</h2>
      </div>

      <!-- Foto in alto a sinistra -->
      <div class="top-left">
        <img src="path-to-your-image.jpg" alt="Profile Photo" class="profile-photo" />
      </div>

      <!-- Contenuto centrale con bottoni -->
      <div class="center-buttons">
        <button @click="navigateTo('profile')">My Profile</button>
        <button @click="navigateTo('achievements')">My Achievements</button>
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
import { useRouter } from 'vue-router'; // Importa il index

const router = useRouter(); // Ottieni l'istanza del index

function navigateTo(page) {
  console.log(`Navigating to ${page}...`);
  router.push(`/${page}`); // Naviga dinamicamente verso la pagina specificata
}

function startSession() {
  console.log('Starting a new session...');
  router.push('/SessionPage'); // Naviga alla pagina "session"
}
</script>


<style scoped>
/* Contenitore per l'intera homepage */
.homepage {
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* Distribuisci uniformemente gli elementi */
  align-items: center;
  height: 100vh; /* Altezza fissa alla finestra del browser */
  padding: 0; /* Rimuovi padding extra */
  font-family: Arial, sans-serif;
  position: relative;
  overflow: hidden; /* Impedisce lo scrolling */

}


/* Contenitore delle nuvole animate */
.sky {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh; /* Copre l'intera finestra */
  z-index: -1;
  background: #87ceeb; /* Sfondo azzurro cielo */
  overflow: hidden;
}

/* Stile per le singole nuvole */
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

/* Personalizza la seconda nuvola */
.cloud:nth-child(2) {
  top: 50px;
  left: 80%;
  width: 250px;
  height: 120px;
  animation-delay: 5s;
}

/* Animazione del movimento delle nuvole */
@keyframes float {
  0% {
    left: -200px;
  }
  100% {
    left: 110%;
  }
}

/* Stile per la foto in alto a sinistra */
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
  border: 2px solid #ccc;
}

/* Bottoni al centro disposti in riga */
.center-buttons {
  display: flex; /* Flexbox per disporre gli elementi */
  flex-direction: row; /* Elementi in una riga */
  gap: 60px; /* Spazio tra i bottoni */
  justify-content: center; /* Centrare i bottoni orizzontalmente */
  align-items: center; /* Centrare i bottoni verticalmente */
  width: 100%; /* Occupa tutta la larghezza */
  height: 100%; /* Occupa tutta l'altezza */
}

.center-buttons button {
  padding: 20px 40px; /* Aumenta il padding */
  font-size: 24px; /* Aumenta la dimensione del testo */
  font-family: Copperplate;
  cursor: pointer;
  border: none;
  background: linear-gradient(90deg, #007bff, lightblue); /* Gradiente di sfondo */
  color: white;
  border-radius: 15px;
  transition: all 0.3s ease;
  min-width: 200px; /* Larghezza minima per uniformità */
  min-height: 60px; /* Altezza minima per uniformità */
}

.center-buttons button:hover {
  /* Effetto al passaggio del mouse */
  transform: scale(1.1);
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2);
}

/* Stile per il bottone in basso */
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
  background-color: mediumpurple;
  color: white;
  border-radius: 15px;
  transition: background-color 0.3s ease;
  min-width: 200px; /* Larghezza minima per uniformità */
  min-height: 60px; /* Altezza minima per uniformità */
}

.bottom-center button:hover {
  background-color: indigo;
}

/* Stile per il contenitore del titolo e sottotitolo */
.header {
  position: absolute;
  top: 20px; /* Posiziona in alto */
  left: 50%; /* Centra orizzontalmente */
  transform: translateX(-50%); /* Correggi il posizionamento centrale */
  text-align: center; /* Centra il testo */
}

.title {
  font-size: 90px;
  font-weight: bold;
  margin: 0;
  font-family: "Roboto Light";
  color:  mediumpurple;
}

.subtitle {
  font-size: 25px;
  font-weight: normal;
  font-family: Copperplate;
  margin: 30px 0 0 0;
  color: steelblue;
}
</style>
