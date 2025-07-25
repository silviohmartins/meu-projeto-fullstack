<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useToast } from 'vue-toastification'

const toast = useToast()
const tarefas = ref([])
const novaTarefa = ref({ titulo: '', descricao: '' })

// Corrigido: Define a URL base a partir das variáveis de ambiente
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
// Corrigido: Cria uma URL completa e específica para o endpoint de tarefas
const TAREFAS_API_URL = `${API_BASE_URL}/tarefas`;

// READ
async function fetchTarefas() {
  try {
    // Corrigido: Usa a nova variável
    const response = await axios.get(TAREFAS_API_URL)
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
    // Corrigido: Usa a nova variável
    const response = await axios.post(TAREFAS_API_URL, novaTarefa.value)
    tarefas.value.push(response.data)
    novaTarefa.value = { titulo: '', descricao: '' }
    toast.success('Tarefa adicionada!')
  } catch (error) {
    toast.error('Erro ao adicionar tarefa.')
    console.error(error)
  }
}

// UPDATE
async function toggleConcluida(id) {
  try {
    // Corrigido: Usa a nova variável
    await axios.put(`${TAREFAS_API_URL}/${id}`)
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
    // Corrigido: Usa a nova variável
    await axios.delete(`${TAREFAS_API_URL}/${id}`)
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
