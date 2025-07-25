<template>
  <div class="tarefas-container">
    <h1>Minhas Tarefas</h1>

    <form @submit.prevent="addTarefa" class="add-tarefa-form">
      <input type="text" v-model="novaTarefa.titulo" placeholder="Título da tarefa" required />
      <input type="text" v-model="novaTarefa.descricao" placeholder="Descrição" />
      <button type="submit">Adicionar Tarefa</button>
    </form>

    <ul class="tarefas-list">
      <li v-for="tarefa in tarefas" :key="tarefa.id" :class="{ concluida: tarefa.concluida }">
        <div class="tarefa-info">
          <h3>{{ tarefa.titulo }}</h3>
          <p>{{ tarefa.descricao }}</p>
        </div>
        <div class="tarefa-actions">
          <button @click="toggleConcluida(tarefa.id)">
            {{ tarefa.concluida ? 'Desmarcar' : 'Concluir' }}
          </button>
          <button @click="deleteTarefa(tarefa.id)" class="delete-btn">Apagar</button>
        </div>
      </li>
    </ul>
    <p v-if="!tarefas.length">Você ainda não tem tarefas cadastradas.</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useToast } from 'vue-toastification'

const toast = useToast()
const tarefas = ref([])
const novaTarefa = ref({ titulo: '', descricao: '' })

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// READ
async function fetchTarefas() {
  try {
    const response = await axios.get(API_URL)
    tarefas.value = response.data
  } catch (error) {
    toast.error('Erro ao buscar tarefas.')
    console.error(error)
  }
}

// CREATE
async function addTarefa() {
  if (!novaTarefa.value.titulo.trim()) return
  try {
    const response = await axios.post(API_URL, novaTarefa.value)
    tarefas.value.push(response.data)
    novaTarefa.value = { titulo: '', descricao: '' } // Limpa o formulário
    toast.success('Tarefa adicionada!')
  } catch (error) {
    toast.error('Erro ao adicionar tarefa.')
    console.error(error)
  }
}

// UPDATE
async function toggleConcluida(id) {
  try {
    await axios.put(`${API_URL}/${id}`)
    // Atualiza a lista localmente para refletir a mudança
    const tarefa = tarefas.value.find((t) => t.id === id)
    if (tarefa) {
      tarefa.concluida = !tarefa.concluida
    }
  } catch (error) {
    toast.error('Erro ao atualizar tarefa.')
    console.error(error)
  }
}

// DELETE
async function deleteTarefa(id) {
  try {
    await axios.delete(`${API_URL}/${id}`)
    // Remove a tarefa da lista local
    tarefas.value = tarefas.value.filter((t) => t.id !== id)
    toast.success('Tarefa apagada.')
  } catch (error) {
    toast.error('Erro ao apagar tarefa.')
    console.error(error)
  }
}

// Carrega as tarefas quando o componente é montado
onMounted(fetchTarefas)
</script>

<style scoped>
.tarefas-container {
  max-width: 800px;
  margin: 0 auto;
}
.add-tarefa-form {
  display: flex;
  gap: 10px;
  margin-bottom: 2rem;
}
.add-tarefa-form input {
  flex-grow: 1;
}
.tarefas-list {
  list-style: none;
  padding: 0;
}
.tarefas-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: var(--color-background-soft);
  border-radius: 8px;
  margin-bottom: 1rem;
}
.tarefas-list li.concluida .tarefa-info h3 {
  text-decoration: line-through;
  opacity: 0.6;
}
.tarefa-actions {
  display: flex;
  gap: 10px;
}
.delete-btn {
  background-color: #dc3545;
}
</style>
