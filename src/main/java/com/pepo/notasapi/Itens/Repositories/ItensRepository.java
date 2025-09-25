package com.pepo.notasapi.Itens.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pepo.notasapi.Itens.Item;

public interface ItensRepository extends JpaRepository<Item, Long>{
    @Query("SELECT i FROM Item i WHERE i.usuario.id = :usuarioId")
    List<Item> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
