package com.diario.de.classe.dto;

import com.diario.de.classe.model.PessoaResponsavel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class PessoaResponsavelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPessoaResponsavel;

    @NotNull
    private Long idAluno;

    @NotNull
    private Long idResponsavel;

    @Size(max = 255)
    private String parentesco;

    private Date createdAt;
    private Date updatedAt;

    public PessoaResponsavelDTO() {
    }

    public PessoaResponsavelDTO(PessoaResponsavel pessoaResponsavel) {
        if (pessoaResponsavel != null) {
            BeanUtils.copyProperties(pessoaResponsavel, this);
            this.idAluno = pessoaResponsavel.getPessoaAluno() != null ? 
                pessoaResponsavel.getPessoaAluno().getIdPessoa() : null;
            this.idResponsavel = pessoaResponsavel.getPessoaResponsavel() != null ? 
                pessoaResponsavel.getPessoaResponsavel().getIdPessoa() : null;
        }
    }

} 