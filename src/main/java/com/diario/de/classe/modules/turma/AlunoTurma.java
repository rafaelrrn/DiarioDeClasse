package com.diario.de.classe.modules.turma;

import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "aluno_turma")
@Data
@EqualsAndHashCode(callSuper = true)
public class AlunoTurma extends BaseEntity {

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
}
