<template>
  <div>
    <!-- Contenitore delle nuvole animate -->
    <div class="sky"></div>

    <div class="homepage">
      <!-- Titolo e sottotitolo in alto al centro -->
      <div class="header">
        <h1 class="title">EMOTINO</h1>
        <h2 class="subtitle">Session history</h2>
      </div>

      <!-- Pulsante Logout in alto a destra -->
      <div class="top-right">
        <button class="logout-button" @click="logout">Logout</button>
      </div>

      <!-- Foto in alto a sinistra -->
      <div class="top-left">
        <img src="path-to-your-image.jpg" alt="Profile Photo" class="profile-photo" />
      </div>

    </div>

    <div class="session-list">
      <div v-for="session in sessions" :key="session.id" class="session-item">
        <h2>{{ session.title }}</h2>
        <p>{{ session.date }}</p>
        <button @click="viewSession(session.id)">View Details</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // Importa il router

const router = useRouter(); // Ottieni l'istanza del router
const sessions = ref([]);

function viewSession(sessionId) {
  router.push({ name: 'SessionDetailPage', params: { id: sessionId } });
}

// Funzione per gestire il logout
function logout() {
  console.log('User logged out');
  router.push('/'); // Riporta l'utente alla schermata di login

/*function goBack() {
  console.log('Navigating back to HomePage...');
  router.push('/home');
}*/

// Function to fetch session history from the server
async function fetchSessionHistory() {
  try {
    const response = await fetch('/api/sessions/history', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Error fetching session history.');
    }

    sessions.value = await response.json();
  } catch (error) {
    console.error('Error fetching session history:', error);
  }
}

onMounted(() => {
  fetchSessionHistory();
});

}
</script>

<style scoped>
.history-page {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.header {
  text-align: center;
  margin-bottom: 20px;
}

.title {
  font-size: 36px;
  color: mediumpurple;
}

.session-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.session-item {
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f9f9f9;
}

.session-item h2 {
  margin: 0;
  font-size: 24px;
}

.session-item p {
  margin: 5px 0;
  color: #666;
}

.logout-button {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 10px 20px;
  background-color: mediumpurple;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.logout-button:hover {
  background-color: indigo;
}
</style>