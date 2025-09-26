package com.pepo.notasapi.FotoPerfil.Repository;

import com.pepo.notasapi.FotoPerfil.FotoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FotoPerfilRepository extends JpaRepository<FotoPerfil, Long> {
    Optional<FotoPerfil> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}