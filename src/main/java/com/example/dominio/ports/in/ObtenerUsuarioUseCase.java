package com.example.dominio.ports.in;

import com.example.dominio.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada - Define los casos de uso para consultar usuarios
 */
public interface ObtenerUsuarioUseCase {
    
    Optional<Usuario> obtenerPorId(Long id);
    
    Optional<Usuario> obtenerPorUsername(String username);
    
    List<Usuario> obtenerTodos();
}