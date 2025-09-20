package com.pepo.notasapi.FotoPerfil.Controllers;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBase64DTO;
import com.pepo.notasapi.FotoPerfil.Services.FotoPerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/fotos")
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PostMapping("/{usuarioId}/base64")
    public ResponseEntity<?> uploadFotoBase64(@PathVariable Long usuarioId, @RequestBody FotoBase64DTO fotoDTO, WebRequest request) {
        try {
            if (fotoDTO.getBase64() == null || fotoDTO.getBase64().isEmpty()) {
                ErrorResponse error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request",
                    "Base64 é obrigatório",
                    request.getDescription(false).replace("uri=", "")
                );
                return ResponseEntity.badRequest().body(error);
            }

            fotoPerfilService.salvarBase64(usuarioId, fotoDTO);
            return ResponseEntity.ok("Foto salva com sucesso");

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
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                "Erro ao salvar foto: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/{usuarioId}/base64")
    public ResponseEntity<?> downloadFotoBase64(@PathVariable Long usuarioId, WebRequest request) {
        try {
            String base64 = fotoPerfilService.buscarImagemBase64(usuarioId);
            FotoPerfil fotoPerfil = fotoPerfilService.buscarPorUsuarioId(usuarioId);

            FotoBase64DTO response = new FotoBase64DTO(base64, fotoPerfil.getTipoArquivo());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
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
                "Erro ao buscar foto: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping(value = "/{usuarioId}/imagem", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<?> downloadFoto(@PathVariable Long usuarioId, WebRequest request) {
        try {
            byte[] imagem = fotoPerfilService.buscarImagemBytes(usuarioId);
            return ResponseEntity.ok(imagem);
        } catch (RuntimeException e) {
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
                "Erro ao buscar imagem: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarFoto(@PathVariable Long usuarioId, WebRequest request) {
        try {
            fotoPerfilService.deletar(usuarioId);
            return ResponseEntity.ok("Foto deletada");
        } catch (RuntimeException e) {
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
                "Erro ao deletar foto: " + e.getMessage(),
                request.getDescription(false).replace("uri=", "")
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}