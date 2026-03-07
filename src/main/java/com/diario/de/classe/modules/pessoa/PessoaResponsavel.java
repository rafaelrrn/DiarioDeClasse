package com.diario.de.classe.modules.pessoa;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Associação entre um aluno (Pessoa do tipo ALUNO) e seu responsável legal.
 *
 * Um aluno pode ter múltiplos responsáveis (pai, mãe, tutor), por isso usa
 * uma tabela separada com o campo 'parentesco' para identificar o vínculo.
 */
@Entity
@Table(name = "pessoa_responsavel")
@Data
@ToString
@EntityListeners(AuditingEntityListener.class)
public class PessoaResponsavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa_responsavel")
    private Long idPessoaResponsavel;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    @ManyToOne
    @JoinColumn(name = "id_responsavel", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_responsavel"))
    private Pessoa pessoaResponsavel;

    @Column(name = "parentesco")
    private String parentesco;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
