<template>
  <div :style="backgroundStyle" class="audio-page">
    <div class="question">
      <p>{{ question }}</p>
    </div>
    <div class="audio-container">
      <div class="microphone">
        <img :src="microphoneImage" alt="Microphone" />
      </div>
    </div>

    <!-- Bottone Start Recording -->
    <button v-if="!isRecording" @click="startRecording" class="btn-center">
      Start Recording
    </button>

    <!-- Bottone Finish Audio -->
    <button v-if="showFinishButton" @click="finishRecording" class="btn-finish">
      Finish Audio
    </button>

    <!-- Timer -->
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
import { ref } from 'vue';
import { computed } from 'vue';


// Stato iniziale
const backgroundImage = ref('path/to/default-image.jpg');
const question = ref('Your AI-generated question here');
const timeRemaining = ref(120);
const isRecording = ref(false);
const showFinishButton = ref(false);
const elapsedTime = ref(0);
let timerInterval = null;

// Stili dinamici
const backgroundStyle = computed(() => ({
  backgroundImage: `url(${backgroundImage.value})`,
  backgroundSize: 'cover',
  backgroundPosition: 'center',
}));

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

// Funzioni per la registrazione e timer
const startRecording = () => {
  isRecording.value = true;
  showFinishButton.value = false;
  timeRemaining.value = 120; // Reset del timer
  elapsedTime.value = 0;

  // Avvia il timer
  timerInterval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
      elapsedTime.value++;
    }
  }, 1000);

  // Dopo 20 secondi, mostra il bottone Finish Audio
  setTimeout(() => {
    showFinishButton.value = true;
  }, 20000);

  // Simula la registrazione audio
  setupMicrophone();
};

const finishRecording = () => {
  isRecording.value = false;
  showFinishButton.value = false;

  // Stoppa il timer e la registrazione
  clearInterval(timerInterval);
  console.log(`Audio registrato per ${elapsedTime.value} secondi.`);

  // Genera una nuova domanda (default)
  question.value = 'Here is your next question!';
};

// Simulazione input microfono
const setupMicrophone = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const analyser = audioContext.createAnalyser();
    const microphone = audioContext.createMediaStreamSource(stream);
    microphone.connect(analyser);

    console.log('Recording started...');
  } catch (error) {
    console.error('Error accessing microphone:', error);
  }
};
</script>

<style scoped>
.audio-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  position: relative;
  overflow: hidden;
  background-color: #1e1e1e;
}

.question {
  margin-bottom: 20px;
  font-size: 1.5rem;
  text-align: center;
  color: white;
}

.microphone img {
  width: 80px;
  height: 80px;
}

/* Bottone Start */
.btn-center {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1.2rem;
  background-color: green;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.btn-center:hover {
  background-color: darkgreen;
}

/* Bottone Finish */
.btn-finish {
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1.2rem;
  background-color: red;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.btn-finish:hover {
  background-color: darkred;
}

/* Timer */
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

.progress-bar {
  transition: height 1s linear;
}

.time-remaining {
  margin-top: 10px;
  font-size: 1rem;
  color: white;
  text-align: center;
}
</style>
