package com.pepo.notasapi.Usuarios.Service;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;
import com.pepo.notasapi.ValueObjects.EmailVO;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

	private final UsuarioRepository ur;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		this.ur = usuarioRepository;
	}

	public Usuario salvarUsuario(Usuario usuario) {
		return ur.save(usuario);
	}

	public List<UsuarioDTO> listarUsuarios() {
		List<UsuarioDTO> usuarios = UsuarioMapper.toDTOList(ur.findAll());
		return usuarios;
	}

	public UsuarioDTO buscarPorId(Long id) {
		Usuario user = ur.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		return UsuarioMapper.toDTO(user);
	}

	public Usuario buscarUsuarioPorId(Long id) {
		return ur.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
	}

	public void deletarUsuario(Long id) {
		if (!ur.existsById(id)) {
			throw new IllegalArgumentException("Usuário com ID " + id + " não encontrado");
		}
		ur.deleteById(id);
	}

	public boolean existsByEmail(String email) {
		return ur.findByEmail(new EmailVO(email)).isPresent();
	}

}
