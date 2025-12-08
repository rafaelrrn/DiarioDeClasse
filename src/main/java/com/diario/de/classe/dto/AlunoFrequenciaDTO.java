package com.diario.de.classe.dto;

import com.diario.de.classe.model.AlunoFrequencia;
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

    public AlunoFrequenciaDTO(Long idAlunoFrequencia, Long idAluno, Long idCalendarioEscolar, String faltas, Date createdAt, Date updatedAt) {
        this.idAlunoFrequencia = idAlunoFrequencia;
        this.idAluno = idAluno;
        this.idCalendarioEscolar = idCalendarioEscolar;
        this.faltas = faltas;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AlunoFrequenciaDTO(AlunoFrequencia entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idAluno = entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
        }
    }
} 