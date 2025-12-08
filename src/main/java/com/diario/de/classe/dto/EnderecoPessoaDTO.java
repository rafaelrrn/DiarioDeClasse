package com.diario.de.classe.dto;

import com.diario.de.classe.model.EnderecoPessoa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

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

    private Date createdAt;
    private Date updatedAt;

    public EnderecoPessoaDTO() {}

    public EnderecoPessoaDTO(EnderecoPessoa entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear os IDs das entidades relacionadas
            this.idPessoa = entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null;
            this.idEndereco = entity.getEndereco() != null ? entity.getEndereco().getIdEndereco() : null;
        }
    }
} 