<template>
  <div :style="backgroundStyle" class="audio-page">
    <div class="question">
      <p>{{ question }}</p>
    </div>
    <div class="audio-container">
      <div class="microphone">
        <img :src="microphoneImage" alt="Microphone" />
      </div>
      <div class="audio-visualizer">
        <div class="waveform">
          <div v-for="(bar, index) in waveformBars" :key="index" :style="barStyle(bar)"></div>
        </div>
      </div>
    </div>
    <div class="timer">
      <div class="timer-progress">
        <div class="progress-bar" :style="progressBarStyle"></div>
      </div>
      <p class="time-remaining">{{ formattedTime }}</p>
    </div>
  </div>

</template>

<script setup>

import microphoneImage from '@/assets/microphone.jpeg';
import { ref, onMounted, computed } from 'vue';

const backgroundImage = ref('path/to/default-image.jpg');
const question = ref('Your AI-generated question here');
const waveformBars = ref(new Array(20).fill(0)); // 20 bars for waveform animation

const timeRemaining = ref(120);

// Dynamic styles
const backgroundStyle = computed(() => ({
  backgroundImage: `url(${backgroundImage.value})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
}));

const barStyle = (height) => ({
  height: `${height}%`,
  width: '6px',
  margin: '0 3px',
  background: 'green',
  transition: 'height 0.1s ease',
});

const progressBarStyle = computed(() => ({
  height: `${(timeRemaining.value / 120) * 100}%`,
  backgroundColor: 'green',
  width: '100%',
  borderRadius: '5px',
}));

const formattedTime = computed(() => {
  const minutes = Math.floor(timeRemaining.value / 60);
  const seconds = timeRemaining.value % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
});

// Simulate audio input
async function setupMicrophone() {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({audio: true});
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const analyser = audioContext.createAnalyser();
    const microphone = audioContext.createMediaStreamSource(stream);
    microphone.connect(analyser);
    analyser.fftSize = 256;

    const dataArray = new Uint8Array(analyser.frequencyBinCount);

    const updateWaveform = () => {
      analyser.getByteFrequencyData(dataArray);
      const volume = dataArray.reduce((a, b) => a + b, 0) / dataArray.length;
      waveformBars.value = waveformBars.value.map(() => (volume > 10 ? Math.random() * 100 : 10));
      requestAnimationFrame(updateWaveform);
    };

    updateWaveform();
  } catch (error) {
    console.error('Error accessing microphone:', error);
  }
}

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
  setupMicrophone();
  startTimer();
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

.audio-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  height: 150px;
}

.microphone {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.microphone img {
  width: 60px;
  height: 60px;
}

.audio-visualizer {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px;
  width: 400px;
  position: relative;
}

.waveform {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.waveform div {
  display: inline-block;
}

.timer {
  position: absolute;
  right: 40px;
  top: 50%;
  transform: translateY(-50%);
  width: 30px;
  height: 300px;
  background: rgba(0, 128, 0, 0.2);
  border-radius: 10px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.timer-progress {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: flex-end;
  flex-direction: column;
}

.progress-bar {
  transition: height 1s linear;
}

.time-remaining {
  margin-top: 10px;
  font-size: 1rem;
  color: white;
  text-align: center;
}

.fixed-microphone {
  position: fixed;
  bottom: 20px;
  left: 20px;
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background-color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.fixed-microphone img {
  width: 40px;
  height: 40px;
}
</style>
