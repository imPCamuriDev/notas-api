package com.pepo.notasapi.Itens.Service;

import org.springframework.stereotype.Service;

import com.pepo.notasapi.Itens.Item;
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
}
