<template>
  <div>
    <!-- Contenitore delle nuvole animate -->
    <div class="sky">
      <div class="cloud"></div>
      <div class="cloud"></div>
    </div>

    <div class="homepage">
      <!-- Titolo e sottotitolo in alto al centro -->
      <div class="header">
        <h1 class="title">EMOTINO</h1>
        <h2 class="subtitle">My profile</h2>
      </div>

      <!-- Pulsante Logout in alto a destra -->
      <div class="top-right">
        <button class="logout-button" @click="logout">Logout</button>
      </div>

      <!-- Foto in alto a sinistra -->
      <div class="top-left">
        <img src="path-to-your-image.jpg" alt="Profile Photo" class="profile-photo" />
      </div>
    </div>

    <div class="profile-info">
      <img :src="user.profilePhoto" alt="Profile Photo" class="profile-photo" />
      <h2>{{ user.name }}</h2>
      <p>Email: {{ user.email }}</p>
      <p>Joined: {{ user.joinedDate }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const user = ref({});

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

    user.value = await response.json();
  } catch (error) {
    console.error('Error fetching user profile:', error);
  }
}

function logout() {
  localStorage.removeItem('token');
  router.push('/');
}

onMounted(() => {
  fetchUserProfile();
});

</script>

<style scoped>

</style>