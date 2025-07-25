import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'
import router from '@/router'

// URL base da API (criaremos um endpoint /users)
const API_BASE_URL = 'http://localhost:8080/api'

export const useAuthStore = defineStore('auth', () => {
  // STATE
  const token = ref(localStorage.getItem('user_token') || null)
  const user = ref(JSON.parse(localStorage.getItem('user_data')) || null) // NOVO: Armazena dados do usuário

  // GETTERS
  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  // ACTIONS

  // NOVO: Ação para buscar os dados do usuário logado
  async function fetchUser() {
    if (token.value) {
      try {
        const response = await axios.get(`${API_BASE_URL}/users/me`)
        user.value = response.data
        localStorage.setItem('user_data', JSON.stringify(user.value))
      } catch (error) {
        console.error('Erro ao buscar dados do usuário:', error)
        // Se der erro (ex: token expirado), limpa tudo
        clearToken()
      }
    }
  }

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('user_token', newToken)
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`
  }

  function clearToken() {
    token.value = null
    user.value = null // LIMPA o usuário do estado
    localStorage.removeItem('user_token')
    localStorage.removeItem('user_data') // LIMPA o usuário do localStorage
    delete axios.defaults.headers.common['Authorization']
  }

  async function login(credentials) {
    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials)
      const receivedToken = response.data.token
      if (receivedToken) {
        setToken(receivedToken)
        await fetchUser() // ALTERADO: Busca os dados do usuário logo após o login
        await router.push('/dashboard')
      } else {
        throw new Error('Token não recebido')
      }
    } catch (error) {
      clearToken()
      throw error
    }
  }

  async function register(userInfo) {
    try {
      await axios.post(`${API_BASE_URL}/auth/cadastro`, userInfo)
    } catch (error) {
      throw error
    }
  }

  function logout() {
    clearToken()
    router.push('/login')
  }

  function checkAuth() {
    const storedToken = localStorage.getItem('user_token')
    if (storedToken) {
      setToken(storedToken)
      fetchUser() // ALTERADO: Se achar um token, busca os dados do usuário também
    }
  }

  return {
    token,
    user, // EXPOR o usuário
    isAuthenticated,
    isAdmin,
    login,
    register,
    logout,
    checkAuth,
    fetchUser // EXPOR a nova ação
  }
})
