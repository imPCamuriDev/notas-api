package com.pepo.notasapi.Usuarios.Controller;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.DTO.CriadorUsuarioDTO;
import com.pepo.notasapi.Usuarios.DTO.UsuarioDTO;
import com.pepo.notasapi.Usuarios.Mappers.UsuarioMapper;
import com.pepo.notasapi.Usuarios.Service.UsuarioService;
import com.pepo.notasapi.ValueObjects.EmailVO;
import com.pepo.notasapi.ValueObjects.PasswordHashVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
// @SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService us;

    public UsuarioController(UsuarioService usuarioService) {
        this.us = usuarioService;
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados no sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuários retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
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
    @Operation(
            summary = "Buscar usuário por ID",
            description = "Retorna os dados de um usuário específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> buscarUsuario(
            @Parameter(description = "ID do usuário a ser buscado", required = true, example = "1")
            @PathVariable Long id,
            WebRequest request
    ) {
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
    @Operation(
            summary = "Criar novo usuário",
            description = "Cria um novo usuário no sistema com os dados fornecidos. O email deve ser único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou email já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> criarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CriadorUsuarioDTO.class))
            )
            @RequestBody CriadorUsuarioDTO dto,
            WebRequest request
    ) {
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
    @Operation(
            summary = "Deletar usuário",
            description = "Remove permanentemente um usuário do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> deletarUsuario(
            @Parameter(description = "ID do usuário a ser deletado", required = true, example = "1")
            @PathVariable Long id,
            WebRequest request
    ) {
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