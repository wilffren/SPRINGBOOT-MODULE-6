package com.example.dominio.model;

/**
 * Modelo de dominio puro - SIN anotaciones de JPA ni Spring
 * Representa la entidad Usuario en la capa de negocio
 */
public class Usuario {
    
    private Long id;
    private String username;
    private String password;
    private String role;

    public Usuario() {
    }

    public Usuario(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
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