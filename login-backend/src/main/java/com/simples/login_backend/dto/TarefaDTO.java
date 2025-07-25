package com.simples.login_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TarefaDTO(
        @NotBlank String titulo,
        String descricao
) {}