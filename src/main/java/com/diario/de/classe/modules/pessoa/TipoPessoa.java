package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** Categoriza o tipo de uma Pessoa: ALUNO, PROFESSOR, RESPONSAVEL, etc. */
@Entity
@Table(name = "tipo_pessoa")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class TipoPessoa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pessoa")
    private Long idTipoPessoa;

    @Column(name = "nome")
    private String nome;
}
