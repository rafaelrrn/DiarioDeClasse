package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.PeriodoLetivo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PeriodoLetivoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPeriodoLetivo;
    private Long idCronogramaAnual;
    private String nome;
    private String tipo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private Integer ordem;
    private Boolean ativo;

    public PeriodoLetivoDTO() {}

    public PeriodoLetivoDTO(PeriodoLetivo entity) {
        if (entity != null) {
            this.idPeriodoLetivo = entity.getIdPeriodoLetivo();
            this.idCronogramaAnual = entity.getCronogramaAnual() != null
                    ? entity.getCronogramaAnual().getIdCronogramaAnual() : null;
            this.nome = entity.getNome();
            this.tipo = entity.getTipo();
            this.dataInicio = entity.getDataInicio();
            this.dataFim = entity.getDataFim();
            this.ordem = entity.getOrdem();
            this.ativo = entity.getAtivo();
        }
    }
}
