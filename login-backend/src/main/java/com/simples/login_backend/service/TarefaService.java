package com.simples.login_backend.service;

import com.simples.login_backend.model.Tarefa;
import com.simples.login_backend.model.Usuario;
import com.simples.login_backend.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public Tarefa criarTarefa(String titulo, String descricao, Usuario usuario) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }

        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo(titulo);
        novaTarefa.setDescricao(descricao);
        novaTarefa.setConcluida(false);
        novaTarefa.setUsuario(usuario);

        return tarefaRepository.save(novaTarefa);

    }

    @Transactional(readOnly = true) // Garante que a sessão com o banco fica aberta
    public List<Tarefa> listarTarefasDoUsuario(Usuario usuario) {
        return tarefaRepository.findByUsuario(usuario);
    }
}