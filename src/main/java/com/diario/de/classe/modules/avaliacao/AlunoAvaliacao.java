package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "aluno_avaliacao")
@Data
@EntityListeners(AuditingEntityListener.class)
public class AlunoAvaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_avaliacao")
    private Long idAlunoAvaliacao;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    @ManyToOne
    @JoinColumn(name = "id_avaliacao", nullable = false, referencedColumnName = "id_avaliacao",
            foreignKey = @ForeignKey(name = "fk_id_avaliacao"))
    private Avaliacao avaliacao;

    @Column(name = "nota")
    private Float nota;

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
