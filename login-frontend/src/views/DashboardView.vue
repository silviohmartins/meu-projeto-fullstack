<template>
  <div class="dashboard-container">
    <h1 v-if="authStore.user">Bem-vindo, {{ authStore.user.nome }}!</h1>
    <h1 v-else>Carregando...</h1>

    <p>Você está em uma rota protegida e seus dados foram buscados com sucesso da API.</p>
    <div v-if="authStore.user" class="user-info">
      <p><strong>ID:</strong> {{ authStore.user.id }}</p>
      <p><strong>Email:</strong> {{ authStore.user.email }}</p>
    </div>

    <button @click="handleLogout">Sair (Logout)</button>
  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

function handleLogout() {
  authStore.logout()
}
</script>

<style scoped>
.dashboard-container {
  text-align: center;
  margin-top: 50px;
}
.user-info {
  margin: 2rem auto;
  padding: 1rem;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  max-width: 400px;
  background-color: var(--color-background-soft);
}
</style>
