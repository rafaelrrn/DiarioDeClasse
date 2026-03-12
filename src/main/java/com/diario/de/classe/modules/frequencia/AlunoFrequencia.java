package com.diario.de.classe.modules.frequencia;

import com.diario.de.classe.modules.calendario.CalendarioEscolar;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Registro de frequência de um aluno em uma aula específica.
 *
 * <p>Cada instância representa a ocorrência de <strong>um aluno</strong>
 * em <strong>uma entrada de calendário escolar</strong> (aula/dia letivo).
 * O tipo determina se o aluno estava presente, faltou ou tem justificativa.
 *
 * @see TipoFrequencia
 */
@Entity
@Table(name = "aluno_frequencia")
@Data
@EqualsAndHashCode(callSuper = true)
public class AlunoFrequencia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_frequencia")
    private Long idAlunoFrequencia;

    /** Aluno (Pessoa com tipo ALUNO) ao qual a frequência pertence. */
    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false, referencedColumnName = "id_pessoa",
            foreignKey = @ForeignKey(name = "fk_id_aluno"))
    private Pessoa pessoaAluno;

    /** Calendário escolar (aula/dia) ao qual esta frequência se refere. */
    @ManyToOne
    @JoinColumn(name = "id_calendario_escolar", nullable = false, referencedColumnName = "id_calendario_escolar",
            foreignKey = @ForeignKey(name = "fk_id_calendario_escolar"))
    private CalendarioEscolar calendarioEscolar;

    /**
     * Tipo da frequência: PRESENTE, FALTA ou FALTA_JUSTIFICADA.
     * Armazenado como String (EnumType.STRING) para legibilidade no banco.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_frequencia", nullable = false, length = 30)
    private TipoFrequencia tipoFrequencia = TipoFrequencia.PRESENTE;
}
