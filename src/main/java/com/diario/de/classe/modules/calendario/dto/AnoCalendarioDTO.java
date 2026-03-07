package com.diario.de.classe.modules.calendario.dto;

import com.diario.de.classe.modules.calendario.AnoCalendario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class AnoCalendarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAnoCalendario;

    @NotNull
    @Size(max = 4)
    private String ano;

    public AnoCalendarioDTO() {}

    public AnoCalendarioDTO(AnoCalendario entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
}
