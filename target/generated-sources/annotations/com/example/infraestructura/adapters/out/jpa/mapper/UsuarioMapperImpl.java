package com.example.infraestructura.adapters.out.jpa.mapper;

import com.example.dominio.model.Usuario;
import com.example.infraestructura.adapters.out.jpa.entity.UsuarioEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T17:06:17-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public Usuario toDomain(UsuarioEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( entity.getId() );
        usuario.setUsername( entity.getUsername() );
        usuario.setPassword( entity.getPassword() );
        usuario.setRole( entity.getRole() );

        return usuario;
    }

    @Override
    public UsuarioEntity toEntity(Usuario domain) {
        if ( domain == null ) {
            return null;
        }

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setId( domain.getId() );
        usuarioEntity.setUsername( domain.getUsername() );
        usuarioEntity.setPassword( domain.getPassword() );
        usuarioEntity.setRole( domain.getRole() );

        return usuarioEntity;
    }
}
