package com.pepo.notasapi.Itens.Mappers;

import java.util.List;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.ItemDTO;


public class ItemMapper {
    public static ItemDTO toDTO(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getDescricao(),
            item.getDataCriacao(),
            item.getDataLimite(),
            item.getUsuario()
        );
    }

    public static List<ItemDTO> toDTOList(List<Item> itens) {
        return itens.stream()
                .map(ItemMapper::toDTO)
                .toList();
    }
    

}
