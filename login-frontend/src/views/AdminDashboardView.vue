<template>
  <div class="admin-dashboard">
    <h1>Painel do Administrador</h1>
    <p>Aqui estão todas as tarefas de todos os usuários.</p>
    <div v-if="isLoading">Carregando tarefas...</div>
    <div v-else-if="error">{{ error }}</div>
    <table v-else>
      <thead>
        <tr>
          <th>ID Tarefa</th>
          <th>Título</th>
          <th>Status</th>
          <th>ID Usuário</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="tarefa in todasAsTarefas" :key="tarefa.id">
          <td>{{ tarefa.id }}</td>
          <td>{{ tarefa.titulo }}</td>
          <td>{{ tarefa.concluida ? 'Concluída' : 'Pendente' }}</td>
          <td>{{ tarefa.usuario_id || 'N/A' }} </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useToast } from 'vue-toastification';

const todasAsTarefas = ref([]);
const isLoading = ref(false);
const error = ref(null);
const toast = useToast();

async function fetchTodasAsTarefas() {
  isLoading.value = true;
  error.value = null;
  try {
    const response = await axios.get('http://localhost:8080/api/tarefas/admin/all');
    todasAsTarefas.value = response.data;
  } catch (err) {
    error.value = 'Falha ao buscar tarefas.';
    toast.error('Você não tem permissão para ver esta página ou ocorreu um erro.');
    console.error(err);
  } finally {
    isLoading.value = false;
  }
}

onMounted(fetchTodasAsTarefas);
</script>

<style scoped>
.admin-dashboard { max-width: 900px; margin: auto; }
table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
th, td { border: 1px solid var(--color-border); padding: 8px; text-align: left; }
th { background-color: var(--color-background-soft); }
</style>
