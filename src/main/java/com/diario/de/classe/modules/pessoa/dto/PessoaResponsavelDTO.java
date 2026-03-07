package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.PessoaResponsavel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

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

    public PessoaResponsavelDTO() {}

    public PessoaResponsavelDTO(PessoaResponsavel entity) {
        if (entity != null) {
            BeanUtils.copyProperties(entity, this);
            this.idAluno = entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idResponsavel = entity.getPessoaResponsavel() != null ? entity.getPessoaResponsavel().getIdPessoa() : null;
        }
    }
}
