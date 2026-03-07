package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.instituicao.Curso;
import com.diario.de.classe.modules.instituicao.InstituicaoEnsino;
import com.diario.de.classe.modules.instituicao.Turno;
import com.diario.de.classe.modules.pessoa.Pessoa;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "classe")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe")
    private Long idClasse;

    @ManyToOne
    @JoinColumn(name = "id_instituicao_ensino", nullable = false, referencedColumnName = "id_instituicao_ensino",
            foreignKey = @ForeignKey(name = "fk_id_instituicao_ensino"))
    private InstituicaoEnsino instituicaoEnsino;

    @ManyToOne
    @JoinColumn(name = "id_componente_curricular", referencedColumnName = "id_componente_curricular",
            foreignKey = @ForeignKey(name = "fk_id_componente_curricular"))
    private ComponenteCurricular componenteCurricular;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false, referencedColumnName = "id_curso",
            foreignKey = @ForeignKey(name = "fk_id_curso"))
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "id_turno", nullable = false, referencedColumnName = "id_turno",
            foreignKey = @ForeignKey(name = "fk_id_turno"))
    private Turno turno;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false, referencedColumnName = "id_turma",
            foreignKey = @ForeignKey(name = "fk_id_turma"))
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_professor"))
    private Pessoa pessoa;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
