package com.diario.de.classe.modules.frequencia.dto;

import com.diario.de.classe.modules.frequencia.AlunoFrequencia;
import com.diario.de.classe.modules.frequencia.TipoFrequencia;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada e saída para o recurso {@code AlunoFrequencia}.
 *
 * <p>Utilizado tanto no corpo das requisições (POST/PUT) quanto
 * nas respostas da API, evitando expor a entidade JPA diretamente.
 */
@Data
public class AlunoFrequenciaDTO {

    /** ID do registro de frequência (nulo na criação, preenchido nas respostas). */
    private Long idAlunoFrequencia;

    /** ID da Pessoa com tipo ALUNO. Obrigatório na criação. */
    @NotNull(message = "idAluno é obrigatório")
    private Long idAluno;

    /** ID do CalendarioEscolar (aula/dia letivo). Obrigatório na criação. */
    @NotNull(message = "idCalendarioEscolar é obrigatório")
    private Long idCalendarioEscolar;

    /**
     * Tipo da frequência: PRESENTE, FALTA ou FALTA_JUSTIFICADA.
     * Quando não informado, o serviço assume {@link TipoFrequencia#PRESENTE}.
     */
    private TipoFrequencia tipoFrequencia;

    public AlunoFrequenciaDTO() {}

    /**
     * Constrói o DTO a partir da entidade JPA.
     *
     * @param entity entidade persistida
     */
    public AlunoFrequenciaDTO(AlunoFrequencia entity) {
        if (entity != null) {
            this.idAlunoFrequencia = entity.getIdAlunoFrequencia();
            this.tipoFrequencia = entity.getTipoFrequencia();
            this.idAluno = entity.getPessoaAluno() != null
                    ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null
                    ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
        }
    }
}
