package com.diario.de.classe.modules.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Representa qualquer pessoa no sistema (aluno, professor, responsável, etc.).
 *
 * O tipo é determinado pelo relacionamento com TipoPessoa.
 * Alunos são Pessoas do tipo ALUNO; professores são Pessoas do tipo PROFESSOR, etc.
 *
 * TODO (Etapa 2): Migrar para estender BaseEntity com campos ativo/deletedAt.
 */
@Entity
@Table(name = "pessoa")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Pessoa {

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

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
