package com.example.infraestructura.adapters.in.web;

import com.example.dominio.model.Usuario;
import com.example.dominio.ports.in.CrearUsuarioUseCase;
import com.example.dominio.ports.in.ObtenerUsuarioUseCase;
import com.example.dominio.ports.in.EliminarUsuarioUseCase;
import com.example.infraestructura.adapters.in.web.dto.UsuarioRequest;
import com.example.infraestructura.adapters.in.web.dto.UsuarioResponse;
import com.example.infraestructura.adapters.in.web.mapper.UsuarioDtoMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador REST que expone los endpoints HTTP
 * Implementa el patrón Adapter convirtiendo HTTP a casos de uso
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioRestAdapter {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ObtenerUsuarioUseCase obtenerUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;
    private final UsuarioDtoMapper dtoMapper;

    public UsuarioRestAdapter(
            CrearUsuarioUseCase crearUsuarioUseCase,
            ObtenerUsuarioUseCase obtenerUsuarioUseCase,
            EliminarUsuarioUseCase eliminarUsuarioUseCase,
            UsuarioDtoMapper dtoMapper) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.obtenerUsuarioUseCase = obtenerUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = dtoMapper.toDomain(request);
        Usuario creado = crearUsuarioUseCase.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(dtoMapper.toResponse(creado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        return obtenerUsuarioUseCase.obtenerPorId(id)
                .map(usuario -> ResponseEntity.ok(dtoMapper.toResponse(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {
        List<UsuarioResponse> usuarios = obtenerUsuarioUseCase.obtenerTodos()
                .stream()
                .map(dtoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarUsuarioUseCase.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}