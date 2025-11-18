package com.example.infraestructura.adapters.in.web.mapper;

import com.example.dominio.model.Usuario;
import com.example.infraestructura.adapters.in.web.dto.UsuarioRequest;
import com.example.infraestructura.adapters.in.web.dto.UsuarioResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre DTOs y modelo de dominio
 */
@Mapper(componentModel = "spring")
public interface UsuarioDtoMapper {

    @Mapping(target = "id", ignore = true)
    Usuario toDomain(UsuarioRequest request);

    UsuarioResponse toResponse(Usuario domain);
}