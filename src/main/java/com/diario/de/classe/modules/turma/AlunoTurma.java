package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "aluno_turma")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AlunoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_turma")
    private Long idAlunoTurma;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false, referencedColumnName = "id_turma",
            foreignKey = @ForeignKey(name = "fk_id_turma"))
    private Turma turma;

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
