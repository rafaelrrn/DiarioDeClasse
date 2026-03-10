package com.diario.de.classe.modules.instituicao;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Série/ano escolar (ex: 1ª série, 2º ano). */
@Entity
@Table(name = "serie")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Serie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_serie")
    private Long idSerie;

    @Column(name = "nome")
    private String nome;
}
