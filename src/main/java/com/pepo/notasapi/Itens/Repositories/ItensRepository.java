package com.pepo.notasapi.Itens.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepo.notasapi.Itens.Item;

public interface ItensRepository extends JpaRepository<Item, Long>{
    
}
