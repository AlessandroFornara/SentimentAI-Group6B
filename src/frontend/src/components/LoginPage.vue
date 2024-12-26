<template>
  <div class="login-page">
    <h1 class="title">Login</h1>

    <form @submit.prevent="handleLogin" class="login-form">
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

      <!-- Pulsante di Login -->
      <button type="submit" class="login-button">Login</button>
    </form>

    <!-- Pulsante di Registrazione -->
    <button class="register-button" @click="navigateToRegister">Register</button>

    <!-- Messaggio di errore -->
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // Importa il router

const router = useRouter(); // Ottieni l'istanza del router

// Variabili reattive
const username = ref('');
const password = ref('');
let errorMessage = ref('');
const requestOptions = {
  method: "POST",
  headers: { 'Content-Type': 'application/json' },
  body: null
};


// Funzione per gestire il login
function handleLogin() {
  requestOptions.body = JSON.stringify({
    email: username.value,
    password: password.value,

  });

  fetch("/api/auth/login", requestOptions)
      .then(response => {
        if (response.status === 200) {
          return response.json().then(data => {
            localStorage.setItem("token", data.token);
            localStorage.setItem("email", data.email);
            localStorage.setItem("role", data.role)
            errorMessage.value = '';
            router.push('/home');
          });
        } else {
          return response.text().then(error => {
            errorMessage.value = error;
          });
        }
      })
      .catch(error => {
        errorMessage.value = 'There was an error in sending the request';
        console.error('There was an error in sending the request:', error);
      });
}

// Navigazione alla pagina di registrazione
function navigateToRegister() {
  router.push('/register'); // Naviga alla pagina di registrazione
}
</script>

<style scoped>
.login-page {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #87ceeb; /* Sfondo azzurro */
  font-family: Arial, sans-serif;
}

.title {
  font-size: 40px;
  margin-bottom: 20px;
  color: mediumpurple;
}

.login-form {
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

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.login-button {
  padding: 10px;
  background-color: mediumpurple;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 18px;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background-color: indigo;
}

.register-button {
  margin-top: 15px;
  padding: 10px 20px;
  background-color: lightblue;
  color: black;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.register-button:hover {
  background-color: #007bff;
  color: white;
}

.error-message {
  color: red;
  margin-top: 15px;
}
</style>
