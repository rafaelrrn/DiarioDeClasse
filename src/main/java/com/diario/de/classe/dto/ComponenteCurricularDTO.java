package com.diario.de.classe.dto;

import com.diario.de.classe.model.ComponenteCurricular;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class ComponenteCurricularDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idComponenteCurricular;

    @NotNull
    @Size(max = 255)
    private String nome;

    private String obs;

    public ComponenteCurricularDTO() {}

    public ComponenteCurricularDTO(ComponenteCurricular entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 