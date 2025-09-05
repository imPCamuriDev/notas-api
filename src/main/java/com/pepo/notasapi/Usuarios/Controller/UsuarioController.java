package com.pepo.notasapi.Usuarios.Controller;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.CriadorUsuarioDTO;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;

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
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO criarUsuario(@RequestBody CriadorUsuarioDTO dto) {
        Usuario user = new Usuario();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        Usuario salvo = usuarioService.salvarUsuario(user);
        return UsuarioMapper.toDTO(salvo);
    }
}
