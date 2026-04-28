package com.diario.de.classe.modules.frequencia;

// DEPRECATED — substituído na Etapa B da refatoração
// O mapeamento anterior vinculava AlunoFrequencia a CalendarioEscolar (tabela legada).
// A nova versão vincula diretamente à entidade Aula, com granularidade por aula/aluno.
//
// Mapeamento anterior (removido):
//   @ManyToOne @JoinColumn(name = "id_calendario_escolar") private CalendarioEscolar calendarioEscolar;
//   @ManyToOne @JoinColumn(name = "id_aluno") private Pessoa pessoaAluno;
//   @Enumerated(EnumType.STRING) private TipoFrequencia tipoFrequencia;
// -------------------------------------------------------------------------

import com.diario.de.classe.modules.cronograma.Aula;
import com.diario.de.classe.modules.pessoa.Pessoa;
import com.diario.de.classe.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "aluno_frequencia")
@Data
@ToString(exclude = {"aula", "aluno"})
@EqualsAndHashCode(callSuper = true, exclude = {"aula", "aluno"})
public class AlunoFrequencia extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aluno_frequencia")
    private Long idAlunoFrequencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aula", nullable = false)
    private Aula aula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", nullable = false)
    private Pessoa aluno;

    @Column(name = "tipo_frequencia", nullable = false, length = 30)
    private String tipoFrequencia = "PRESENTE";

    @Column(name = "justificativa", length = 500)
    private String justificativa;
}
