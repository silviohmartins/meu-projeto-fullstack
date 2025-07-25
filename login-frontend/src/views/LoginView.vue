<template>
  <div class="form-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div class="input-group">
        <label for="email">Email</label>
        <input type="email" id="email" v-model="email" required autocomplete="username" />
      </div>
      <div class="input-group">
        <label for="senha">Senha</label>
        <input type="password" id="senha" v-model="senha" required autocomplete="current-password" />
      </div>

      <button type="submit" :disabled="isLoading">
        <LoadingSpinner v-if="isLoading" />
        <span v-else>Acessar</span>
      </button>
    </form>
    <p class="link-text">Não tem uma conta? <router-link to="/cadastro">Cadastre-se</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useToast } from 'vue-toastification'
import LoadingSpinner from '@/components/LoadingSpinner.vue' // <-- IMPORTAÇÃO DO SPINNER

const authStore = useAuthStore()
const toast = useToast()

const email = ref('')
const senha = ref('')
const isLoading = ref(false)

async function handleLogin() {
  isLoading.value = true
  try {
    await authStore.login({
      email: email.value,
      senha: senha.value
    })
    // O redirecionamento é feito dentro da action do Pinia
  } catch (error) {
    toast.error('Falha no login. Verifique suas credenciais.')
    console.error(error)
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* Os estilos que adicionamos anteriormente continuam aqui */
.form-container {
  max-width: 420px;
  margin: 30px auto;
  background: var(--color-background-soft);
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
.link-text {
  text-align: center;
  margin-top: 2rem;
}
</style>
