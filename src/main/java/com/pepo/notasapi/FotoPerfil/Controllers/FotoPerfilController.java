package com.pepo.notasapi.FotoPerfil.Controllers;

import com.pepo.notasapi.Exceptions.ErrorResponse;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBase64DTO;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBinarioDTO;
import com.pepo.notasapi.FotoPerfil.Services.FotoPerfilService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/fotos")
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PostMapping(value = "/usuarios/{id}/foto", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        FotoBinarioDTO dto = new FotoBinarioDTO(file.getBytes(), file.getContentType());
        fotoPerfilService.salvarBinario(id, dto);

        return ResponseEntity.ok("Foto salva com sucesso!");
    }

    // Download Imagem Direta
    @GetMapping("/usuarios/{id}/foto")
    public ResponseEntity<byte[]> buscarFoto(@PathVariable Long id) {
        byte[] imagem = fotoPerfilService.buscarImagemBytes(id);
        String tipoArquivo = fotoPerfilService.buscarPorUsuarioId(id).getTipoArquivo();

        return ResponseEntity.ok()
                .header("Content-Type", tipoArquivo)
                .body(imagem);
    }

    // Deletar Foto
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<?> deletarFoto(@PathVariable Long usuarioId, WebRequest request) {
        try {
            fotoPerfilService.deletar(usuarioId);
            return ResponseEntity.ok("Foto deletada com sucesso");
        } catch (RuntimeException e) {
            return criarErro(HttpStatus.NOT_FOUND, e.getMessage(), request);
        } catch (Exception e) {
            return criarErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar foto: " + e.getMessage(), request);
        }
    }

    // Método auxiliar para criar respostas de erro
    private ResponseEntity<ErrorResponse> criarErro(HttpStatus status, String mensagem, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getDescription(false).replace("uri=", ""));
        return ResponseEntity.status(status).body(error);
    }
}