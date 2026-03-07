package com.diario.de.classe.modules.instituicao.dto;

import com.diario.de.classe.modules.instituicao.Ensino;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.io.Serializable;

@Data
public class EnsinoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idEnsino;
    @NotNull @Size(max = 255) private String nome;

    public EnsinoDTO() {}
    public EnsinoDTO(Ensino entity) {
        if (entity != null) BeanUtils.copyProperties(entity, this);
    }
}
