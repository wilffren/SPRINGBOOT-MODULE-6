package com.example.infraestructura.adapters.in.web.dto;

/**
 * DTO para enviar datos de usuario a través de la API REST
 */
public class UsuarioResponse {

    private Long id;
    private String username;
    private String role;

    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}