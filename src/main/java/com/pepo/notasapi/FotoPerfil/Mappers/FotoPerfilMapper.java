package com.pepo.notasapi.FotoPerfil.Mappers;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.FotoPerfil.DTO.FotoPerfilDTO;

public class FotoPerfilMapper {
    
    public static FotoPerfilDTO toDTO(FotoPerfil fotoPerfil) {
        if (fotoPerfil == null) {
            return null;
        }
        
        FotoPerfilDTO dto = new FotoPerfilDTO();
        dto.setId(fotoPerfil.getId());
        dto.setTipoArquivo(fotoPerfil.getTipoArquivo());

        return dto;
    }
    
    public static FotoPerfil toEntity(FotoPerfilDTO dto) {
        if (dto == null) {
            return null;
        }
        
        FotoPerfil fotoPerfil = new FotoPerfil();
        fotoPerfil.setId(dto.getId());
        fotoPerfil.setTipoArquivo(dto.getTipoArquivo());
        return fotoPerfil;
    }
}