package com.pepo.notasapi.FotoPerfil.Repository;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FotoPerfilRepository extends JpaRepository<FotoPerfil, Long> {
    Optional<FotoPerfil> findByUsuarioId(Long usuarioId);
}