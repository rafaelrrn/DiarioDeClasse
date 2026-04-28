package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.GradeCurricular;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class GradeCurricularDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idGradeCurricular;
    private Long idClasse;
    private Integer diaSemana;
    private Integer numeroAula;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioInicio;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horarioFim;

    private Boolean ativo;

    public GradeCurricularDTO() {}

    public GradeCurricularDTO(GradeCurricular entity) {
        if (entity != null) {
            this.idGradeCurricular = entity.getIdGradeCurricular();
            this.idClasse = entity.getClasse() != null ? entity.getClasse().getIdClasse() : null;
            this.diaSemana = entity.getDiaSemana();
            this.numeroAula = entity.getNumeroAula();
            this.horarioInicio = entity.getHorarioInicio();
            this.horarioFim = entity.getHorarioFim();
            this.ativo = entity.getAtivo();
        }
    }
}
