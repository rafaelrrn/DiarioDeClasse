package com.diario.de.classe.dto;

import com.diario.de.classe.model.Serie;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class SerieDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idSerie;

    @NotNull
    @Size(max = 255)
    private String nome;

    public SerieDTO() {}

    public SerieDTO(Serie entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 