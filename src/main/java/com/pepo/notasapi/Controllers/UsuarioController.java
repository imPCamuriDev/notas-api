package com.pepo.notasapi.Controllers;

import com.pepo.notasapi.Entities.UsuariosEntity;
import com.pepo.notasapi.Repositories.UsuarioRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepositories usuarioRepository;

    // Criar um novo usuário
    @PostMapping
    public ResponseEntity<UsuariosEntity> createUser(@RequestBody UsuariosEntity usuario) {
        UsuariosEntity savedUser = usuarioRepository.save(usuario);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // Listar todos os usuários
    @GetMapping
    public List<UsuariosEntity> getAllUsers() {
        return usuarioRepository.findAll();
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuariosEntity> getUserById(@PathVariable Integer id) {
        return usuarioRepository.findById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}