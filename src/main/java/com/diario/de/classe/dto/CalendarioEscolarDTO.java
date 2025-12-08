package com.diario.de.classe.dto;

import com.diario.de.classe.model.CalendarioEscolar;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class CalendarioEscolarDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idCalendarioEscolar;

    @NotNull
    private Long idMes;

    private Long idAnoCalendario;

    @NotNull
    private Long idPeriodo;

    @NotNull
    private Long idClasse;

    @Size(max = 255)
    private String diasLetivos;

    @Size(max = 255)
    private String diasAvaliacoes;

    private Date createdAt;
    private Date updatedAt;

    public CalendarioEscolarDTO() {}

    public CalendarioEscolarDTO(Long idCalendarioEscolar, Long idMes, Long idAnoCalendario, Long idPeriodo, Long idClasse, String diasLetivos, String diasAvaliacoes, Date createdAt, Date updatedAt) {
        this.idCalendarioEscolar = idCalendarioEscolar;
        this.idMes = idMes;
        this.idAnoCalendario = idAnoCalendario;
        this.idPeriodo = idPeriodo;
        this.idClasse = idClasse;
        this.diasLetivos = diasLetivos;
        this.diasAvaliacoes = diasAvaliacoes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CalendarioEscolarDTO(CalendarioEscolar entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear os IDs das entidades relacionadas
            this.idMes = entity.getMes() != null ? entity.getMes().getIdMes() : null;
            this.idAnoCalendario = entity.getAnoCalendario() != null ? entity.getAnoCalendario().getIdAnoCalendario() : null;
            this.idPeriodo = entity.getPeriodo() != null ? entity.getPeriodo().getIdPeriodo() : null;
            this.idClasse = entity.getClasse() != null ? entity.getClasse().getIdClasse() : null;
        }
    }
} 