package com.diario.de.classe.modules.avaliacao.dto;

import com.diario.de.classe.modules.avaliacao.AlunoAvaliacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class AlunoAvaliacaoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAlunoAvaliacao;

    @NotNull
    private Long idAluno;

    @NotNull
    private Long idAvaliacao;

    private Float nota;

    @Size(max = 255)
    private String obs;

    public AlunoAvaliacaoDTO() {}

    public AlunoAvaliacaoDTO(AlunoAvaliacao entity) {
        if (entity != null) {
            this.idAlunoAvaliacao = entity.getIdAlunoAvaliacao();
            this.idAluno = entity.getPessoaAluno() != null ? entity.getPessoaAluno().getIdPessoa() : null;
            this.idAvaliacao = entity.getAvaliacao() != null ? entity.getAvaliacao().getIdAvaliacao() : null;
            this.nota = entity.getNota();
            this.obs = entity.getObs();
        }
    }
}
