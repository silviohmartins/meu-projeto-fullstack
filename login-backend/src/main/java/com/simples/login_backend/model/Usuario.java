package com.simples.login_backend.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios") // Nome da tabela no banco
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    @Enumerated(EnumType.STRING) // Armazena o Enum como String ("ADMIN" ou "USER") no banco
    private UsuarioRole role;

    // Construtores, Getters e Setters
    public Usuario() {}

    public Usuario(String nome, String email, String senha, UsuarioRole role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public UsuarioRole getRole() { return role; }
    public void setRole(UsuarioRole role) { this.role = role; }

    // Métodos da interface UserDetails do Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // O Spring Security exige o prefixo "ROLE_"
        if (this.role == UsuarioRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    // Para este exemplo, as contas nunca expiram ou são bloqueadas
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}