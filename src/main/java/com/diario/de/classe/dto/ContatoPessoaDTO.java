package com.diario.de.classe.dto;

import com.diario.de.classe.model.ContatoPessoa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class ContatoPessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idContatoPessoa;

    @NotNull
    private Long idPessoa;

    @NotNull
    private Long idContato;

    @Size(max = 255)
    private String nome;

    private Date createdAt;
    private Date updatedAt;

    public ContatoPessoaDTO() {}

    public ContatoPessoaDTO(ContatoPessoa entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear os IDs das entidades relacionadas
            this.idPessoa = entity.getPessoa() != null ? entity.getPessoa().getIdPessoa() : null;
            this.idContato = entity.getContato() != null ? entity.getContato().getIdContato() : null;
        }
    }
} 