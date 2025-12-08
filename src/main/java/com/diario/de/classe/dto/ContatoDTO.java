package com.diario.de.classe.dto;

import com.diario.de.classe.model.Contato;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContatoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idContato;

    @NotNull
    @Size(max = 255)
    private String tipoContato;

    @NotNull
    @Size(max = 255)
    private String contato;

    private Date createdAt;
    private Date updatedAt;

    public ContatoDTO() {}

    public ContatoDTO(Contato entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
        }
    }
} 