package com.diario.de.classe.dto;

import com.diario.de.classe.model.Curso;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class CursoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idCurso;

    @NotNull
    private Long idEnsino;

    @NotNull
    private Long idGrau;

    @NotNull
    private Long idSerie;

    public CursoDTO() {}

    public CursoDTO(Curso entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear os IDs das entidades relacionadas
            this.idEnsino = entity.getEnsino() != null ? entity.getEnsino().getIdEnsino() : null;
            this.idGrau = entity.getGrau() != null ? entity.getGrau().getIdGrau() : null;
            this.idSerie = entity.getSerie() != null ? entity.getSerie().getIdSerie() : null;
        }
    }
} 