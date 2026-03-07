package com.diario.de.classe.modules.frequencia.dto;

import com.diario.de.classe.modules.frequencia.AlunoFrequencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlunoFrequenciaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAlunoFrequencia;

    @NotNull
    private Long idAluno;

    @NotNull
    private Long idCalendarioEscolar;

    @Size(max = 255)
    private String faltas;

    private Date createdAt;
    private Date updatedAt;

    public AlunoFrequenciaDTO() {}

    public AlunoFrequenciaDTO(AlunoFrequencia entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idAluno = entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
        }
    }
}
