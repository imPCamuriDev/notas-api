package com.pepo.notasapi.Itens.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Mappers.ItemMapper;
import com.pepo.notasapi.Itens.Service.ItemServices;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemServices is;
    private final UsuarioRepository ur;

    public ItemController(ItemServices itemService, UsuarioRepository ur) {
        this.is = itemService;
		this.ur = ur;
    }

    @GetMapping
    public List<ItemDTO> listarItens() {
        return is.getItens();
    }


    @GetMapping("/{id}")
    public ItemDTO buscarItem(@PathVariable Long id) {
        return is.buscarPorId(id);
    }


    @PostMapping
    public ResponseEntity<?> criarItem(@RequestBody ItemDTO dto) {

        try {
            Item item = new Item();
            item.setDataCriacao(dto.getDataCriacao());
            item.setDataLimite(dto.getDataLimite());
            item.setDescricao(dto.getDescricao());

            Usuario usuario = ur.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + dto.getUsuarioId() + " não encontrado"));
            
            item.setUsuario(usuario);
            Item itemSalvo = is.salvarItem(item);
            
            return ResponseEntity.ok(ItemMapper.toDTO(itemSalvo));
                
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno: " + e.getMessage());
        }
    }
}

