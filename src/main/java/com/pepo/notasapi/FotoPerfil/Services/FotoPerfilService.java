package com.pepo.notasapi.FotoPerfil.Services;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBase64DTO;
import com.pepo.notasapi.FotoPerfil.Repository.FotoPerfilRepository;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class FotoPerfilService {

    private final FotoPerfilRepository fotoPerfilRepository;
    private final UsuarioService usuarioService;

    public FotoPerfilService(FotoPerfilRepository fotoPerfilRepository, UsuarioService usuarioService) {
        this.fotoPerfilRepository = fotoPerfilRepository;
        this.usuarioService = usuarioService;
    }
    
    public FotoPerfil buscarPorUsuarioId(Long usuarioId) {
        return fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
    }

    public FotoPerfil salvarBase64(Long usuarioId, FotoBase64DTO fotoDTO) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioId);
        
        byte[] imagemBytes = Base64.getDecoder().decode(fotoDTO.getBase64());
        
        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElse(new FotoPerfil());
        
        fotoPerfil.setUsuario(usuario);
        fotoPerfil.setImagem(imagemBytes);
        fotoPerfil.setTipoArquivo(fotoDTO.getTipoArquivo());
        
        return fotoPerfilRepository.save(fotoPerfil);
    }

    public String buscarImagemBase64(Long usuarioId) {
        FotoPerfil fotoPerfil = buscarPorUsuarioId(usuarioId);
        return Base64.getEncoder().encodeToString(fotoPerfil.getImagem());
    }

    public byte[] buscarImagemBytes(Long usuarioId) {
        FotoPerfil fotoPerfil = buscarPorUsuarioId(usuarioId);
        return fotoPerfil.getImagem();
    }

    public void deletar(Long usuarioId) {
        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada"));
        fotoPerfilRepository.delete(fotoPerfil);
    }
}