package com.simples.login_backend.controller;

import com.simples.login_backend.dto.TarefaDTO;
import com.simples.login_backend.model.Tarefa;
import com.simples.login_backend.model.Usuario;
import com.simples.login_backend.repository.TarefaRepository;
import com.simples.login_backend.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private TarefaService tarefaService;

    // CREATE - Criar uma nova tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody @Valid TarefaDTO tarefaDTO, @AuthenticationPrincipal Usuario usuario) {
        //Tarefa novaTarefa = new Tarefa();
        //novaTarefa.setTitulo(tarefaDTO.titulo());
        //novaTarefa.setDescricao(tarefaDTO.descricao());
        //novaTarefa.setConcluida(false);
        //novaTarefa.setUsuario(usuario); // Associa a tarefa ao usuário logado

        Tarefa tarefaSalva = tarefaService.criarTarefa(tarefaDTO.titulo(), tarefaDTO.descricao(), usuario);
        return new ResponseEntity<>(tarefaSalva, HttpStatus.CREATED);
    }

    // READ - Listar todas as tarefas do usuário logado
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTarefasDoUsuario(@AuthenticationPrincipal Usuario usuario) {
        List<Tarefa> tarefas = tarefaService.listarTarefasDoUsuario(usuario);
        return ResponseEntity.ok(tarefas);
    }

    // NOVO MÉTODO SÓ PARA ADMINS
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')") // A mágica acontece aqui!
    public ResponseEntity<List<Tarefa>> listarTodasAsTarefas() {
        List<Tarefa> todasAsTarefas = tarefaRepository.findAll();
        return ResponseEntity.ok(todasAsTarefas);
    }

    // UPDATE - Atualizar uma tarefa (marcar como concluída)
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        // VERIFICAÇÃO DE SEGURANÇA: Garante que o usuário só pode alterar suas próprias tarefas
        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não autorizado a alterar esta tarefa");
        }

        tarefa.setConcluida(!tarefa.isConcluida()); // Inverte o estado de 'concluida'
        Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
        return ResponseEntity.ok(tarefaAtualizada);
    }

    // DELETE - Apagar uma tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada"));

        // VERIFICAÇÃO DE SEGURANÇA
        if (!tarefa.getUsuario().getId().equals(usuario.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não autorizado a deletar esta tarefa");
        }

        tarefaRepository.delete(tarefa);
        return ResponseEntity.noContent().build();
    }
}