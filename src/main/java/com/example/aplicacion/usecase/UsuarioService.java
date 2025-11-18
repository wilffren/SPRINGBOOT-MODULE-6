package com.example.aplicacion.usecase;

import com.example.dominio.model.Usuario;
import com.example.dominio.ports.in.CrearUsuarioUseCase;
import com.example.dominio.ports.in.ObtenerUsuarioUseCase;
import com.example.dominio.ports.in.EliminarUsuarioUseCase;
import com.example.dominio.ports.out.UsuarioRepositoryPort;

import java.util.List;
import java.util.Optional;

/**
 * Implementación de los casos de uso de Usuario
 * Inyecta dependencias mediante INTERFACES (Dependency Inversion)
 */
public class UsuarioService implements 
    CrearUsuarioUseCase, 
    ObtenerUsuarioUseCase, 
    EliminarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UsuarioService(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        // Lógica de negocio pura (sin anotaciones de Spring)
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        return usuarioRepositoryPort.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepositoryPort.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerPorUsername(String username) {
        return usuarioRepositoryPort.findByUsername(username);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepositoryPort.findAll();
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepositoryPort.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepositoryPort.deleteById(id);
    }
}