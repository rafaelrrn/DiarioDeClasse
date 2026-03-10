package com.diario.de.classe.modules.turma.dto;

import com.diario.de.classe.modules.turma.Classe;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class ClasseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idClasse;

    @NotNull
    private Long idInstituicaoEnsino;

    private Long idComponenteCurricular;

    @NotNull
    private Long idCurso;

    @NotNull
    private Long idTurno;

    @NotNull
    private Long idTurma;

    @NotNull
    private Long idProfessor;

    public ClasseDTO() {}

    public ClasseDTO(Classe entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idInstituicaoEnsino = entity.getInstituicaoEnsino() != null ? entity.getInstituicaoEnsino().getIdInstituicaoEnsino() : null;
            this.idComponenteCurricular = entity.getComponenteCurricular() != null ? entity.getComponenteCurricular().getIdComponenteCurricular() : null;
            this.idCurso = entity.getCurso() != null ? entity.getCurso().getIdCurso() : null;
            this.idTurno = entity.getTurno() != null ? entity.getTurno().getIdTurno() : null;
            this.idTurma = entity.getTurma() != null ? entity.getTurma().getIdTurma() : null;
            this.idProfessor = entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null;
        }
    }
}
