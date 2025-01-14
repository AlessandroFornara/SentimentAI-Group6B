<template>
  <div class="container" style="display: flex; flex-direction: column">

    <div class="container mt-5" style="display: flex; flex-direction: row; align-items: baseline">
      <h1>Profile</h1>
      <h4 style="margin-left: 2%; color: darkgreen">{{role}}</h4>
    </div>

    <div class="container" style="display: flex; flex-direction: row; margin-top: 10%">

      <div class="container" style="display: flex; align-items: center">
        <div class="col" style="width: 35%; display: flex; justify-content: center">
          <img src="@/assets/profile-circle-svgrepo-com.svg" width="300" alt="P">
        </div>
        <div class="col" style="margin-left: 10%">
          <p>Email: <strong>{{email}}</strong></p>
        </div>
        <div class="col" style="margin-left: 10%">
          <p>Name: <strong>{{name}}</strong></p>
          <p>Surname: <strong>{{surname}}</strong></p>
          <p>Company: <strong>{{company}}</strong></p>
          <p>Level: <strong>{{level}}</strong></p>
          <p>Points: <strong>{{points}}</strong></p>
          <p>Badges: <strong>{{badges}}</strong></p>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
//import { useRouter } from 'vue-router';

//const router = useRouter();
const name = ref('');
const surname = ref('');
const email = ref('');
const company = ref('');
const level = ref('');
const points = ref('');
const role = ref('');
const badges = ref('');

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
/*
function logout() {
  localStorage.removeItem('token');
  router.push('/');
}*/

onMounted(() => {
  fetchUserProfile();
});

</script>

<style scoped>

</style>