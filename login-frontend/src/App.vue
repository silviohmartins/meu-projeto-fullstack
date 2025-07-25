<template>
  <header>
    <div class="wrapper">
      <nav>
        <RouterLink v-if="authStore.isAuthenticated" to="/dashboard">Dashboard</RouterLink>
        <RouterLink v-if="authStore.isAuthenticated" to="/tarefas">Tarefas</RouterLink>
        <RouterLink v-if="authStore.isAdmin" to="/admin/dashboard">Admin</RouterLink>
        <RouterLink v-if="!authStore.isAuthenticated" to="/login">Login</RouterLink>
        <RouterLink v-if="!authStore.isAuthenticated" to="/cadastro">Cadastro</RouterLink>
        <a v-if="authStore.isAuthenticated" @click="authStore.logout" href="#" class="logout-button">Sair</a>
      </nav>
    </div>
  </header>

  <main>
    <RouterView />
  </main>
</template>

<script setup>
import { onMounted } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

// Quando o App for montado, verifica se já existe um token no localStorage
onMounted(() => {
  authStore.checkAuth()
})
</script>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
  padding: 1rem 0;
  border-bottom: 1px solid var(--color-border);
}

.wrapper {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
}

nav {
  width: 100%;
  font-size: 1rem;
  text-align: center;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
  color: var(--color-text);
  text-decoration: none;
}
nav a:first-of-type {
  border: 0;
}
nav a.router-link-exact-active {
  color: hsla(160, 100%, 37%, 1);
}
.logout-button {
  cursor: pointer;
}
main {
  padding-top: 2rem;
}
</style>
