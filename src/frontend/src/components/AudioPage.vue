<template>
  <div :style="backgroundStyle" class="audio-page">
    <div class="question">
      <p>{{ question }}</p>
    </div>
    <div class="audio-box">
      <div class="audio-visualizer">
        <div v-for="(bar, index) in audioBars" :key="index" :style="barStyle(bar)"></div>
      </div>
      <div class="microphone">
        <i class="microphone-icon">🎤</i>
      </div>
    </div>
    <div class="timer">
      <div class="timer-cylinder" :style="timerStyle"></div>
      <p class="time-remaining">{{ formattedTime }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';

// State variables
const backgroundImage = ref('path/to/default-image.jpg'); // Replace with dynamic path
const question = ref('');
const audioBars = ref(new Array(10).fill(0)); // Audio visualizer bars
const timeRemaining = ref(120); // Timer in seconds

// Dynamic styles
const backgroundStyle = computed(() => ({
  backgroundImage: `url(${backgroundImage.value})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
}));

const barStyle = (height) => ({
  height: `${height}%`,
  width: '10px',
  margin: '0 5px',
  backgroundColor: 'green',
  animation: 'pulse 0.5s ease infinite',
});

const timerStyle = computed(() => ({
  height: `${(timeRemaining.value / 120) * 100}%`,
  backgroundColor: 'red',
}));

const formattedTime = computed(() => {
  const minutes = Math.floor(timeRemaining.value / 60);
  const seconds = timeRemaining.value % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
});

// Fetch question from backend
const fetchQuestion = async () => {
  try {
    const response = await fetch('/api/get-question');
    const data = await response.json();
    question.value = data.question;
  } catch (error) {
    console.error('Error fetching question:', error);
    question.value = 'Default question';
  }
};

// Simulate audio visualizer
const updateAudioBars = () => {
  audioBars.value = audioBars.value.map(() => Math.random() * 100);
};

// Countdown timer
const startTimer = () => {
  const interval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value -= 1;
    } else {
      clearInterval(interval);
    }
  }, 1000);
};

// Lifecycle hooks
onMounted(() => {
  fetchQuestion();
  startTimer();
  setInterval(updateAudioBars, 200);
});
</script>

<style scoped>
.audio-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  height: 100vh;
  width: 100vw;
  position: relative;
  overflow: hidden;
}

.question {
  margin-top: 20px;
  font-size: 1.5rem;
  text-align: center;
  color: white;
  text-shadow: 0 0 10px rgba(0, 0, 0, 0.7);
}

.audio-box {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.audio-visualizer {
  display: flex;
  align-items: flex-end;
  justify-content: center;
  height: 100px;
  margin-bottom: 10px;
}

.microphone {
  font-size: 2rem;
  color: white;
}

.timer {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
}

.timer-cylinder {
  width: 20px;
  background: rgba(255, 0, 0, 0.8);
  transition: height 1s linear;
}

.time-remaining {
  margin-top: 10px;
  font-size: 1rem;
  color: white;
}

@keyframes pulse {
  0%, 100% {
    transform: scaleY(1);
  }
  50% {
    transform: scaleY(1.5);
  }
}
</style>
