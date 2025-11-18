package com.example.infraestructura.adapters.out.jpa.mapper;

import com.example.dominio.model.Usuario;
import com.example.infraestructura.adapters.out.jpa.entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper con MapStruct para convertir entre:
 * - Usuario (modelo de dominio) 
 * - UsuarioEntity (entidad de persistencia)
 */
@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    Usuario toDomain(UsuarioEntity entity);

    UsuarioEntity toEntity(Usuario domain);
}