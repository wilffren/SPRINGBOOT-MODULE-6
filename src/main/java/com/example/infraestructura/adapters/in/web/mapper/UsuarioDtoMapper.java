package com.example.infraestructura.adapters.in.web.mapper;

import com.example.dominio.model.Usuario;
import com.example.infraestructura.adapters.in.web.dto.UsuarioRequest;
import com.example.infraestructura.adapters.in.web.dto.UsuarioResponse;
import org.mapstruct.Mapper;

/**
 * Mapper para convertir entre DTOs y modelo de dominio
 */
@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    Usuario toDomain(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario domain);
}