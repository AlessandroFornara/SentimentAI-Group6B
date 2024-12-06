<template>
  <div class="homepage">
    <!-- Foto in alto a sinistra -->
    <div class="top-left">
      <img src="path-to-your-image.jpg" alt="Profile Photo" class="profile-photo" />
    </div>

    <!-- Contenuto centrale con bottoni -->
    <div class="center-buttons">
      <button @click="navigateTo('profile')">My Profile</button>
      <button @click="navigateTo('achievements')">My Achievements</button>
      <button @click="navigateTo('history')">History</button>
    </div>

    <!-- Bottone in basso al centro -->
    <div class="bottom-center">
      <button @click="startSession">Start Session</button>
    </div>
  </div>
</template>


<script setup>
function navigateTo(page) {
  console.log(`Navigating to ${page}...`);
  // Puoi aggiungere logica per cambiare pagina, ad esempio:
  // this.$router.push(`/${page}`);
}

function startSession() {
  console.log('Starting a new session...');
  // Puoi aggiungere la logica per avviare una nuova sessione
}
</script>

<style scoped>
.homepage {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
  padding: 20px;
  font-family: Arial, sans-serif;
}

/* Stile per la foto in alto a sinistra */
.top-left {
  position: absolute;
  top: 20px;
  left: 20px;
}

.profile-photo {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #ccc;
}

/* Stile per i bottoni al centro */
.center-buttons {
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
}

.center-buttons button {
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
  border: none;
  background-color: #007bff;
  color: white;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

.center-buttons button:hover {
  background-color: #0056b3;
}

/* Stile per il bottone in basso */
.bottom-center {
  position: absolute;
  bottom: 20px;
  text-align: center;
}

.bottom-center button {
  padding: 15px 30px;
  font-size: 20px;
  cursor: pointer;
  border: none;
  background-color: #28a745;
  color: white;
  border-radius: 10px;
  transition: background-color 0.3s ease;
}

.bottom-center button:hover {
  background-color: #1c7430;
}
</style>



