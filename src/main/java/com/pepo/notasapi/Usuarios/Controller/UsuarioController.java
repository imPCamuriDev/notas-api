package com.pepo.notasapi.Usuarios.Controller;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.CriadorUsuarioDTO;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService us;

    public UsuarioController(UsuarioService usuarioService) {
        this.us = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return us.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarUsuario(@PathVariable Long id) {
        return us.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO criarUsuario(@RequestBody CriadorUsuarioDTO dto) {
        Usuario user = new Usuario();

        try {
            user.setNome(dto.getNome());
            user.setEmail(new EmailVO(dto.getEmail()));
            user.setPassword(new PasswordHashVO(dto.getPassword()));
        } catch (IllegalArgumentException e) {
            return null;
        }

        return UsuarioMapper.toDTO(us.salvarUsuario(user));
    }
}
