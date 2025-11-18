package com.example.dominio.ports.in;

import com.example.dominio.model.Usuario;

/**
 * Puerto de entrada - Define el caso de uso para crear usuarios
 */
public interface CrearUsuarioUseCase {
    Usuario crearUsuario(Usuario usuario);
}