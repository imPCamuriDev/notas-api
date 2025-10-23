package com.pepo.notasapi.Itens.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.CriadorDeItens;
import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Service.ItemServices;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/itens")
@Tag(name = "Itens", description = "Endpoints para gerenciamento de itens/tarefas dos usuários")
// @SecurityRequirement(name = "bearerAuth")
public class ItemController {

    private final ItemServices is;
    private final UsuarioRepository ur;

    public ItemController(ItemServices itemService, UsuarioRepository ur) {
        this.is = itemService;
        this.ur = ur;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar item por ID",
            description = "Retorna os detalhes de um item específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> buscarItem(
            @Parameter(description = "ID do item a ser buscado", required = true, example = "1")
            @PathVariable Long id,
            WebRequest request
    ) {
        try {
            ItemDTO item = is.buscarPorId(id);
            return ResponseEntity.ok(item);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "Erro ao buscar item: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Buscar itens por usuário",
            description = "Retorna uma lista com todos os itens pertencentes a um usuário específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de itens retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class)
                    )
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
    public ResponseEntity<?> buscarItensPorUsuario(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long usuarioId,
            WebRequest request
    ) {
        try {
            List<ItemDTO> itens = is.buscarItensPorUsuarioId(usuarioId);
            return ResponseEntity.ok(itens);
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao buscar itens do usuário: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping
    @Operation(
            summary = "Criar novo item",
            description = "Cria um novo item/tarefa associado a um usuário com os dados fornecidos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Item criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ItemDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou usuário não encontrado",
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
    public ResponseEntity<?> criarItem(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do item a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CriadorDeItens.class))
            )
            @Valid @RequestBody CriadorDeItens dto,
            WebRequest request
    ) {
        try {
            Item item = new Item();
            item.setDataLimite(dto.getDataLimite());
            item.setDescricao(dto.getDescricao());

            Usuario usuario = ur.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuário com ID " + dto.getUsuarioId() + " não encontrado"));

            item.setUsuario(usuario);
            Item itemSalvo = is.salvarItem(item);

            ItemDTO responseDTO = new ItemDTO(
                    itemSalvo.getId(),
                    itemSalvo.getDescricao(),
                    itemSalvo.getDataCriacao(),
                    itemSalvo.getDataLimite(),
                    itemSalvo.getUsuario().getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    "Erro ao criar item: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar item",
            description = "Remove permanentemente um item do sistema baseado no ID fornecido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Item deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> deletarItem(
            @Parameter(description = "ID do item a ser deletado", required = true, example = "1")
            @PathVariable Long id,
            WebRequest request
    ) {
        try {
            is.deletarItem(id);
            return ResponseEntity.ok().body("Item deletado com sucesso");
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Not Found",
                    "Erro ao deletar item: " + e.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}