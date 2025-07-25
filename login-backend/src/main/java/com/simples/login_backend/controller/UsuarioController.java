package com.simples.login_backend.controller;

import com.simples.login_backend.dto.UsuarioResponseDTO;
import com.simples.login_backend.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(@AuthenticationPrincipal Usuario usuario) {
        // A anotação @AuthenticationPrincipal injeta o usuário autenticado diretamente.
        // O Spring Security faz a mágica de pegar o usuário do token JWT e nos entregar aqui.

        // Mapeia a entidade Usuario para o nosso DTO de resposta
        UsuarioResponseDTO response = new UsuarioResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getRole());

        return ResponseEntity.ok(response);
    }
}