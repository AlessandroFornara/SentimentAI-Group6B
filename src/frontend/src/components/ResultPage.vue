<template>
  <div class="result-page" :style="backgroundStyle">
    <h1 class="title">{{ $t('resultPageTitle') }}</h1>
    <h2 class="subtitle">{{ $t('resultPageSubtitle') }}</h2>

    <div class="result-content">
      <p class="emotion">
        <img :src="emotionImagePath" alt="Emotion Image" class="emotion-image" />
        {{ $t('Emotion') }}
        <span style="color: blue">{{ $t(dominantEmotion) }}</span>
      </p>

      <p class="activity">{{ $t('suggestedActivity') }}: <span style="color: blue">{{ activity }}</span></p>
      <button v-if="!pointsCollected" class="collect-button" @click="collectPoints">{{ $t('clickHereButton') }}</button>
      <p v-else class="points">{{ $t('youEarned') }} <span style="color: blue">{{ points }}</span> {{ $t('points') }}!</p>
    </div>
    <div v-if="showConfetti" class="confetti-container"></div>

    <div style="display: flex; flex-direction: column; justify-content: center; margin-top: 5%">
      <h1 v-if="newBadges = Object.values(badges).some(value => value > 0)">{{ $t('obtainedBadges') }}</h1>
      <div v-if="newBadges" style="display: flex; flex-direction: row; justify-content: center">
        <div v-for="(value, key) in badges" :key="key" class="badge">
          <div v-if="value > 0">
            <div v-if="badgeImage = getBadgeImage(key, value)">
              <p style="color: black; font-size: 20px; margin: 0">
                {{ i18n.global.locale === 'it-IT' ? badgeImage.nameIt : badgeImage.name }}
              </p>
              <img :src="badgeImage.path" :alt="`${key} level ${value}`" class="badge-image" style="height: 200px" />
              <p style="color: black">
                {{ capitalizeWords($t(key)) }} ({{ $t('level') }} {{ value }})
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="homeButton">
      <button class="button-custom" @click="navigateTo('home')">{{ $t('homeButton') }}</button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, computed, onUnmounted} from 'vue';
import {useRouter} from 'vue-router';
import confetti from 'canvas-confetti';
import {badgeImages, capitalizeWords} from '@/utils/badgeUtils';
import AngerImage from '@/assets/Anger.png';
import FearImage from '@/assets/Fear.png';
import DisgustImage from '@/assets/Disgust.png';
import NeutralImage from '@/assets/Neutral.png';
import JoyImage from '@/assets/Joy.png';
import SadnessImage from '@/assets/Sadness.png';
import SurpriseImage from '@/assets/Surprise.png';
import { i18n } from "@/main";

const emotionImages = {
  Anger: AngerImage,
  Fear: FearImage,
  Disgust: DisgustImage,
  Neutrality: NeutralImage,
  Joy: JoyImage,
  Sadness: SadnessImage,
  Surprise: SurpriseImage,
};


const emotionImagePath = computed(() => emotionImages[dominantEmotion.value] || null);

const router = useRouter();
const dominantEmotion = ref('');
const activity = ref('');
const points = ref(0);
const badges = ref('');
const pointsCollected = ref(false);
const showConfetti = ref(false);

function preventReload(event) {
  event.preventDefault();
  event.returnValue = '';
}

function navigateTo(page) {
  console.log(`Navigating to ${page}...`);
  sessionStorage.removeItem('isFirstLoad');
  router.push(`/${page}`);
}

const fetchSessionResults = async () => {
  try {
    const response = await fetch('/api/worker/end_session', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`,
        'Accept-Language': i18n.global.locale,
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
  const isFirstLoad = sessionStorage.getItem('isFirstLoad') === null;

  if (isFirstLoad) {
    sessionStorage.setItem('isFirstLoad', 'false');
    fetchSessionResults();
  }else{
    navigateTo('home');
  }

  window.addEventListener('beforeunload', preventReload);
});

onUnmounted(() => {
  window.removeEventListener('beforeunload', preventReload);
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

.emotion-image {
  width: 50px;
  height: 50px;
  margin-right: 10px;
  vertical-align: middle;
  border-radius: 5px;
}

</style>
