package com.pepo.notasapi.Repositories;

import com.pepo.notasapi.Entities.UsuariosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositories extends JpaRepository<UsuariosEntity, Integer> {
}