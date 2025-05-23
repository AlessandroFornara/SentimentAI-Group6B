<template>
  <div class="upper-left">
    <button class="button-custom" @click="goBack">Back</button>
  </div>

  <div class="container" style="display: flex; flex-direction: column">

    <div class="container mt-2" style="display: flex; flex-direction: row; align-items: baseline; margin-left: 15%">
      <h1>{{ $t('profileButton') }}</h1>
      <h4 style="margin-left: 2%; color: #1666cb">{{role}}</h4>
    </div>

    <div class="container" style="display: flex; align-items: center">
      <div class="col" style="display: flex; justify-content: center">
        <img src="@/assets/profile-circle-svgrepo-com.svg" width="200px" alt="P">
      </div>
      <div class="col">
        <p>Email: <strong>{{email}}</strong></p>
        <p>{{ $t('name') }}: <strong>{{name}}</strong></p>
        <p>{{ $t('surname') }}: <strong>{{surname}}</strong></p>
        <p>{{ $t('company') }}: <strong>{{company}}</strong></p>

        <div>
          <p style="margin-bottom: 10px; font-size: 18px">
            <strong>{{ $t('level') }}: <span style="color: blue">{{ level }}</span></strong>
          </p>
          <!-- Contenitore della barra -->
          <div style="linear-gradient(90deg, #e0f7ff, #ccefff); height: 30px; width: 50%; border-radius: 10px; overflow: hidden; position: relative; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1)">
            <!-- Barra di progresso -->
            <div :style="{ width: progressData.progress + '%', background: 'linear-gradient(90deg, #0077b6, #00b4d8)', height: '100%' }" style="border-radius: 10px; position: absolute; transition: width 0.5s ease-in-out"></div>

            <!-- Testo al centro della barra -->
            <div style="position: absolute; left: 50%; transform: translate(-50%, 0); color: black; font-weight: bold; line-height: 30px; z-index: 1">
              {{ progressData.currentXP }} / {{ progressData.xpForNextLevel }}
            </div>
          </div>
        </div>
      </div>
    </div>

    <div style="display: flex; flex-direction: column; justify-content: center; margin-top: 5%">
      <div v-if="Object.values(badges).some(value => value > 0)" style="display: flex; flex-direction: row; justify-content: center">
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

      <div v-else style="text-align: center; margin-top: 20px;">
        <p style="color: black; font-size: 18px;">{{ $t('noBadgesToDisplay') }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue';
import { useRouter } from 'vue-router';
import {badgeImages, capitalizeWords} from '@/utils/badgeUtils';
import { i18n } from "@/main";

const router = useRouter();

const name = ref('');
const surname = ref('');
const email = ref('');
const company = ref('');
const level = ref(0);
const points = ref(0);
const role = ref('');
const badges = ref('');

onMounted(() => {
  fetchUserProfile();
});

const calculateProgress = () => {
  if (!Number.isFinite(level.value) || !Number.isFinite(points.value)) {
    return { progress: 0, currentXP: 0, xpForNextLevel: 0 };
  }

  let xpForNextLevel = 80;
  let xpCurrentLevel = 0;

  for (let i = 0; i < level.value; i++) {
    xpCurrentLevel += xpForNextLevel;
    xpForNextLevel += 40;
  }

  const progress = ((points.value - xpCurrentLevel) / xpForNextLevel) * 100;

  return {
    progress: Math.max(0, Math.min(progress, 100)),
    currentXP: Math.max(0, points.value - xpCurrentLevel),
    xpForNextLevel,
  };
};

const progressData = computed(calculateProgress);

async function fetchUserProfile() {
  try {
    const response = await fetch('/api/worker/profile', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });

    if (!response.ok) {
      throw new Error('Error fetching user profile.');
    }

    let user = await response.json();
    name.value = user.name;
    surname.value = user.surname;
    email.value = user.email;
    company.value = user.company;
    level.value = user.level;
    points.value = user.points;
    role.value = user.role;
    badges.value = user.badges;

  } catch (error) {
    console.error('Error fetching user profile:', error);
  }
}

const getBadgeImage = (badgeKey, level) => {
  return badgeImages[badgeKey]?.[level];
}

function goBack() {
  console.log('Navigating back to HomePage...');
  router.push('/home');
}

</script>

<style scoped>

</style>