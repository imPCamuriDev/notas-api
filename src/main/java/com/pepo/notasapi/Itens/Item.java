package com.pepo.notasapi.Itens;

import com.pepo.notasapi.Usuarios.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime dataLimite;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
