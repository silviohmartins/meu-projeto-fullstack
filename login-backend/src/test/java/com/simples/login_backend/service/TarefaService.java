package com.simples.login_backend.service;

import com.simples.login_backend.model.Tarefa;
import com.simples.login_backend.model.Usuario;
import com.simples.login_backend.model.UsuarioRole;
import com.simples.login_backend.repository.TarefaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Habilita a integração com o Mockito
@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    // @Mock: Cria uma versão "falsa" (mock) do TarefaRepository.
    // Ele não se conectará ao banco de dados. Nós controlaremos seu comportamento.
    @Mock
    private TarefaRepository tarefaRepository;

    // @InjectMocks: Cria uma instância real do TarefaService e injeta
    // os mocks (como o tarefaRepository) criados nesta classe dentro dele.
    @InjectMocks
    private TarefaService tarefaService;

    @Test // Anotação que marca este método como um caso de teste
    @DisplayName("Deve criar uma tarefa com sucesso") // Nome legível para o teste
    void deveCriarTarefaComSucesso() {
        // Arrange (Organizar/Preparar)
        // 1. Preparamos os dados de entrada para o nosso teste.
        String titulo = "Estudar Testes Unitários";
        String descricao = "Aprender JUnit e Mockito";
        Usuario usuario = new Usuario("Teste User", "teste@email.com", "senha123", UsuarioRole.USER);

        // 2. Preparamos o comportamento do nosso mock.
        // QUANDO (when) o método 'save' do nosso repositório falso for chamado com QUALQUER (any)
        // objeto do tipo Tarefa, ENTÃO (then) retorne o mesmo objeto que foi passado.
        when(tarefaRepository.save(any(Tarefa.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act (Agir)
        // 3. Chamamos o método que queremos testar.
        Tarefa tarefaSalva = tarefaService.criarTarefa(titulo, descricao, usuario);

        // Assert (Verificar)
        // 4. Verificamos se o resultado foi o esperado.
        assertThat(tarefaSalva).isNotNull(); // A tarefa salva não deve ser nula
        assertThat(tarefaSalva.getTitulo()).isEqualTo(titulo); // O título deve ser o mesmo que passamos
        assertThat(tarefaSalva.getUsuario()).isEqualTo(usuario); // O usuário associado deve ser o mesmo
        assertThat(tarefaSalva.isConcluida()).isFalse(); // A tarefa deve começar como não concluída

        // 5. Verificamos se nosso mock foi chamado como esperado.
        // Verifique se o método 'save' do repositório foi chamado exatamente 1 vez.
        verify(tarefaRepository).save(any(Tarefa.class));
    }
}