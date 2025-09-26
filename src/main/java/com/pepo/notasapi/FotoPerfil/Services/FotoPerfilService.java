package com.pepo.notasapi.FotoPerfil.Services;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBase64DTO;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBinarioDTO;
import com.pepo.notasapi.FotoPerfil.Repository.FotoPerfilRepository;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class FotoPerfilService {

    private final FotoPerfilRepository fotoPerfilRepository;
    private final UsuarioRepository usuarioRepository;

    public FotoPerfilService(FotoPerfilRepository fotoPerfilRepository, UsuarioRepository usuarioRepository) {
        this.fotoPerfilRepository = fotoPerfilRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Salvar binário diretamente
    public void salvarBinario(Long usuarioId, FotoBinarioDTO fotoDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElse(new FotoPerfil());

        fotoPerfil.setUsuario(usuario);
        fotoPerfil.setDadosBinarios(fotoDTO.getDadosBinarios());
        fotoPerfil.setTipoArquivo(fotoDTO.getTipoArquivo());

        fotoPerfilRepository.save(fotoPerfil);
    }

    // Buscar binário
    public FotoBinarioDTO buscarBinario(Long usuarioId) {
        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada para o usuário: " + usuarioId));

        return new FotoBinarioDTO(fotoPerfil.getDadosBinarios(), fotoPerfil.getTipoArquivo());
    }

    // Método para buscar bytes da imagem (para o endpoint /imagem)
    public byte[] buscarImagemBytes(Long usuarioId) {
        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada para o usuário: " + usuarioId));

        return fotoPerfil.getDadosBinarios();
    }

    // Buscar entidade FotoPerfil completa
    public FotoPerfil buscarPorUsuarioId(Long usuarioId) {
        return fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada para o usuário: " + usuarioId));
    }

    // Verificar se existe foto para o usuário
    public boolean existeFoto(Long usuarioId) {
        return fotoPerfilRepository.existsByUsuarioId(usuarioId);
    }

    // Deletar foto
    public void deletar(Long usuarioId) {
        FotoPerfil fotoPerfil = fotoPerfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada para o usuário: " + usuarioId));
        fotoPerfilRepository.delete(fotoPerfil);
    }

    // Deletar por ID do usuário (alternativo)
    public void deletarPorUsuarioId(Long usuarioId) {
        if (!fotoPerfilRepository.existsByUsuarioId(usuarioId)) {
            throw new RuntimeException("Foto não encontrada para o usuário: " + usuarioId);
        }
        fotoPerfilRepository.deleteByUsuarioId(usuarioId);
    }
}