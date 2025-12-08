package com.diario.de.classe.dto;

import com.diario.de.classe.model.Avaliacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AvaliacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAvaliacao;

    @NotNull
    private Long idDisciplina;

    private Long idCalendarioEscolar;

    @Size(max = 255)
    private String materia;

    @Size(max = 255)
    private String dia;

    private Date createdAt;
    private Date updatedAt;

    public AvaliacaoDTO() {}

    public AvaliacaoDTO(Long idAvaliacao, Long idDisciplina, Long idCalendarioEscolar, String materia, String dia, Date createdAt, Date updatedAt) {
        this.idAvaliacao = idAvaliacao;
        this.idDisciplina = idDisciplina;
        this.idCalendarioEscolar = idCalendarioEscolar;
        this.materia = materia;
        this.dia = dia;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AvaliacaoDTO(Avaliacao entity) {
        if (entity != null) {
            this.idAvaliacao = entity.getIdAvaliacao();
            this.idDisciplina = entity.getDisciplina() != null ? entity.getDisciplina().getIdDisciplina() : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
            this.materia = entity.getMateria();
            this.dia = entity.getDia();
            this.createdAt = entity.getCreatedAt();
            this.updatedAt = entity.getUpdatedAt();
        }
    }
} 