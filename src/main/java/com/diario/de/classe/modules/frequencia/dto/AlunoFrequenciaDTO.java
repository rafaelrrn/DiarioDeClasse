package com.diario.de.classe.modules.frequencia.dto;

import com.diario.de.classe.modules.frequencia.AlunoFrequencia;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlunoFrequenciaDTO {

    private Long idAlunoFrequencia;

    @NotNull(message = "idAluno é obrigatório")
    private Long idAluno;

    @NotNull(message = "idAula é obrigatório")
    private Long idAula;

    /** Valores aceitos: PRESENTE, FALTA, FALTA_JUSTIFICADA. Padrão: PRESENTE. */
    private String tipoFrequencia;

    private String justificativa;

    public AlunoFrequenciaDTO() {}

    public AlunoFrequenciaDTO(AlunoFrequencia entity) {
        if (entity != null) {
            this.idAlunoFrequencia = entity.getIdAlunoFrequencia();
            this.idAluno           = entity.getAluno() != null ? entity.getAluno().getIdPessoa() : null;
            this.idAula            = entity.getAula()  != null ? entity.getAula().getIdAula()    : null;
            this.tipoFrequencia    = entity.getTipoFrequencia();
            this.justificativa     = entity.getJustificativa();
        }
    }
}
