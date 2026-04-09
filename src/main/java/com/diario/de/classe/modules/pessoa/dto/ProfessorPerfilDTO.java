package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.ProfessorPerfil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ProfessorPerfilDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idProfessorPerfil;

    @NotNull
    private Long idPessoa;

    @Size(max = 30)
    private String registroMec;

    @Size(max = 200)
    private String formacao;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAdmissao;

    public ProfessorPerfilDTO() {}

    public ProfessorPerfilDTO(ProfessorPerfil entity) {
        if (entity != null) {
            this.idProfessorPerfil = entity.getIdProfessorPerfil();
            this.registroMec       = entity.getRegistroMec();
            this.formacao          = entity.getFormacao();
            this.dataAdmissao      = entity.getDataAdmissao();
            this.idPessoa          = entity.getPessoa() != null
                    ? entity.getPessoa().getIdPessoa()
                    : null;
        }
    }
}
