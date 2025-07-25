<template>
  <div class="form-container">
    <h2>Cadastro</h2>
    <form @submit.prevent="handleRegister">
      <div class="input-group">
        <label for="nome">Nome</label>
        <input type="text" id="nome" v-model="nome" required autocomplete="name" />
      </div>
      <div class="input-group">
        <label for="email">Email</label>
        <input type="email" id="email" v-model="email" required autocomplete="email" />
      </div>
      <div class="input-group">
        <label for="senha">Senha</label>
        <input type="password" id="senha" v-model="senha" required autocomplete="new-password" />
      </div>

      <button type="submit" :disabled="isLoading">
        <LoadingSpinner v-if="isLoading" />
        <span v-else>Cadastrar</span>
      </button>
    </form>
    <p class="link-text">Já tem uma conta? <router-link to="/login">Faça o login</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useToast } from 'vue-toastification'
import LoadingSpinner from '@/components/LoadingSpinner.vue' // <-- IMPORTAÇÃO DO SPINNER

const authStore = useAuthStore()
const router = useRouter()
const toast = useToast()

const nome = ref('')
const email = ref('')
const senha = ref('')
const isLoading = ref(false)

async function handleRegister() {
  isLoading.value = true

  if (senha.value.length < 6) {
    toast.error('A senha deve ter no mínimo 6 caracteres.')
    isLoading.value = false
    return
  }

  try {
    await authStore.register({
      nome: nome.value,
      email: email.value,
      senha: senha.value
    })
    toast.success('Cadastro realizado com sucesso!')
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    const errorMessage = error.response?.data || 'Erro ao realizar o cadastro. Tente novamente.'
    toast.error(errorMessage)
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
