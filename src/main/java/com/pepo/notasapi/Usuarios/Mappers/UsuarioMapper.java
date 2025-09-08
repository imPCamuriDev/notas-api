package com.pepo.notasapi.Usuarios.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.pepo.notasapi.Itens.Mappers.ItemMapper;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;

public class UsuarioMapper {
	
	
	
//    public static UsuarioDTO toDTO(Usuario usuario) {
//        return new UsuarioDTO(
//            usuario.getId(),
//            usuario.getNome(),
//            usuario.getEmail(),
//            usuario.getEmailVerificado(),
//            usuario.getItems()
//        );
//    }
	
	    public static UsuarioDTO toDTO(Usuario usuario) {
	        UsuarioDTO dto = new UsuarioDTO();
	        dto.setId(usuario.getId());
	        dto.setNome(usuario.getNome());
	        dto.setEmail(usuario.getEmail()); // assumindo que EmailVO tem getValue()
	        
	        // Mapear os itens
	        if (usuario.getItems() != null) {
	            dto.setItens(usuario.getItems().stream()
	                .map(ItemMapper::toDTO)
	                .collect(Collectors.toList()));
	        }
	        
	        return dto;
	    }

    public static List<UsuarioDTO> toDTOList(List<Usuario> usuarios) {
        return usuarios.stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }
    

}
