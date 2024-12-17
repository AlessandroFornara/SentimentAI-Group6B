<template>
  <div class="register-page">
    <h1 class="title">Register</h1>

    <form @submit.prevent="handleRegister" class="register-form">
      <!-- Input Ruolo -->
      <div class="form-group">
        <label for="role">Role</label>
        <select id="role" v-model="role" required>
          <option value="WORKER">WORKER</option>
          <option value="HR">HR</option>
        </select>
      </div>

      <!-- Input Username -->
      <div class="form-group">
        <label for="username">Username</label>
        <input
            id="username"
            v-model="username"
            type="text"
            placeholder="Enter your username"
            required
        />
      </div>

      <!-- Input Password -->
      <div class="form-group">
        <label for="password">Password</label>
        <input
            id="password"
            v-model="password"
            type="password"
            placeholder="Enter your password"
            required
        />
      </div>

      <!-- Pulsante di Registrazione -->
      <button type="submit" class="register-button">Register</button>
    </form>

    <!-- Messaggio di errore o successo -->
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const role = ref('');
const username = ref('');
const password = ref('');
const errorMessage = ref('');
const successMessage = ref('');

// Simulazione di un "database" in memoria
const registeredUsers = JSON.parse(localStorage.getItem('users')) || [];

// Funzione per la registrazione
function handleRegister() {
  if (registeredUsers.some((u) => u.username === username.value)) {
    errorMessage.value = 'Username already exists.';
    successMessage.value = '';
  } else {
    registeredUsers.push({
      role: role.value,
      username: username.value,
      password: password.value,
    });
    localStorage.setItem('users', JSON.stringify(registeredUsers));
    successMessage.value = 'Registration successful! Redirecting to login...';
    errorMessage.value = '';

    // Reindirizza alla pagina di login dopo 2 secondi
    setTimeout(() => {
      router.push('/');
    }, 2000);
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #87ceeb;
  font-family: Arial, sans-serif;
}

.title {
  font-size: 40px;
  margin-bottom: 20px;
  color: mediumpurple;
}

.register-form {
  display: flex;
  flex-direction: column;
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  width: 300px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.register-button {
  padding: 10px;
  background-color: mediumpurple;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 18px;
  transition: background-color 0.3s ease;
}

.register-button:hover {
  background-color: indigo;
}

.error-message {
  color: red;
  margin-top: 15px;
}

.success-message {
  color: green;
  margin-top: 15px;
}
</style>
