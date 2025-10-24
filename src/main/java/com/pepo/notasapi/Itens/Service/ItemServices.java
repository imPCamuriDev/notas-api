package com.pepo.notasapi.Itens.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pepo.notasapi.Itens.Item;
import com.pepo.notasapi.Itens.DTO.ItemDTO;
import com.pepo.notasapi.Itens.Mappers.ItemMapper;
import com.pepo.notasapi.Itens.Repositories.ItensRepository;
import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.Usuarios.Repositories.UsuarioRepository;

@Service
public class ItemServices {
    private final ItensRepository ir;
    private final UsuarioRepository ur;

    private ItemMapper im = new ItemMapper();

    public ItemServices(ItensRepository itemRepository, UsuarioRepository ur) {
        this.ir = itemRepository;
        this.ur = ur;
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

    public Item buscarItemEntityPorId(Long id) {
        return ir.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Item com ID " + id + " não encontrado"));
    }

    public void deletarItem(Long id) {
        if (!ir.existsById(id)) {
            throw new IllegalArgumentException("Item com ID " + id + " não encontrado");
        }
        ir.deleteById(id);
    }
    
    @SuppressWarnings("unused")
    public List<ItemDTO> buscarItensPorUsuarioId(Long usuarioId) {
        Usuario usuario = ur.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + usuarioId));
        List<Item> itens = ir.findByUsuarioId(usuarioId);

        return im.toDTOList(itens);
    }

}
