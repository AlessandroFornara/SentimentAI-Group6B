<template>
  <div class="login-page">
    <div style="display: flex; justify-content: center; align-items: center; flex-direction: column; width: 300px">
      <h1 class="title">EMOTINO</h1>
      <h2 class="subtitle">{{ $t('slogan') }}</h2>
      <img src="@/assets/mic_emotino_noBack.png" width="300" alt="EMOTINO">
    </div>
    <form class="login-form" @submit.prevent="handleSubmit">
      <!-- Input Username -->
      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input id="email" class="form-control" v-model="email" type="text" required/>
      </div>

      <!-- Input Password -->
      <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <input id="password" class="form-control" v-model="password" type="password" required/>
      </div>

      <div class="row text-center">
        <a
            @click.prevent="register()"
            href="#"
        >{{ registration ? "Already registered? Log in!" : "New here? Sign up!" }}</a>
      </div>

      <div class="mb-3" v-if="registration">
        <label for="name" class="form-label">Name</label>
        <input id="name" class="form-control" v-model="name" type="text" required/>
      </div>

      <div class="mb-3" v-if="registration">
        <label for="surname">Surname</label>
        <input id="surname" v-model="surname" type="text" required/>
      </div>

      <div class="mb-3" v-if="registration">
        <label for="company" class="form-label">Company</label>
        <input id="company" class="form-control" v-model="company" type="text" required/>
      </div>

      <div class="row" v-if="registration">

        <div class="col">
          <p style="font-family: Calibri, sans-serif">WORKER</p>
        </div>

        <div class="col d-flex justify-content-center">
          <div class="form-check form-switch">
            <input
                class="form-check-input"
                type="checkbox"
                role="switch"
                id="flexSwitchCheckDefault"
                v-model="role"
            >
          </div>
        </div>

        <div class="col d-flex justify-content-center">
          <p style="font-family: Calibri, sans-serif">HR</p>
        </div>

      </div>

      <button
          v-if="!registration"
          type="submit"
          class="btn btn-primary"
      >Log in</button>

      <button
          v-if="registration"
          type="submit"
          class="btn btn-primary"
      >Sign up</button>

      <p
          v-if="errorMessage!==''"
          class="text-danger text-center"
          role="alert"
      >
        {{ errorMessage }}
      </p>

      <p
          v-if="successMessage!==''"
          class="text-success text-center"
          role="alert"
      >
        {{ successMessage }}
      </p>
    </form>
    <div style="position: absolute; top: 20px; right: 20px">
      <button @click="changeLanguage('en-US')">English</button>
      <button @click="changeLanguage('it-IT')">Italiano</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // Importa il router
import { changeLanguage } from '@/main';

const router = useRouter();

const email = ref('');
const password = ref('');
const name = ref('');
const surname = ref('');
const company = ref('');
const role = ref(false);
const registration = ref(false);
let successMessage = ref('');
let errorMessage = ref('');
const requestOptions = {
  method: "POST",
  headers: { 'Content-Type': 'application/json' },
  body: null
};

function register() {
  this.registration = (!this.registration)
}

function handleSubmit() {
  if (registration.value) {
    handleRegister();
  } else {
    handleLogin();
  }
}

function handleLogin() {
  requestOptions.body = JSON.stringify({
    email: email.value,
    password: password.value,

  });

  fetch("/api/auth/login", requestOptions)
      .then(response => {
        if (response.status === 200) {
          return response.json().then(data => {
            localStorage.setItem("token", data.token);
            localStorage.setItem("name", data.username);
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

function handleRegister() {
  let assignedRole = role.value ? "HR" : "WORKER";
  console.log(assignedRole);
  requestOptions.body = JSON.stringify({
    email: email.value,
    password: password.value,
    name: name.value,
    surname: surname.value,
    company: company.value,
    role: assignedRole
  });

  fetch("/api/auth/register", requestOptions)
      .then(response => {
        if (response.status === 200) {
          return response.text().then(data => {
            successMessage.value = data;
            errorMessage.value = '';
            router.push('/');
          });
        } else {
          return response.text().then(error => {
            successMessage.value = '';
            errorMessage.value = error;
          });
        }
      })
      .catch(error => {
        errorMessage.value = 'There was an error in sending the request';
        console.error('There was an error in sending the request:', error);
      });
}
</script>

<style scoped>
.title {
  font-size: 70px;
  font-family: 'Lobster', cursive;
  margin: 0;
  color: #1666cb; /* Blu scuro */
}

.subtitle {
  font-size: 25px;
  font-weight: normal;
  font-family: 'Lobster', cursive;
  color: #1666cb; /* Blu pastello */
  text-align: center;
}

.login-page {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  height: 100vh;
  font-family: Arial, sans-serif;
  gap: 20%
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

.text-danger {
  margin-top: 10%;
}

.text-success {
  margin-top: 10%;
}
.btn {
  margin-top: 10%;
}
</style>
