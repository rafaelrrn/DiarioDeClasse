package com.diario.de.classe.modules.avaliacao.dto;

import com.diario.de.classe.modules.avaliacao.Avaliacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AvaliacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAvaliacao;

    @NotNull
    private Long idDisciplina;

    private Long idCalendarioEscolar;

    private Long idPeriodoLetivo;

    /** Valores aceitos: PROVA, TRABALHO, RECUPERACAO, SIMULADO, OUTRO. Padrão: PROVA. */
    private String tipo = "PROVA";

    @Size(max = 255)
    private String materia;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dia;

    private Integer peso;

    public AvaliacaoDTO() {}

    public AvaliacaoDTO(Avaliacao entity) {
        if (entity != null) {
            this.idAvaliacao         = entity.getIdAvaliacao();
            this.idDisciplina        = entity.getDisciplina()     != null ? entity.getDisciplina().getIdDisciplina()                 : null;
            this.idCalendarioEscolar = entity.getCalendarioEscolar() != null ? entity.getCalendarioEscolar().getIdCalendarioEscolar() : null;
            this.idPeriodoLetivo     = entity.getPeriodoLetivo()   != null ? entity.getPeriodoLetivo().getIdPeriodoLetivo()           : null;
            this.tipo                = entity.getTipo();
            this.materia             = entity.getMateria();
            this.dia                 = entity.getDia();
            this.peso                = entity.getPeso();
        }
    }
}
