package com.simples.login_backend.dto;

import com.simples.login_backend.model.UsuarioRole;

// DTO que representa os dados que enviaremos de volta para o frontend
public record UsuarioResponseDTO(Long id, String nome, String email, UsuarioRole role) {
}