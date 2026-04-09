package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.AlunoPerfil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AlunoPerfilDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAlunoPerfil;

    @NotNull
    private Long idPessoa;

    @NotNull
    @Size(max = 30)
    private String matricula;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataMatricula;

    private Boolean necessidadeEspecial = false;

    private String descricaoNee;

    public AlunoPerfilDTO() {}

    public AlunoPerfilDTO(AlunoPerfil entity) {
        if (entity != null) {
            this.idAlunoPerfil      = entity.getIdAlunoPerfil();
            this.matricula          = entity.getMatricula();
            this.dataMatricula      = entity.getDataMatricula();
            this.necessidadeEspecial = entity.getNecessidadeEspecial();
            this.descricaoNee       = entity.getDescricaoNee();
            this.idPessoa           = entity.getPessoa() != null
                    ? entity.getPessoa().getIdPessoa()
                    : null;
        }
    }
}
