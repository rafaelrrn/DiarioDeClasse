package com.diario.de.classe.modules.turma.dto;

import com.diario.de.classe.modules.turma.AlunoTurma;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class AlunoTurmaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAlunoTurma;

    @NotNull
    private Long idAluno;

    @NotNull
    private Long idTurma;

    @Size(max = 255)
    private String obs;

    private Date createdAt;
    private Date updatedAt;

    public AlunoTurmaDTO() {}

    public AlunoTurmaDTO(AlunoTurma entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idAluno = entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idTurma = entity.getTurma() != null ? entity.getTurma().getIdTurma() : null;
        }
    }
}
