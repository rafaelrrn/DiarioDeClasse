package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Grau de ensino (ex: 1º grau, 2º grau). */
@Entity
@Table(name = "grau")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Grau extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grau")
    private Long idGrau;

    @Column(name = "nome")
    private String nome;
}
