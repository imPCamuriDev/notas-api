package com.pepo.notasapi.Itens.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Service.ItemServices;

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

    
}
