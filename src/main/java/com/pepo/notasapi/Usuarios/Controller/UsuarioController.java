package com.pepo.notasapi.Usuarios.Controller;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.CriadorUsuarioDTO;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService us;

    public UsuarioController(UsuarioService usuarioService) {
        this.us = usuarioService;
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios(WebRequest request) {
        try {
            List<UsuarioDTO> usuarios = us.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro ao listar usuários: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Long id, WebRequest request) {
        try {
            UsuarioDTO usuario = us.buscarPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Erro ao buscar usuário: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody CriadorUsuarioDTO dto, WebRequest request) {
        try {
            Usuario user = new Usuario();

            if (us.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email já está em uso");
            }

            user.setNome(dto.getNome());
            user.setEmail(new EmailVO(dto.getEmail()));
            user.setPassword(new PasswordHashVO(dto.getPassword()));
            
            UsuarioDTO usuarioSalvo = UsuarioMapper.toDTO(us.salvarUsuario(user));
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro ao criar usuário: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id, WebRequest request) {
        try {
            us.deletarUsuario(id);
            return ResponseEntity.ok().body("Usuário deletado com sucesso");
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro ao deletar usuário: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }
}