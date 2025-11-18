package com.example.dominio.ports.out;

import com.example.dominio.model.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida - Define las operaciones de persistencia necesarias
 * Interfaz que será implementada por el adaptador JPA
 */
public interface UsuarioRepositoryPort {
    
    Usuario save(Usuario usuario);
    
    Optional<Usuario> findById(Long id);
    
    Optional<Usuario> findByUsername(String username);
    
    List<Usuario> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}