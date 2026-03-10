package com.diario.de.classe.modules.turma;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "turma")
@Data
@EqualsAndHashCode(callSuper = true)
public class Turma extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma")
    private Long idTurma;

    @Column(name = "nome")
    private String nome;
}
