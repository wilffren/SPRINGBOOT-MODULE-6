package com.example.HU4.domain.ports.out;

import com.example.HU4.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida - Repositorio de Usuarios
 * Define el contrato para persistencia de usuarios
 */
public interface UserRepositoryPort {
    
    User save(User user);
    
    Optional<User> findById(Long id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    List<User> findAll();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
}