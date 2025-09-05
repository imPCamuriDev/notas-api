package com.pepo.notasapi.Usuarios.Service;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(Usuario usuario) {    	
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public UsuarioDTO buscarPorId(Long id) {
    	Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return UsuarioMapper.toDTO(user);
    }
}
