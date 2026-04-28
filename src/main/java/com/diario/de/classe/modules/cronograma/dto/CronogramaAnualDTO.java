package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.CronogramaAnual;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CronogramaAnualDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idCronogramaAnual;
    private Integer ano;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private Integer diasLetivosPrevistos;
    private Integer cargaHorariaPrevista;
    private String status;
    private Boolean ativo;

    public CronogramaAnualDTO() {}

    public CronogramaAnualDTO(CronogramaAnual entity) {
        if (entity != null) {
            this.idCronogramaAnual = entity.getIdCronogramaAnual();
            this.ano = entity.getAno();
            this.dataInicio = entity.getDataInicio();
            this.dataFim = entity.getDataFim();
            this.diasLetivosPrevistos = entity.getDiasLetivosPrevistos();
            this.cargaHorariaPrevista = entity.getCargaHorariaPrevista();
            this.status = entity.getStatus();
            this.ativo = entity.getAtivo();
        }
    }
}
