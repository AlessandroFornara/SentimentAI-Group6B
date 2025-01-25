<template>
  <div class="result-page" :style="backgroundStyle">
    <h1 class="title">Your session is concluded!</h1>
    <h2 class="subtitle">Here are some results from the analysis:</h2>

    <div class="result-content">
      <p class="emotion">The emotion that you are mostly feeling is: <span style="color: blue">{{ dominantEmotion }}</span></p>
      <p class="activity">Suggested activity based on your emotion: <span style="color: blue">{{ activity }}</span></p>
      <button v-if="!pointsCollected" class="collect-button" @click="collectPoints">Click here to collect your points</button>
      <p v-else class="points">You earned <span style="color: blue">{{ points }}</span> points!</p>
    </div>
    <div v-if="showConfetti" class="confetti-container"></div>

    <div style="display: flex; flex-direction: column; justify-content: center; margin-top: 5%">
      <h1 v-if="newBadges = Object.values(badges).some(value => value > 0)">You have obtained the following badges:</h1>
      <div v-if="newBadges" style="display: flex; flex-direction: row; justify-content: center">
        <div v-for="(value, key) in badges" :key="key" class="badge">
          <div v-if="value > 0">
            <div v-if="badgeImage = getBadgeImage(key, value)">
              <p style="color: black; font-size: 20px; margin: 0">
                {{ badgeImage.name }}
              </p>
              <img :src="badgeImage.path" :alt="`${key} level ${value}`" class="badge-image" style="height: 200px" />
              <p style="color: black">
                {{ capitalizeWords(key) }} (Level {{ value }})
              </p>
            </div>
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

const router = useRouter();
const dominantEmotion = ref('');
const activity = ref('');
const points = ref(0);
const badges = ref('');
const pointsCollected = ref(false);
const showConfetti = ref(false);

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
p, h1, h2 {
  color: black;
}

.result-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 20px;
  justify-content: center;
}

.title {
  font-size: 3rem;
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
  width: 100%;
}

.subtitle {
  font-size: 1.5rem;
  font-weight: 300;
  margin-bottom: 20px;
  text-align: left;
}

.result-content {
  font-size: 1.2rem;
  line-height: 1.8;
}

.result-content span {
  font-weight: bold;
  color: #fff;
}

.emotion, .activity, .points {
  margin-bottom: 10px;
}

.collect-button {
  background-color: #4CAF50;
  color: white;
  padding: 10px 20px;
  font-size: 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.collect-button:hover {
  background-color: #45a049;
}

.confetti-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}
</style>
