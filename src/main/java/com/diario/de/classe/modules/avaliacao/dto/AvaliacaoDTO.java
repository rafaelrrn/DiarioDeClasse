package com.diario.de.classe.modules.avaliacao.dto;

import com.diario.de.classe.modules.avaliacao.Avaliacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

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

    /** Peso da avaliação para cálculo de média ponderada (ex: prova=7, trabalho=3). */
    private Integer peso;

    public AvaliacaoDTO() {}

    public AvaliacaoDTO(Avaliacao entity) {
        if (entity != null) {
            this.idAvaliacao = entity.getIdAvaliacao();
            this.idDisciplina = entity.getDisciplina() != null ? entity.getDisciplina().getIdDisciplina() : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
            this.materia = entity.getMateria();
            this.dia = entity.getDia();
            this.peso = entity.getPeso();
        }
    }
}
