package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.EventoCalendario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EventoCalendarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEventoCalendario;
    private Long idCronogramaAnual;
    private String titulo;
    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    private String tipoEvento;
    private Boolean ehLetivo;
    private String cor;
    private Boolean ativo;

    public EventoCalendarioDTO() {}

    public EventoCalendarioDTO(EventoCalendario entity) {
        if (entity != null) {
            this.idEventoCalendario = entity.getIdEventoCalendario();
            this.idCronogramaAnual = entity.getCronogramaAnual() != null
                    ? entity.getCronogramaAnual().getIdCronogramaAnual() : null;
            this.titulo = entity.getTitulo();
            this.descricao = entity.getDescricao();
            this.dataInicio = entity.getDataInicio();
            this.dataFim = entity.getDataFim();
            this.tipoEvento = entity.getTipoEvento();
            this.ehLetivo = entity.getEhLetivo();
            this.cor = entity.getCor();
            this.ativo = entity.getAtivo();
        }
    }
}
