package com.example.infraestructura.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para recibir datos de usuario desde la API REST
 */
public class UsuarioRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    private String role;

    public UsuarioRequest() {
    }

    public UsuarioRequest(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}