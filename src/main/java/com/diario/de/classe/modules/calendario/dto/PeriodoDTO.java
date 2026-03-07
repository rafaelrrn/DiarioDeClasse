package com.diario.de.classe.modules.calendario.dto;

import com.diario.de.classe.modules.calendario.Periodo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class PeriodoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPeriodo;

    @NotNull
    @Size(max = 255)
    private String nome;

    public PeriodoDTO() {}

    public PeriodoDTO(Periodo entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
}
