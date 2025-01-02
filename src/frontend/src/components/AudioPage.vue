<template>
  <div :style="backgroundStyle" class="audio-page">
    <!-- Frase Generata -->
    <div class="question">
      <p>{{ question }}</p>
    </div>

    <!-- Immagine al centro in grande -->
    <div class="image-bubble">
      <img :src="selectedImage" alt="Selected Background" class="center-image" />
    </div>

    <!-- Barra Audio dinamica -->
    <div v-if="isRecording" class="audio-visualizer">
      <canvas ref="audioCanvas"></canvas>
    </div>

    <!-- Icona Microfono -->
    <div class="bottom-container">
      <div class="microphone-container">
        <img :src="microphoneImage" alt="Microphone" />
      </div>
    </div>

    <!-- Timer -->
    <div class="timer">
      <div class="timer-progress">
        <div class="progress-bar" :style="progressBarStyle"></div>
      </div>
      <p class="time-remaining">{{ formattedTime }}</p>
    </div>

    <!-- Bottoni -->
    <div class="controls">
      <button v-if="!isRecording && questionReady" @click="startRecording" class="btn-center">
        Start Recording
      </button>

      <button v-if="showFinishButton" @click="finishRecording" class="btn-finish">
        Finish Audio
      </button>
    </div>
  </div>
</template>

<script setup>
import microphoneImage from '@/assets/microphone.jpeg';
import { ref, computed, onMounted } from 'vue';

// Stato iniziale
const selectedImage = ref(null); // Per memorizzare l'immagine selezionata
const question = ref('How do you feel about this argument?'); // Prima domanda fissa
const timeRemaining = ref(120);
const isRecording = ref(false);
const showFinishButton = ref(false);
const questionReady = ref(true);
const elapsedTime = ref(0);
let timerInterval = null;
const microphoneAccessError = ref(false);
const audioCanvas = ref(null);
let analyser;
let audioChunks = [];
let mediaRecorder;
let recognition; // Per la trascrizione Web Speech API
let liveTranscript = ''; // Accumula la trascrizione live

// Recupera l'immagine salvata in sessionStorage all'avvio
onMounted(() => {
  selectedImage.value = sessionStorage.getItem('selectedImage') || 'path/to/default-image.jpg';
});

// Stili dinamici
const backgroundStyle = computed(() => ({
  backgroundColor: '#87CEEB', // Azzurro chiaro
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
  microphoneAccessError.value = false;
  isRecording.value = true;
  showFinishButton.value = false;
  questionReady.value = false;
  timeRemaining.value = 120; // Reset del timer
  elapsedTime.value = 0;

  // Avvia il timer
  timerInterval = setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
      elapsedTime.value++;

      // Mostra il pulsante Finish dopo 5 secondi
      if (elapsedTime.value >= 5) {
        showFinishButton.value = true;
      }
    } else {
      clearInterval(timerInterval);
      finishRecording();
    }
  }, 1000);

  // Avvia la registrazione audio
  setupMicrophone();
};

const finishRecording = async () => {
  isRecording.value = false;
  showFinishButton.value = false;
  clearInterval(timerInterval);
  console.log('Registrazione terminata.');

  // Stop la registrazione e salva l'audio
  mediaRecorder.stop();

  // Stop la trascrizione
  recognition.stop();

  // Reset del timer immediato
  timeRemaining.value = 120;
};

// Funzione per inviare l'audio e la trascrizione al server
const sendAudioToServer = async (audioBlob, transcript) => {
  const formData = new FormData();
  formData.append('audio', audioBlob);
  formData.append('audioTranscript', transcript);
  console.log('sto per mandare l\'audio');
  try {
    const response = await fetch('/api/worker/handle_audio', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
      body: formData,
    });

    if (!response.ok) {
      if (response.status === 401) {
        console.error('Token scaduto o non valido. Reindirizzamento alla pagina di login.');
        alert('Sessione scaduta. Per favore, effettua nuovamente il login.');
        window.location.href = '/login';
        return null;
      }
      throw new Error(`Errore durante l'invio dell'audio al server: ${response.statusText}`);
    }

    const data = await response.json();
    return data.content;
  } catch (error) {
    console.error('Errore durante l\'invio dell\'audio:', error);
    return null;
  }
};

// Configura il microfono e registra audio
const setupMicrophone = async () => {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    checkMicrophoneInput(stream); // Controllo input microfono

    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    analyser = audioContext.createAnalyser();
    const microphone = audioContext.createMediaStreamSource(stream);
    microphone.connect(analyser);

    mediaRecorder = new MediaRecorder(stream);
    audioChunks = [];

    // Configura la trascrizione live
    recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
    recognition.lang = 'it-IT'; // Imposta la lingua in italiano
    recognition.interimResults = true; // Risultati parziali

    recognition.onresult = (event) => {
      liveTranscript = Array.from(event.results)
          .map((result) => result[0].transcript)
          .join(' ');
      console.log('Trascrizione live:', liveTranscript);
    };

    recognition.onerror = (event) => {
      console.error('Errore nella trascrizione live:', event.error);
    };

    recognition.onend = () => {
      console.log('Trascrizione live terminata.');
    };

    recognition.start();

    mediaRecorder.ondataavailable = (event) => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = async () => {
      const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });

      try {
        console.log('Trascrizione finale:', liveTranscript);

        // Invia l'audio e la trascrizione al server
        const newQuestion = await sendAudioToServer(audioBlob, liveTranscript);
        if (newQuestion) {
          question.value = newQuestion;
          questionReady.value = true;
        }
      } catch (error) {
        console.error('Errore durante la trascrizione o l\'invio:', error);
      }
    };

    mediaRecorder.start();

    animateAudioVisualizer();

    console.log('Registrazione avviata...');
  } catch (error) {
    console.error('Errore durante l\'accesso al microfono:', error);
    microphoneAccessError.value = true;
    isRecording.value = false;
    clearInterval(timerInterval);
  }
};

// Controllo input microfono
const checkMicrophoneInput = (stream) => {
  const audioContext = new (window.AudioContext || window.webkitAudioContext)();
  const source = audioContext.createMediaStreamSource(stream);
  const analyser = audioContext.createAnalyser();
  source.connect(analyser);

  const dataArray = new Uint8Array(analyser.fftSize);
  analyser.getByteTimeDomainData(dataArray);

  /*const isSilent = dataArray.every((value) => value === 128);
  if (isSilent) {
    console.warn('Microfono non riceve input. Verifica il dispositivo.');
    alert('Il microfono non sembra funzionare. Verifica il dispositivo e riprova.');
  } else {
    console.log('Microfono funzionante, input rilevato.');
  }*/
};

// Animazione della barra audio
const animateAudioVisualizer = () => {
  const canvas = audioCanvas.value;
  const ctx = canvas.getContext('2d');
  analyser.fftSize = 256;
  const bufferLength = analyser.frequencyBinCount;
  const dataArray = new Uint8Array(bufferLength);

  canvas.width = 300;
  canvas.height = 50;

  const draw = () => {
    analyser.getByteFrequencyData(dataArray);

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const barWidth = canvas.width / bufferLength;
    let barHeight;
    let x = 0;

    for (let i = 0; i < bufferLength; i++) {
      barHeight = dataArray[i] / 2;
      ctx.fillStyle = 'rgb(255, 255, 255)'; // Bianco
      ctx.fillRect(x, canvas.height - barHeight / 2, barWidth, barHeight);

      x += barWidth + 1;
    }

    if (isRecording.value) {
      requestAnimationFrame(draw);
    }
  };

  draw();
};
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
  background-color: #1e1e1e;
}

.audio-visualizer {
  width: 300px;
  height: 50px;
  position: relative;
  margin-top: 10px;
}

.audio-visualizer canvas {
  width: 100%;
  height: 100%;
  background-color: transparent;
}

.question {
  margin-top: 20px;
  font-size: 2rem;
  text-align: center;
  color: white;
}

.image-bubble {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50%;
  height: 50%;
  border-radius: 50%;
  overflow: hidden;
  background-color: rgba(255, 255, 255, 0.1);
  margin: auto;
}

.center-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.bottom-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: flex-start;
  position: absolute;
  bottom: 60px;
  left: 40px;
}

.microphone-container {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
  margin-right: 20px;
}

.microphone-container.recording {
  transform: scale(1.2);
  box-shadow: 0 0 15px red;
}

.microphone-container img {
  width: 100%;
  height: 100%;
}

.timer {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  right: 40px;
  top: 50%;
  transform: translateY(-50%);
  width: 30px;
  height: 300px;
  background: rgba(0, 128, 0, 0.2);
  border-radius: 10px;
  overflow: hidden;
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

.controls {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  gap: 20px;
}

.btn-center {
  padding: 15px 30px;
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

.btn-finish {
  padding: 15px 30px;
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

@media (max-width: 768px) {
  .image-bubble {
    width: 70%;
    height: 70%;
  }
  .microphone-container {
    width: 60px;
    height: 60px;
  }
  .question {
    font-size: 1.5rem;
  }
  .timer {
    height: 200px;
  }
}
</style>
