import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useToast } from 'vue-toastification' // <-- 1. IMPORTE O USETOAST

// Importe suas views (páginas)
import LoginView from '../views/LoginView.vue'
import CadastroView from '../views/CadastroView.vue'
import DashboardView from '../views/DashboardView.vue'
import TarefasView from '../views/TarefasView.vue'
import AdminDashboardView from '../views/AdminDashboardView.vue'

const toast = useToast() // <-- 2. CRIE A INSTÂNCIA DO TOAST

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/cadastro',
      name: 'cadastro',
      component: CadastroView
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView,
      meta: { requiresAuth: true }
    },
    {
      path: '/tarefas',
      name: 'tarefas',
      component: TarefasView,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/dashboard',
      name: 'admin-dashboard',
      component: AdminDashboardView,
      meta: { requiresAuth: true, requiresAdmin: true }
    }
  ]
})

// Navigation Guard (Guarda de Navegação) com Toast
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  // Verifica se a autenticação é necessária e se o usuário está logado
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login') // Redireciona para o login se não estiver autenticado
  }
  // Verifica se privilégios de admin são necessários e se o usuário não é admin
  else if (to.meta.requiresAdmin && !authStore.isAdmin) {
    toast.error("Acesso negado. Apenas administradores.") // <-- 3. USE O TOAST AQUI
    next('/dashboard'); // Redireciona usuários não-admin para o dashboard normal
  }
  // Se passou por todas as verificações, permite o acesso
  else {
    next()
  }
})

export default router
