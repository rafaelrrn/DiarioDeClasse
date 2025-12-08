package com.diario.de.classe.dto;

import com.diario.de.classe.model.Pessoa;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class PessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPessoa;

    @NotNull
    private Long idTipoPessoa;

    @NotNull
    @Size(max = 255)
    private String nome;

    @Size(max = 255)
    private String sexo;

    @Size(max = 255)
    private String dataNascimento;

    @Size(max = 255)
    private String situacao;

    @Size(max = 255)
    private String obs;

    public PessoaDTO() {}

    public PessoaDTO(Pessoa entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            // Mapear o ID da entidade relacionada
            this.idTipoPessoa = entity.getTipoPessoa() != null ? entity.getTipoPessoa().getIdTipoPessoa() : null;
        }
    }
} 