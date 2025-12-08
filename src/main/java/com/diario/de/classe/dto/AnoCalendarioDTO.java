package com.diario.de.classe.dto;

import com.diario.de.classe.model.AnoCalendario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class AnoCalendarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAnoCalendario;

    @NotNull
    @Size(max = 4)
    private String ano;

    private Date createdAt;
    private Date updatedAt;

    public AnoCalendarioDTO() {}

    public AnoCalendarioDTO(Long idAnoCalendario, String ano, Date createdAt, Date updatedAt) {
        this.idAnoCalendario = idAnoCalendario;
        this.ano = ano;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public AnoCalendarioDTO(AnoCalendario entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 