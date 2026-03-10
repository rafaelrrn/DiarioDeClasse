package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Turno de funcionamento das turmas (ex: Manhã, Tarde, Noite). */
@Entity
@Table(name = "turno")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Turno extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turno")
    private Long idTurno;

    @Column(name = "nome")
    private String nome;
}
