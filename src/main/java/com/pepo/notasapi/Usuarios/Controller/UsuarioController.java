package com.pepo.notasapi.Usuarios.Controller;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.CriadorUsuarioDTO;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;
import com.pepo.notasapi.ValueObjects.Email;
import com.pepo.notasapi.ValueObjects.PasswordHash;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO criarUsuario(@RequestBody CriadorUsuarioDTO dto) {
        Usuario user = new Usuario();

        try {
            user.setNome(dto.getNome());
            user.setEmail(new Email(dto.getEmail()));
            user.setPassword(new PasswordHash(dto.getPassword()));
        } catch (IllegalArgumentException e) {
            return null;
        }

        return UsuarioMapper.toDTO(usuarioService.salvarUsuario(user));
    }
}
