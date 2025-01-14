<template>
  <div class="result-page" :style="backgroundStyle">
    <h1 class="title">Your session is concluded! Congratulations</h1>
    <h2 class="subtitle">Here are some results from the analysis:</h2>

    <div class="result-content">
      <p class="emotion">The emotion that you are mostly feeling is: <span>{{ dominantEmotion }}</span></p>
      <p class="activity">Suggested activity based on your emotion: <span>{{ activity }}</span></p>
      <p class="points">You earned <span>{{ points }}</span> points!</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const dominantEmotion = ref('');
const activity = ref('');
const points = ref(0);

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
  } catch (error) {
    console.error('Error fetching session results:', error);
    alert('Failed to fetch session results. Please try again later.');
  }
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
</script>

<style scoped>
.result-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.title {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 1.5rem;
  font-weight: 300;
  margin-bottom: 20px;
}

.result-content {
  font-size: 1.2rem;
  line-height: 1.8;
}

.result-content span {
  font-weight: bold;
  color: #fff;
}

.emotion {
  margin-bottom: 10px;
}

.activity {
  margin-bottom: 10px;
}

.points {
  margin-bottom: 10px;
}
</style>
