package com.simples.login_backend.repository;

import com.simples.login_backend.model.Tarefa;
import com.simples.login_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Método para buscar todas as tarefas de um usuário específico
    List<Tarefa> findByUsuario(Usuario usuario);
}