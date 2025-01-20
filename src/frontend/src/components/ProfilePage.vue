<template>
  <div class="container" style="display: flex; flex-direction: column">

    <div class="container mt-5" style="display: flex; flex-direction: row; align-items: baseline">
      <h1>Profile</h1>
      <h4 style="margin-left: 2%; color: darkgreen">{{role}}</h4>
    </div>

    <div class="container" style="display: flex; flex-direction: column; margin-top: 10%; gap: 100px">

      <div class="container" style="display: flex; align-items: center">
        <div class="col" style="width: 35%; display: flex; justify-content: center">
          <img src="@/assets/profile-circle-svgrepo-com.svg" width="300" alt="P">
        </div>
        <div class="col" style="margin-left: 10%">
          <p>Email: <strong>{{email}}</strong></p>
          <p>Name: <strong>{{name}}</strong></p>
          <p>Surname: <strong>{{surname}}</strong></p>
          <p>Company: <strong>{{company}}</strong></p>
          <p>Level: <strong>{{level}}</strong></p>
          <p>Points: <strong>{{points}}</strong></p>
        </div>
      </div>

      <div class="badges-container">
        <h2 style="text-align: center; margin-bottom: 20px;">Your Badges</h2>
        <div class="badges-grid">
          <div v-for="(value, key) in badgesData" :key="key" class="badge">
            <img :src="getBadgeImage(key)" :alt="key" class="badge-image" />
            <p>{{ key.replace(/([A-Z])/g, ' $1').trim() }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const name = ref('');
const surname = ref('');
const email = ref('');
const company = ref('');
const level = ref('');
const points = ref('');
const role = ref('');
const badges = ref('');

const badgesData = {
  activityBasedBadge: 0,
  levelBasedBadge: 0,
  timeBasedBadge: 0,
  topicBasedBadge: 0
};

// Function to fetch user profile from the server
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

// Funzione per ottenere l'immagine corrispondente a ciascun badge
const getBadgeImage = (badgeKey) => {
  const badgeImages = {
    activityBasedBadge: '@/assets/activity-badge.png',
    levelBasedBadge: '@/assets/level-badge.png',
    timeBasedBadge: '@/assets/time-badge.png',
    topicBasedBadge: '@/assets/topic-badge.png'
  };
  return badgeImages[badgeKey] || '@/assets/default-badge.png';
};

onMounted(() => {
  fetchUserProfile();
});

</script>

<style scoped>

</style>