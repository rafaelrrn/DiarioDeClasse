package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Representa uma instituição de ensino (escola, colégio, etc.). */
@Entity
@Table(name = "instituicao_ensino")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class InstituicaoEnsino extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instituicao_ensino")
    private Long idInstituicaoEnsino;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "codigo_estadual")
    private String codigoEstadual;
}
