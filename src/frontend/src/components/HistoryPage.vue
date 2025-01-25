<template>
  <div class="upper-left">
    <button @click="goBack">Back</button>
  </div>

  <div style="display: flex; flex-direction: column; align-items: center">

    <h1 style="text-align: center; margin-top: 20px">Session History</h1>
    <div v-if="sessions.length > 0" class="scroll-container">
      <div
          v-for="session in sessions"
          :key="session.date"
          class="session-card">
        <p><strong>Date:</strong> {{ formatDate(session.date) }}</p>
        <p><strong>Topic:</strong> {{ session.topic }}</p>
        <p><strong>Dominant Emotion:</strong> {{ session.dominantEmotion }}</p>
        <p><strong>Proposed Activity:</strong> {{ session.activityCategory }} - {{ session.activityText }}</p>
      </div>
    </div>

    <div v-else style="text-align: center; color: gray;">
      <p>No session history available.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

import { useRouter } from 'vue-router';

const router = useRouter();
let sessions = ref([]);

onMounted(() => {
  fetchHistory();
});

function formatDate(date) {
  const options = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
  return new Date(date).toLocaleDateString('en-US', options);
}

async function fetchHistory() {
  try {
    const response = await fetch('/api/worker/history', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Error fetching session history.');
    }

    const data = await response.json();
    sessions.value = data.sessions;
  } catch (error) {
    console.error('Error fetching session history:', error);
  }
}

function goBack() {
  console.log('Navigating back to HomePage...');
  router.push('/home');
}

</script>

<style>

.scroll-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 40px;
  max-height: 500px;
  overflow-y: auto;
  scrollbar-width: none; /* For Firefox */
  -ms-overflow-style: none; /* For IE and Edge */
}

.scroll-container::-webkit-scrollbar {
  display: none; /* For Chrome, Safari, and Edge */
}

.session-card {
  background-color: #f9f9f9;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease;
  border: 1px solid #ddd;
  border-radius: 10px;
  margin-bottom: 15px;
  padding: 15px;
  width: 100%;
}

.session-card:hover {
  transform: translateY(-5px);
}
</style>