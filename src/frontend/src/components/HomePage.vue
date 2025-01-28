<template>
  <div>
    <div class="cloud"></div>
    <div class="cloud"></div>

    <div class="homepage">
      <div class="header">
        <h1 class="title">{{ $t('titleHomePage') }} {{ username }}!</h1>
        <h2 class="subtitle">{{ $t('subtitleHomePage') }}</h2>
      </div>

      <div class="top-right">
        <button class="logout-button" @click="logout">{{ $t('logoutButton') }}</button>
        <div class="language-buttons">
          <button @click="changeLanguage('en-US')">English</button>
          <button @click="changeLanguage('it-IT')">Italiano</button>
        </div>
      </div>

      <div class="top-left">
        <img src="@/assets/mic_emotino_noBack.png" width="150" alt="P">
      </div>

      <div class="center-buttons">
        <button @click="navigateTo('profile')">
          <span>{{ $t('profileButton') }}</span>
          <img src="@/assets/ProfileButtonIcon.png" alt="Profile"/>
        </button>
        <button @click="navigateTo('history')">
          <span>{{ $t('historyButton') }}</span>
          <img src="../assets/historyButtonIcon.png" alt="History"/>
        </button>
      </div>

      <div class="bottom-center">
        <button @click="startSession">{{ $t('startButton') }}</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import {ref} from "vue";
import { changeLanguage } from "@/main";

const router = useRouter();
const username = ref(localStorage.getItem('name'));

function navigateTo(page) {
  console.log(`Navigating to ${page}...`);
  router.push(`/${page}`);
}

function startSession() {
  console.log('Starting a new session...');
  router.push('/session');
}

function logout() {
  console.log('User logged out');
  router.push('/');
}
</script>

<style scoped>
.homepage {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
  font-family: 'Lobster', cursive;
  position: relative;
  overflow: hidden;
}

.cloud {
  position: absolute;
  top: 20px;
  width: 200px;
  height: 100px;
  background: white;
  border-radius: 50%;
  box-shadow: -30px 30px 0px white, 30px 30px 0px white, 0px 30px 0px white;
  animation: float 10s linear infinite;
}

.cloud:nth-child(2) {
  top: 50px;
  left: 80%;
  width: 250px;
  height: 120px;
  animation-delay: 5s;
}

@keyframes float {
  0% {
    left: -200px;
  }
  100% {
    left: 110%;
  }
}

.header {
  position: absolute;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  text-align: center;
}

.title {
  font-size: 60px;
  font-weight: bold;
  margin: 0;
}

.subtitle {
  font-size: 30px;
  font-weight: normal;
  margin: 30px 0 0 0;
}

.center-buttons {
  display: flex;
  flex-direction: row;
  gap: 80px;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.center-buttons button {
  position: relative;
  width: 200px;
  height: 200px;
  border: none;
  border-radius: 50%;
  background-color: transparent;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  overflow: hidden;
}

.center-buttons button span {
  position: absolute;
  bottom: 10px;
  font-size: 1.5rem;
  font-weight: bold;
  color: dodgerblue;
  z-index: 2;
}

.center-buttons button img {
  width: 100%;
  height: 100%;
  object-fit: fill;
}

.center-buttons button:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.center-buttons button:active {
  transform: scale(0.95);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.bottom-center {
  position: absolute;
  bottom: 20px;
  text-align: center;
}

.bottom-center button {
  padding: 30px 70px;
  font-size: 36px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  background: linear-gradient(90deg, dodgerblue, #1666cb);
  color: white;
  border-radius: 20px;
  transition: transform 0.3s ease, box-shadow 0.3s ease, background 0.3s ease;
  min-width: 240px;
  min-height: 70px;
  box-shadow: 0px 8px 20px rgba(30, 144, 255, 0.3);
}

.bottom-center button:hover {
  transform: scale(1.05);
  box-shadow: 0px 12px 30px rgba(30, 144, 255, 0.4);
  background: linear-gradient(90deg, #1e90ff, #0073e6);
}

.bottom-center button:active {
  transform: scale(0.98);
  box-shadow: 0px 6px 15px rgba(0, 0, 0, 0.2);
  background: linear-gradient(90deg, #005bb5, #004a99);
}

</style>

