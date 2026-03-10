package com.diario.de.classe.modules.calendario.dto;

import com.diario.de.classe.modules.calendario.CalendarioEscolar;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

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

    public CalendarioEscolarDTO() {}

    public CalendarioEscolarDTO(CalendarioEscolar entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idMes = entity.getMes() != null ? entity.getMes().getIdMes() : null;
            this.idAnoCalendario = entity.getAnoCalendario() != null ? entity.getAnoCalendario().getIdAnoCalendario() : null;
            this.idPeriodo = entity.getPeriodo() != null ? entity.getPeriodo().getIdPeriodo() : null;
            this.idClasse = entity.getClasse() != null ? entity.getClasse().getIdClasse() : null;
        }
    }
}
