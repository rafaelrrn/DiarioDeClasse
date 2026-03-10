package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Nível de ensino (ex: Ensino Fundamental, Ensino Médio). */
@Entity
@Table(name = "ensino")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Ensino extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ensino")
    private Long idEnsino;

    @Column(name = "nome")
    private String nome;
}
