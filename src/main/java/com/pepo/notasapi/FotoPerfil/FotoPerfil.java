package com.pepo.notasapi.FotoPerfil;

import com.pepo.notasapi.Usuarios.Usuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "foto_perfil")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoPerfil {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @Lob
    private byte[] imagem;
}
