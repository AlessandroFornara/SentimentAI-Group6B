<template>
  <div class="audio-page">

    <div class="background-bubbles">
      <div v-for="(bubble, index) in bubbles" :key="index" class="floating-bubble" :style="bubble.style"></div>
    </div>



    <div class="floating-sphere"></div>
    <div class="floating-sphere"></div>
    <div class="floating-sphere"></div>
    <div class="floating-sphere"></div>
    <div class="floating-sphere"></div>

    <!-- Frase Generata -->
    <div class="question">
      <img :src="questionImage" alt="Question Icon" class="question-image" />
      <p>{{ displayedText }}</p>
    </div>
    <p style="font-size: 25px">{{ $t('sessionRemainingTime') }}: {{formattedSessionRemainingTime}}</p>

    <!-- Nuvola con l'immagine selezionata -->
    <div :class="['masking', { 'no-mask': !shouldMask }]">
      <img :src="selectedImage" alt="Cloud Image" class="cloud-image" />
    </div>



    <div class="timer">    <!-- Minuti e secondi mancanti -->

      <div class="timer-progress">
        <div class="progress-bar" :style="progressBarStyle"></div>
      </div>
    </div>


    <!-- Timer -->
    <div class="timer">
      <div class="timer-progress">
        <div class="progress-bar" :style="progressBarStyle"></div>
      </div>
      <p class="time-remaining">{{ formattedAudioRemainingTime }}</p>
    </div>

    <!-- Contenitore degli elementi di registrazione -->
    <div class="recording-box">
      <!-- Pulsante Start Audio -->
      <button v-if="!isRecording && questionReady" @click="startRecording" class="btn-start">
        {{ $t('startAudioButton') }}
      </button>

      <!-- Icona del Microfono -->
      <div class="microphone-container">
        <img :src="microphoneImage" alt="Microphone" />
      </div>

      <!-- Barra Audio -->
      <div v-if="isRecording" class="audio-visualizer">
        <canvas ref="audioCanvas"></canvas>
      </div>

      <!-- Pulsante Finish Audio -->
      <button v-if="showFinishButton" @click="finishRecording" class="btn-finish">
        Finish Audio
      </button>
    </div>

    <!-- Pulsante Terminate Session -->
    <div v-if="maxSessionTime - totalRemainingTime >= minimumSessionTime" class="terminate-session">
      <button @click="goToResultPage" class="btn-terminate">Terminate Session</button>
    </div>
  </div>
</template>


<script setup>
import microphoneImage from '@/assets/Microphone.png';
import { ref, computed, onMounted } from 'vue';
import {useRoute, useRouter, onBeforeRouteLeave} from "vue-router";
import questionImage from '@/assets/HappyCloud.png'
import { i18n } from "@/main";

// Stato iniziale
const selectedImage = ref(null); // Per memorizzare l'immagine selezionata
const question = ref(''); // Prima domanda fissa

const maxAudioTime = 80;
const maxSessionTime = 360;
const timeRemaining = ref(maxAudioTime);
const minimumSessionTime = 120;

const shouldMask = ref(true)
// Calcolo dei minuti e secondi mancanti

const isRecording = ref(false);
const showFinishButton = ref(false);
const questionReady = ref(true);
const displayedText = ref("")
const typingSpeed = 50;
const elapsedTime = ref(0);
const totalRemainingTime = ref(maxSessionTime); // Tempo totale accumulato
let timerInterval = null;
const microphoneAccessError = ref(false);
const audioCanvas = ref(null);
let analyser;
let audioChunks = [];
let mediaRecorder;
let recognition; // Per la trascrizione Web Speech API
let liveTranscript = ''; // Accumula la trascrizione live
let completeTranscript = ''; // Trascrizione finale
const skipBeforeRouteLeave = ref(false);
let endSession = false;

const router = useRouter(); // Router per la navigazione

function toggleMasking() {
  shouldMask.value = !shouldMask.value;
}

function preventReload(event)
{
  event.preventDefault();
  event.returnValue = '';
}

onBeforeRouteLeave((to, from, next) => {
  if(!skipBeforeRouteLeave.value) {
    const confirmLeave = window.confirm(
        'Are you sure you want to leave? Your session may be deleted or closed.'
    );
    if (confirmLeave) {
      closeSession();
      window.removeEventListener('beforeunload', preventReload);
      next();
    } else {
      next(false);
    }
  }else{
    window.removeEventListener('beforeunload', preventReload);
    next();
  }
});

const closeSession = () => {
  fetch('/api/worker/end_session', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  })
      .then((response) => {
        if (!response.ok) {
          if (response.status === 401) {
            alert('Session expired. Please log in again.');
            router.push('/login');
            return;
          }
          throw new Error('Error fetching session results');
        }
        router.push('/home')
      })
      .catch((error) => {
        console.error('Error fetching session results:', error);
        alert('Failed to fetch session results. Please try again later.');
      });
};

// Funzione per avviare il conto alla rovescia
const startTimer = () => {
  timerInterval = setInterval(() => {
    if (timeRemaining.value > 0 && totalRemainingTime.value > 0) {
      timeRemaining.value--; // Riduci di un secondo
      totalRemainingTime.value -= 1; // Incrementa il tempo totale
    } else {
      if (totalRemainingTime.value === 0) {
        endSession = true;
        finishRecording()
      } else {
        finishRecording();
      }
    }
  }, 1000);
};

const typeText = () => {
  let index = 0;
  displayedText.value = "";
  const interval = setInterval(() => {
    if (index < question.value.length) {
      displayedText.value += question.value[index];
      index++;
    } else {
      clearInterval(interval);
    }
  }, typingSpeed);
};


// Recupera l'immagine salvata in sessionStorage all'avvio
onMounted(() => {
  const route = useRoute();
  const backgroundImage = route.query.background;
  if (backgroundImage) {
    if(route.query.mask === 'false'){
      toggleMasking();
      question.value = i18n.global.locale === 'it-IT' ? 'Di cosa vuoi parlare oggi?' : 'What do you want to talk about today?';
    }else
      question.value = i18n.global.locale === 'it-IT' ? 'Ottima scelta. A cosa ti fa pensare questa immagine?' : 'Great choice. What does this image make you think of?';
    selectedImage.value = backgroundImage;  // Salva l'immagine nel ref
    typeText();
  }else{
    skipBeforeRouteLeave.value = true;
    closeSession();
    router.push('/home');
  }

  const remainingQuery = { ...route.query };
  delete remainingQuery.background;
  router.replace({ query: remainingQuery });

  window.addEventListener('beforeunload', preventReload);
});

const generateBubble = () => {
  const numberOfBubbles = Math.floor(5 * bubbleMultiplier.value); // Numero di bolle moltiplicato
  for (let i = 0; i < numberOfBubbles; i++) {
    const bubbleSize = Math.random() * 10 + 10; // Dimensione casuale
    const bubbleLeft = Math.random() * 100; // Posizione casuale
    const bubbleStyle = {
      width: `${bubbleSize}px`,
      height: `${bubbleSize}px`,
      left: `${bubbleLeft}%`,
      animationDuration: `${Math.random() * 3 + 2}s`, // Durata casuale
      background: 'radial-gradient(circle, #ff7f50, #ffa500, #ff4500)', // Sfumatura arancione
    };

    bubbles.value.push({ style: bubbleStyle });

    // Rimuovi la bolla dopo la fine dell'animazione
    setTimeout(() => {
      bubbles.value.shift();
    }, 10000); // Le bolle durano 10 secondi
  }
};



const progressBarStyle = computed(() => ({
  height: `${(timeRemaining.value / maxAudioTime) * 100}%`, // Altezza dinamica basata sul tempo
  backgroundColor: 'orange', // Verde chiaro
  transition: 'height 1s linear', // Transizione fluida
}));

const bubbles = ref([]); // Array reattivo per gestire le bolle
const bubbleMultiplier = ref(1); // Moltiplicatore per il numero di bolle



const formattedAudioRemainingTime = computed(() => {
  const minutes = Math.floor(timeRemaining.value / 60);
  const seconds = timeRemaining.value % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
});

const formattedSessionRemainingTime = computed(() => {
  const minutes = Math.floor(totalRemainingTime.value / 60);
  const seconds = totalRemainingTime.value % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
});

const startRecording = () => {
  microphoneAccessError.value = false;
  isRecording.value = true;
  bubbleMultiplier.value = 2; // Aumenta il numero di bolle
  showFinishButton.value = false;
  questionReady.value = false;
  timeRemaining.value = maxAudioTime;
  elapsedTime.value = 0;
  liveTranscript = '';
  completeTranscript = '';

  // Mostra il pulsante Finish Audio dopo 30 secondi
  setTimeout(() => {
    if (isRecording.value) {
      showFinishButton.value = true;
    }
  }, 5000);

  // Genera bolle continuamente durante la registrazione
  const bubbleInterval = setInterval(() => {
    if (isRecording.value) {
      generateBubble();
    } else {
      clearInterval(bubbleInterval);
    }
  }, 1000);

  // Avvia il timer
  startTimer();
  setupMicrophone();
};


const finishRecording = async () => {
  isRecording.value = false;
  bubbleMultiplier.value = 1; // Torna al numero normale di bolle
  showFinishButton.value = false;

  clearInterval(timerInterval);
  timeRemaining.value = maxAudioTime; // Reset del timer

  // Ferma la registrazione
  mediaRecorder.stop();
  recognition.stop();
};


const goToResultPage = () => {
  // Reindirizza alla pagina ResultPage
  skipBeforeRouteLeave.value = true;
  router.push('/result');
};

// Funzione per inviare l'audio e la trascrizione al server
const sendAudioToServer = async (audioBlob, transcript) => {
  const formData = new FormData();
  formData.append('audio', audioBlob);
  formData.append('audioTranscript', transcript);
  formData.append('language', i18n.global.locale);
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
        router.push('/login');
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
    const stream = await navigator.mediaDevices.getUserMedia({audio: true});
    checkMicrophoneInput(stream); // Controllo input microfono

    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    analyser = audioContext.createAnalyser();
    const microphone = audioContext.createMediaStreamSource(stream);
    microphone.connect(analyser);

    mediaRecorder = new MediaRecorder(stream);
    audioChunks = [];

    // Configura la trascrizione live
    if (!recognition) {
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
        completeTranscript += liveTranscript;
        console.log('Trascrizione completa:', completeTranscript);
        if (isRecording.value) {
          liveTranscript = '';
          recognition.start();
        }
      };
    }

    recognition.start();

    mediaRecorder.ondataavailable = (event) => {
      audioChunks.push(event.data);
    };

    mediaRecorder.onstop = async () => {
      const audioBlob = new Blob(audioChunks, {type: 'audio/wav'});
      recognition.onend(); // Termina la trascrizione
      try {
        console.log('Trascrizione finale:', completeTranscript);

        // Invia l'audio e la trascrizione al server
        const newQuestion = await sendAudioToServer(audioBlob, completeTranscript);
        if (newQuestion || endSession) {
          if(endSession){
            goToResultPage()
            return;
          }
          question.value = newQuestion;
          typeText();
          questionReady.value = true;
        }else{
          alert('You registered an empty audio. Please try again.');
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
      ctx.fillStyle = 'rgb(0, 0, 255)'; // Bianco
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
}

.image-bubble {
  width: 1000px;
  height: 1000px;
  display: flex;
  align-items: center;
  justify-content: center;
  -webkit-mask-image: url('/src/assets/cloud-shape.svg');
  -webkit-mask-size: cover;
  mask-image: url('/src/assets/cloud-shape.svg');
  mask-size: cover;
  background-color: white;
  overflow: hidden;
}

.center-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}



.bubble-shape {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.bubble-image {
  position: absolute;
  top: 10px; /* Offset interno */
  left: 10px;
  width: calc(100% - 20px); /* Per adattare l'immagine */
  height: calc(100% - 20px);
  object-fit: cover; /* Mantiene proporzioni */
  border-radius: 15px; /* Per armonizzare */
  z-index: 2;
}



.center-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Mantiene proporzioni senza modifiche */
}

.question {
  display: flex; /* Disposizione orizzontale di immagine e testo */
  align-items: center; /* Allinea verticalmente immagine e testo */
  text-align: left; /* Allinea il contenitore a sinistra */
  gap: 15px; /* Spazio tra immagine e testo */
  margin: 20px 0; /* Spazio intorno al box */
  margin-top: 20px;
  font-size: 2rem;
  font-family: 'Lobster', cursive;
  text-align: center;
  color: #1666cb;
  font-weight: bold;
  position: relative; /* Necessario per far funzionare il z-index */
  z-index: 10; /* Garantisce che il testo sia sempre sopra altri elementi */
}

.question-image {
  width: 100px; /* Larghezza immagine */
  height: 100px; /* Altezza immagine */
  object-fit: contain; /* Adatta l'immagine mantenendo proporzioni */
  animation: bounce 1s infinite;
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}
/* Box unico per la registrazione */
.recording-box {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px; /* Spazio tra gli elementi */
  padding: 20px; /* Padding interno */
}

/* Pulsante Start Audio */
.btn-start {
  padding: 10px 20px;
  font-size: 1.2rem;
  background: linear-gradient(45deg, #ffa500, #ff4500); /* Sfumatura arancione */
  color: white; /* Testo bianco */
  font-weight: bold; /* Grassetto */
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-start:hover {
  background: linear-gradient(45deg, #ff7f50, #ff6347); /* Sfumatura più chiara al passaggio del mouse */
}

/* Icona del microfono */
.microphone-container {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.microphone-container img {
  width: 100%;
  //height: 100%;
  object-fit: cover;
}

/* Barra audio */
.audio-visualizer {
  width: 300px;
  height: 50px;
  background-color: transparent;
}

.audio-visualizer canvas {
  width: 100%;
  height: 100%;
}

/* Pulsante Finish Audio */
.btn-finish {
  padding: 10px 20px;
  font-size: 1.2rem;
  background: linear-gradient(45deg, #ffa500, #ff4500); /* Azzurro */
  color: white; /* Testo bianco */
  font-weight: bold; /* Grassetto */
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-finish:hover {
  background:linear-gradient(45deg, #ff7f50, #ff6347);  /* Azzurro più scuro quando si passa sopra */
}

/* Timer */
.timer {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start; /* Posiziona il contenuto verso l'alto */
  position: absolute;
  right: 100px;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 300px;
  background: rgba(255, 255, 255, 0.2); /* Sfondo trasparente */
  border-radius: 10px;
  overflow: hidden;

}

.progress-bar {
  background: linear-gradient(to bottom, #ff4500, #ffa500); /* Sfumatura arancione */
  transition: height 1s linear;
  width: 100%;
  height: 100%; /* Partenza piena */
  transform-origin: bottom; /* L'origine diventa il bordo superiore */
}




.time-remaining {
  position: absolute;
  margin-bottom: 10px; /* Spazio tra il testo e la barra */
  color: orange; /* Testo blu */
  font-size: 1rem;
  font-weight: bold;
}


.timer-progress {
  width: 100%;
  height: 100%; /* Stessa altezza del timer */
  position: relative;
}

/* Pulsante Terminate Session */
.terminate-session {
  position: fixed;
  bottom: 20px;
  right: 20px;
}

.btn-terminate {
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #1666cb;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-terminate:hover {
  background-color: #0f4da8;
}



@media (max-width: 800px) {
  .image-bubble {
    width: 100%;
    height: 100%;
  }

  .microphone-container {
    width: 60px;
    height: 60px;
  }



  .timer {
    height: 200px;
  }
}

.floating-bubble {
  position: absolute;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, #ff7f50, #ffa07a, #ff6347); /* Sfumature arancione-rosa */
  border-radius: 50%;
  animation: floatUp 8s ease-in-out infinite;
  opacity: 0.8;
}

.cloud-container {
  position: relative;
  width: 500px; /* Larghezza della nuvola */
  height: 300px; /* control the size */
  aspect-ratio: 1.8;
  --g: radial-gradient(50% 50%, #000 98%, #0000) no-repeat;
  mask: var(--g) 100% 100%/30% 60%,var(--g) 70% 0/50% 100%,var(--g) 0 100%/36% 68%,var(--g) 27% 18%/26% 40%,linear-gradient(#000 0 0) bottom/67% 58% no-repeat;
  background: #269af2;
}

.masking {
  width: 40%;
  height: auto;
  position: relative;
  mask-image: url('../assets/cloudMask.png');
  mask-size: cover;
  mask-repeat: no-repeat;
  mask-position: center;
  -webkit-mask-image: url('../assets/cloudMask.png');
  -webkit-mask-size: cover;
  -webkit-mask-repeat: no-repeat;
  -webkit-mask-position: center;
  background-color: #fff; /* Optional: background color inside the mask */
}

.no-mask {
  width: 20%;
  mask-image: none;
  -webkit-mask-image: none;
  background-color: transparent;
}

.cloud-image {
  width: 100%;
  height: 100%;
  object-fit: cover; /* Adatta l'immagine alla nuvola */
}

@keyframes floatUp {
  0% {
    transform: translateY(100vh);
    opacity: 0.8;
  }
  50% {
    transform: translateY(50vh);
    opacity: 1;
  }
  100% {
    transform: translateY(-10%);
    opacity: 0;
  }
}

.floating-sphere {
  position: absolute;
  width: 100px;
  height: 100px;
  background: radial-gradient(circle, #ff7f50, #ffa07a, #ff6347); /* Sfumature arancione-rosa */
  border-radius: 50%;
  animation: floatUp 8s ease-in-out infinite;
  opacity: 0.8;
}

.floating-sphere:nth-child(1) {
  left: 10%;
  animation-delay: 0s;
  width: 120px;
  height: 120px;
}

.floating-sphere:nth-child(2) {
  left: 30%;
  animation-delay: 2s;
  width: 80px;
  height: 80px;
}

.floating-sphere:nth-child(3) {
  left: 50%;
  animation-delay: 4s;
  width: 100px;
  height: 100px;
}

.floating-sphere:nth-child(4) {
  left: 70%;
  animation-delay: 1s;
  width: 90px;
  height: 90px;
}

.floating-sphere:nth-child(5) {
  left: 90%;
  animation-delay: 3s;
  width: 110px;
  height: 110px;
}

.time-numbers {
  position: absolute;
  top: -25px; /* Posiziona sopra il timer */
  color: orange; /* Testo arancione */
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
  width: 100%;
}

.time-remaining-text {
  font-size: 1.5rem; /* Dimensione del testo */
  font-weight: bold;
  color: orange; /* Colore arancione */
  text-align: center;
  margin-bottom: 10px; /* Spazio sopra la barra del timer */
}

</style>
