package com.pepo.notasapi.Itens.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Mappers.ItemMapper;
import com.pepo.notasapi.Itens.Service.ItemServices;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemServices is;

    public ItemController(ItemServices itemService) {
        this.is = itemService;
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
    public ItemDTO criarItem(@RequestBody ItemDTO dto) {
        Item item = new Item();

        try {
            item.setDataCriacao(dto.getDataCriacao());
            item.setDataLimite(dto.getDataLimite());
            item.setDescricao(dto.getDescricao());
            item.setUsuario(dto.getUsuario_id());
        } catch (IllegalArgumentException e) {
            return null;
        }

        return ItemMapper.toDTO(is.salvarItem(item));
    }
}

