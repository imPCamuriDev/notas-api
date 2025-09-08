package com.pepo.notasapi.FotoPerfil.Controllers;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import com.pepo.notasapi.FotoPerfil.DTO.FotoBase64DTO;
import com.pepo.notasapi.FotoPerfil.Services.FotoPerfilService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fotos")
public class FotoPerfilController {

    private final FotoPerfilService fotoPerfilService;

    public FotoPerfilController(FotoPerfilService fotoPerfilService) {
        this.fotoPerfilService = fotoPerfilService;
    }

    @PostMapping("/{usuarioId}/base64")
    public ResponseEntity<String> uploadFotoBase64(
            @PathVariable Long usuarioId,
            @RequestBody FotoBase64DTO fotoDTO) {
        
        try {
            if (fotoDTO.getBase64() == null || fotoDTO.getBase64().isEmpty()) {
                return ResponseEntity.badRequest().body("Base64 é obrigatório");
            }

            fotoPerfilService.salvarBase64(usuarioId, fotoDTO);
            return ResponseEntity.ok("Foto salva com sucesso");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar foto: " + e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}/base64")
    public ResponseEntity<?> downloadFotoBase64(@PathVariable Long usuarioId) {
        try {
            String base64 = fotoPerfilService.buscarImagemBase64(usuarioId);
            
            FotoPerfil fotoPerfil = fotoPerfilService.buscarPorUsuarioId(usuarioId);
            
            FotoBase64DTO response = new FotoBase64DTO(base64, fotoPerfil.getTipoArquivo());
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{usuarioId}/imagem", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadFoto(@PathVariable Long usuarioId) {
        try {
            byte[] imagem = fotoPerfilService.buscarImagemBytes(usuarioId);
            return ResponseEntity.ok(imagem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<String> deletarFoto(@PathVariable Long usuarioId) {
        try {
            fotoPerfilService.deletar(usuarioId);
            return ResponseEntity.ok("Foto deletada");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}