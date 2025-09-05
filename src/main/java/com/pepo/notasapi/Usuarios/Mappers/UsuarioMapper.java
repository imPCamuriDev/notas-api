package com.pepo.notasapi.Usuarios.Mappers;

import java.util.List;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getEmailVerificado()
        );
    }

    public static List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }
    

}
