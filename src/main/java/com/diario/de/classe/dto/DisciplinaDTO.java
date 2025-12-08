package com.diario.de.classe.dto;

import com.diario.de.classe.model.Disciplina;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class DisciplinaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idDisciplina;

    @NotNull
    @Size(max = 255)
    private String nome;

    private Date createdAt;
    private Date updatedAt;

    public DisciplinaDTO() {}

    public DisciplinaDTO(Disciplina entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 