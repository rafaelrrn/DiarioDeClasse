package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.TipoPessoa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class TipoPessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idTipoPessoa;

    @NotNull
    @Size(max = 255)
    private String nome;

    public TipoPessoaDTO() {}

    public TipoPessoaDTO(TipoPessoa entity) {
        if (entity != null) BeanUtils.copyProperties(entity, this);
    }
}
