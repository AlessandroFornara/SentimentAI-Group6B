<template>
  <div class="result-page" :style="backgroundStyle">
    <h1 class="title">Your session is concluded! <em>Congratulations</em></h1>
    <h2 class="subtitle">Here are some results from the analysis:</h2>
    <!-- Corpo diviso in quattro sezioni -->
    <div class="result-grid">
      <!-- Sezione: Emotion -->
      <div class="grid-item emotion-box">
        <p>The emotion you are feeling is:</p>
        <div class="emotion-display">
          <img :src="getEmotionImage(dominantEmotion)" alt="Emotion Icon" class="emotion-icon" />
          <p class="emotion-text">{{ dominantEmotion }}</p>
        </div>
      </div>

      <!-- Sezione: Points -->
      <div class="grid-item points-box">
        <button v-if="!pointsCollected" class="collect-button" @click="collectPoints">Click here to collect your points</button>
        <p v-else class="points">You earned <span>{{ points }}</span> points!</p>
      </div>
      <div v-if="showConfetti" class="confetti-container">
      </div>

      <!-- Sezione: Suggested Activity -->
      <div class="grid-item activity-box">
        <p>Suggested activity:</p>
        <p class="activity-text">{{ activity }}</p>
      </div>

      <!-- Sezione: Badges -->
      <div class="grid-item badges-box">
        <p>Your badges:</p>
        <div class="badges-container">
          <div v-for="([key, value]) in filteredBadges" :key="key" class="badge">
            <img :src="getBadgeImage(key, value)?.path" :alt="`${key} level ${value}`" class="badge-image" />
            <p class="badge-label">{{ capitalizeWords(key) }} (Level {{ value }})</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import confetti from 'canvas-confetti';
import {badgeImages, capitalizeWords} from '@/utils/badgeUtils';
import angerImage from '@/assets/Anger.png';
import fearImage from '@/assets/Fear.png';
import disgustImage from '@/assets/Disgust.png';
import joyImage from '@/assets/Joy.png';
import sadnessImage from '@/assets/Sadness.png';
import neutralImage from '@/assets/Neutral.png';
import surpriseImage from '@/assets/Surprise.png';

const router = useRouter();
const dominantEmotion = ref('');
const activity = ref('');
const points = ref(0);
const badges = ref('');
const pointsCollected = ref(false);
const showConfetti = ref(false);

// Mappatura emozioni -> immagini
const emotionImages = {
  Anger: angerImage,
  Fear: fearImage,
  Disgust: disgustImage,
  Joy: joyImage,
  Sadness: sadnessImage,
  Neutral: neutralImage,
  Surprise: surpriseImage,
};

// Funzione per ottenere l'immagine in base all'emozione
const getEmotionImage = (emotion) => {
  return emotionImages[emotion] || neutralImage; // Valore di fallback: immagine neutrale
};

const fetchSessionResults = async () => {
  try {
    const response = await fetch('/api/worker/end_session', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      if (response.status === 401) {
        alert('Session expired. Please log in again.');
        router.push('/login');
        return;
      }
      throw new Error('Error fetching session results');
    }

    const data = await response.json();
    dominantEmotion.value = data.dominantEmotion;
    activity.value = data.activityResponse.activityText;
    points.value = data.points;
    badges.value = data.badges;
  } catch (error) {
    console.error('Error fetching session results:', error);
    alert('Failed to fetch session results. Please try again later.');
  }
};

const collectPoints = () => {
  showConfetti.value = true;
  confetti({
    particleCount: 150,
    spread: 70,
    origin: { y: 0.6 },
  });

  setTimeout(() => {
    showConfetti.value = false;
    pointsCollected.value = true;
  }, 2000);
};

onMounted(() => {
  fetchSessionResults();
});

const backgroundStyle = {
  backgroundColor: '#87CEEB',
  color: 'white',
  height: '100vh',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  justifyContent: 'center',
  textAlign: 'center',
  padding: '20px',
};

const getBadgeImage = (badgeKey, level) => {
  return badgeImages[badgeKey]?.[level];
}

</script>

<style scoped>
.result-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #87ceeb;
  color: white;
  padding: 20px;
}

.title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
}

.result-grid {
  display: grid;
  grid-template-columns: 1fr 1fr; /* Due colonne */
  grid-template-rows: 1fr 1fr; /* Due righe */
  gap: 20px; /* Spazio tra le sezioni */
  width: 80%; /* Adatta alla larghezza della pagina */
  height: 60%; /* Adatta l'altezza */
}

.grid-item {

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  height: 200px;
  background: linear-gradient(to right, #e65100, #fb8c00, #ffcc80);
}

.emotion-box .emotion-text {
  color: blue;
  font-size: 2rem;
  font-weight: bold;
  animation: wave 2s infinite;
}

.emotion-display {
  display: flex;
  align-items: center; /* Allinea verticalmente immagine e testo */
  gap: 15px; /* Spazio tra immagine e testo */
}

.emotion-icon {
  width: 50px; /* Larghezza dell'immagine */
  height: 50px; /* Altezza dell'immagine */
  object-fit: contain; /* Mantiene proporzioni */
}

.emotion-text {
  color: blue;
  font-size: 2rem;
  font-weight: bold;
  animation: wave 2s infinite; /* Animazione ondulata */
}

@keyframes wave {
  0%, 100% {
    transform: rotate(-10deg);
  }
  50% {
    transform: rotate(10deg);
  }
}


.collect-button {
  background-color: darkblue; /* Blu */
  color: white; /* Testo bianco */
  padding: 10px 20px; /* Spaziatura interna */
  font-family: "Ink Free", sans-serif;
  font-size: 1.2rem; /* Dimensione del testo */
  border: none; /* Rimuove il bordo */
  border-radius: 10px; /* Angoli smussati */
  cursor: pointer; /* Cursore puntatore al passaggio del mouse */
  transition: background-color 0.3s ease; /* Animazione su hover */
}

.collect-button:hover {
  background-color: #0056b3; /* Blu più scuro quando si passa sopra */
}

.points{
  font-size: 2rem;
}


.activity-box .activity-text {
  color: blue;
  font-size: 2rem;
  font-weight: bold;
}

.badges-box .badges-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: center;
}

.badge-image {
  width: 60px;
  height: 60px;
}

.badge-label {
  font-size: 0.8rem;
  margin-top: 5px;
  color: white;
}

@keyframes wave {
  0%, 100% {
    transform: rotate(-10deg);
  }
  50% {
    transform: rotate(10deg);
  }
}



</style>
