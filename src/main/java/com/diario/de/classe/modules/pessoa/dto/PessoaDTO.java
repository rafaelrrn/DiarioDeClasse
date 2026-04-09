package com.diario.de.classe.modules.pessoa.dto;

import com.diario.de.classe.modules.pessoa.Pessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PessoaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idPessoa;

    @NotNull
    private Long idTipoPessoa;

    @NotNull
    @Size(max = 255)
    private String nome;

    @Size(max = 11)
    private String cpf;

    @Size(max = 2)
    private String sexo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @Size(max = 30)
    private String situacao;

    @Size(max = 500)
    private String fotoUrl;

    @Size(max = 255)
    private String obs;

    public PessoaDTO() {}

    public PessoaDTO(Pessoa entity) {
        if (entity != null) {
            this.idPessoa       = entity.getIdPessoa();
            this.nome           = entity.getNome();
            this.cpf            = entity.getCpf();
            this.sexo           = entity.getSexo();
            this.dataNascimento = entity.getDataNascimento();
            this.situacao       = entity.getSituacao();
            this.fotoUrl        = entity.getFotoUrl();
            this.obs            = entity.getObs();
            this.idTipoPessoa   = entity.getTipoPessoa() != null
                    ? entity.getTipoPessoa().getIdTipoPessoa()
                    : null;
        }
    }
}
