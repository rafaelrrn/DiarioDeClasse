package com.diario.de.classe.modules.avaliacao;

import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "aluno_avaliacao")
@Data
@EqualsAndHashCode(callSuper = true)
public class AlunoAvaliacao extends BaseEntity {

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
}
