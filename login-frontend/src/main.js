import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// IMPORTAÇÕES DA BIBLIOTECA DE TOAST
import Toast from 'vue-toastification'
// A LINHA MAIS IMPORTANTE PARA O ESTILO DO TOAST:
import 'vue-toastification/dist/index.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// USA O PLUGIN DE TOAST COM OPÇÕES PADRÃO
app.use(Toast, {
  transition: "Vue-Toastification__bounce",
  maxToasts: 3,
  newestOnTop: true
})

app.mount('#app')
