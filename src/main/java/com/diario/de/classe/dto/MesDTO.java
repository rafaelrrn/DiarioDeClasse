package com.diario.de.classe.dto;

import com.diario.de.classe.model.Mes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class MesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idMes;

    @NotNull
    @Size(max = 255)
    private String nome;

    private Date createdAt;
    private Date updatedAt;

    public MesDTO() {}

    public MesDTO(Mes entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 