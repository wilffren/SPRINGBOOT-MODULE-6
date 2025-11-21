package com.example.HU4.infrastructure.adapters;

import com.example.HU4.domain.model.User;
import com.example.HU4.domain.ports.out.UserRepositoryPort;
import com.example.HU4.infrastructure.adapters.mappers.UserMapper;
import com.example.HU4.infrastructure.entities.UserEntity;
import com.example.HU4.infrastructure.repositories.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador del repositorio de usuarios
 * Implementa el puerto de salida UserRepositoryPort
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    
    private final UserJpaRepository userRepository;
    private final UserMapper userMapper;
    
    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        log.debug("Usuario guardado: {}", saved.getId());
        return userMapper.toDomain(saved);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
            .map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(userMapper::toDomain);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(userMapper::toDomain);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.debug("Usuario eliminado: {}", id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}