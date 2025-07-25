import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import { createTestingPinia } from '@pinia/testing'

import DashboardView from '../DashboardView.vue'

describe('DashboardView', () => {
  it('renderiza a mensagem de boas-vindas para o usuário logado', () => {
    // Arrange
    const wrapper = mount(DashboardView, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
            initialState: {
              auth: {
                user: { nome: 'Usuário de Teste' },
              },
            },
          }),
        ],
      },
    })

    // Assert
    expect(wrapper.text()).toContain('Bem-vindo, Usuário de Teste!')
    expect(wrapper.text()).toContain('Você está em uma rota protegida')
  })

  it('mostra "Carregando..." quando o usuário ainda não foi carregado', () => {
    // Arrange
    const wrapper = mount(DashboardView, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn,
            initialState: {
              auth: { user: null },
            },
          }),
        ],
      },
    })

    // Assert
    expect(wrapper.text()).toContain('Carregando...')
  })
})
