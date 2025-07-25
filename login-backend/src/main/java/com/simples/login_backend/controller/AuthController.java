package com.simples.login_backend.controller;

import com.simples.login_backend.dto.CadastroDTO;
import com.simples.login_backend.dto.LoginDTO;
import com.simples.login_backend.dto.LoginResponseDTO;
import com.simples.login_backend.model.Usuario;
import com.simples.login_backend.repository.UsuarioRepository;
import com.simples.login_backend.security.TokenService;
import com.simples.login_backend.model.UsuarioRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://login-frontend-0jio.onrender.com", allowedHeaders = "https://login-frontend-0jio.onrender.com") // Permite acesso de qualquer origem
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var usuario = (Usuario) auth.getPrincipal();
        var token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody @Valid CadastroDTO data) {
        if (this.usuarioRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
        }

        String senhaCriptografada = passwordEncoder.encode(data.senha());
        Usuario novoUsuario = new Usuario(data.nome(), data.email(), senhaCriptografada, UsuarioRole.USER);

        this.usuarioRepository.save(novoUsuario);

        return ResponseEntity.ok().body("Usuário cadastrado com sucesso!");
    }
}