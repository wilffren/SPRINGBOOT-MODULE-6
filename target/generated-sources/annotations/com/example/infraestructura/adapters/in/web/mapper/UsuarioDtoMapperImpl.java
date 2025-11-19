package com.example.infraestructura.adapters.in.web.mapper;

import com.example.dominio.model.Usuario;
import com.example.infraestructura.adapters.in.web.dto.UsuarioRequest;
import com.example.infraestructura.adapters.in.web.dto.UsuarioResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-19T17:06:17-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class UsuarioDtoMapperImpl implements UsuarioDtoMapper {

    @Override
    public Usuario toDomain(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setUsername( request.getUsername() );
        usuario.setPassword( request.getPassword() );
        usuario.setRole( request.getRole() );

        return usuario;
    }

    @Override
    public UsuarioResponse toResponse(Usuario domain) {
        if ( domain == null ) {
            return null;
        }

        UsuarioResponse usuarioResponse = new UsuarioResponse();

        usuarioResponse.setId( domain.getId() );
        usuarioResponse.setUsername( domain.getUsername() );
        usuarioResponse.setRole( domain.getRole() );

        return usuarioResponse;
    }
}
