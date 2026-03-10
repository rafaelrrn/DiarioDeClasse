package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.calendario.CalendarioEscolar;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "aluno_frequencia")
@Data
@EqualsAndHashCode(callSuper = true)
public class AlunoFrequencia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_frequencia")
    private Long idAlunoFrequencia;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    @ManyToOne
    @JoinColumn(name = "id_calendario_escolar", nullable = false, referencedColumnName = "id_calendario_escolar",
            foreignKey = @ForeignKey(name = "fk_id_calendario_escolar"))
    private CalendarioEscolar calendarioEscolar;

    @Column(name = "faltas")
    private String faltas;
}
