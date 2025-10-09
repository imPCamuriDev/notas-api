package com.pepo.notasapi.FotoPerfil.Controllers;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBinarioDTO;
import com.pepo.notasapi.FotoPerfil.Services.FotoPerfilService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fotos")
@Tag(name = "Fotos de Perfil", description = "Endpoints para gerenciamento de fotos de perfil dos usuários")
@SecurityRequirement(name = "bearerAuth")
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PostMapping(value = "/usuarios/{id}/foto", consumes = "multipart/form-data")
    @Operation(
            summary = "Upload de foto de perfil",
            description = "Faz upload de uma nova foto de perfil para o usuário especificado. Aceita formatos de imagem comuns (JPEG, PNG, GIF, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto salva com sucesso",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Arquivo inválido ou formato não suportado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Erro ao processar o arquivo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<String> uploadFoto(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(
                    description = "Arquivo de imagem a ser enviado",
                    required = true,
                    content = @Content(mediaType = "multipart/form-data")
            )
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        FotoBinarioDTO dto = new FotoBinarioDTO(file.getBytes(), file.getContentType());
        fotoPerfilService.salvarBinario(id, dto);

        return ResponseEntity.ok("Foto salva com sucesso!");
    }

    @GetMapping("/usuarios/{id}/foto")
    @Operation(
            summary = "Buscar foto de perfil",
            description = "Retorna a foto de perfil de um usuário específico como arquivo binário. A imagem é retornada no formato original em que foi enviada."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto retornada com sucesso",
                    content = {
                            @Content(mediaType = "image/jpeg"),
                            @Content(mediaType = "image/png"),
                            @Content(mediaType = "image/gif")
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foto ou usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<byte[]> buscarFoto(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long id
    ) {
        byte[] imagem = fotoPerfilService.buscarImagemBytes(id);
        String tipoArquivo = fotoPerfilService.buscarPorUsuarioId(id).getTipoArquivo();

        return ResponseEntity.ok()
                .header("Content-Type", tipoArquivo)
                .body(imagem);
    }

    @DeleteMapping("/{usuarioId}")
    @Operation(
            summary = "Deletar foto de perfil",
            description = "Remove permanentemente a foto de perfil de um usuário específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Foto deletada com sucesso",
                    content = @Content(mediaType = "text/plain")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Foto ou usuário não encontrado",
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
    public ResponseEntity<?> deletarFoto(
            @Parameter(description = "ID do usuário", required = true, example = "1")
            @PathVariable Long usuarioId,
            WebRequest request
    ) {
        try {
            fotoPerfilService.deletar(usuarioId);
            return ResponseEntity.ok("Foto deletada com sucesso");
        } catch (RuntimeException e) {
            return criarErro(HttpStatus.NOT_FOUND, e.getMessage(), request);
        } catch (Exception e) {
            return criarErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar foto: " + e.getMessage(), request);
        }
    }

    private ResponseEntity<ErrorResponse> criarErro(HttpStatus status, String mensagem, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(error);
    }
}