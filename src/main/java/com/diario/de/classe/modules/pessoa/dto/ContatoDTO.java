package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.Contato;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

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

    public ContatoDTO() {}

    public ContatoDTO(Contato entity) {
        if (entity != null) BeanUtils.copyProperties(entity, this);
    }
}
