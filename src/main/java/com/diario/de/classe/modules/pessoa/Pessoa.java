package com.diario.de.classe.modules.pessoa;

import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Representa qualquer pessoa no sistema (aluno, professor, responsável, etc.).
 *
 * O tipo é determinado pelo relacionamento com TipoPessoa.
 * Alunos são Pessoas do tipo ALUNO; professores são Pessoas do tipo PROFESSOR, etc.
 *
 * Soft delete: ao invés de excluir, o campo 'ativo' é definido como false
 * e 'deletedAt' recebe a data da desativação (herdado de BaseEntity).
 */
@Entity
@Table(name = "pessoa")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long idPessoa;

    @ManyToOne
    @JoinColumn(name = "id_tipo_pessoa", nullable = false, referencedColumnName = "id_tipo_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_tipo_pessoa"))
    private TipoPessoa tipoPessoa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "data_nascimento")
    private String dataNascimento;

    @Column(name = "situacao")
    private String situacao;

    @Column(name = "obs")
    private String obs;
}
