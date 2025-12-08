package com.diario.de.classe.dto;

import com.diario.de.classe.model.Classe;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

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

    private Date createdAt;
    private Date updatedAt;

    public ClasseDTO() {}

    public ClasseDTO(Long idClasse, Long idInstituicaoEnsino, Long idComponenteCurricular, Long idCurso, Long idTurno, Long idTurma, Long idProfessor, Date createdAt, Date updatedAt) {
        this.idClasse = idClasse;
        this.idInstituicaoEnsino = idInstituicaoEnsino;
        this.idComponenteCurricular = idComponenteCurricular;
        this.idCurso = idCurso;
        this.idTurno = idTurno;
        this.idTurma = idTurma;
        this.idProfessor = idProfessor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ClasseDTO(Classe entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear os IDs das entidades relacionadas
            this.idInstituicaoEnsino = entity.getInstituicaoEnsino() != null ? entity.getInstituicaoEnsino().getIdInstituicaoEnsino() : null;
            this.idComponenteCurricular = entity.getComponenteCurricular() != null ? entity.getComponenteCurricular().getIdComponenteCurricular() : null;
            this.idCurso = entity.getCurso() != null ? entity.getCurso().getIdCurso() : null;
            this.idTurno = entity.getTurno() != null ? entity.getTurno().getIdTurno() : null;
            this.idTurma = entity.getTurma() != null ? entity.getTurma().getIdTurma() : null;
            this.idProfessor = entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null;
        }
    }
} 