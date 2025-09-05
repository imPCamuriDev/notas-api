package com.pepo.notasapi.Usuarios.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepo.notasapi.Usuarios.Usuario;
import com.pepo.notasapi.ValueObjects.Email;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByEmail(Email email);
}