package com.pepo.notasapi.Itens.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Mappers.ItemMapper;
import com.pepo.notasapi.Itens.Repositories.ItensRepository;

@Service
public class ItemServices {
    private final ItensRepository ir;

    public ItemServices(ItensRepository itemRepository) {
        this.ir = itemRepository;
    }

    public Item salvarItem(Item item) {
        return ir.save(item);
    }

    public List<ItemDTO> getItens() {
        return ItemMapper.toDTOList(ir.findAll());
    }

    public ItemDTO buscarPorId(Long id) {
        return ItemMapper.toDTO(ir.findById(id).orElse(null));
    }
    
    public void deletarItem(Long id) {
        if (!ir.existsById(id)) {
            throw new IllegalArgumentException("Item com ID " + id + " não encontrado");
        }
        ir.deleteById(id);
    }

}
