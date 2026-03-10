package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.EnderecoPessoa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class EnderecoPessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idEnderecoPessoa;

    @NotNull
    private Long idPessoa;

    @NotNull
    private Long idEndereco;

    @Size(max = 255)
    private String nome;

    public EnderecoPessoaDTO() {}

    public EnderecoPessoaDTO(EnderecoPessoa entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idPessoa = entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null;
            this.idEndereco = entity.getEndereco() != null ? entity.getEndereco().getIdEndereco() : null;
        }
    }
}
