package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.Aula;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AulaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idAula;
    private Long idClasse;
    private Long idPeriodoLetivo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAula;

    private Integer numeroAula;
    private String conteudoMinistrado;
    private Boolean chamadaEncerrada;
    private Long idRegistradoPor;
    private String nomeRegistradoPor;
    private Boolean ativo;

    public AulaDTO() {}

    public AulaDTO(Aula entity) {
        if (entity != null) {
            this.idAula = entity.getIdAula();
            this.idClasse = entity.getClasse() != null ? entity.getClasse().getIdClasse() : null;
            this.idPeriodoLetivo = entity.getPeriodoLetivo() != null
                    ? entity.getPeriodoLetivo().getIdPeriodoLetivo() : null;
            this.dataAula = entity.getDataAula();
            this.numeroAula = entity.getNumeroAula();
            this.conteudoMinistrado = entity.getConteudoMinistrado();
            this.chamadaEncerrada = entity.getChamadaEncerrada();
            this.idRegistradoPor = entity.getRegistradoPor() != null
                    ? entity.getRegistradoPor().getIdPessoa() : null;
            this.nomeRegistradoPor = entity.getRegistradoPor() != null
                    ? entity.getRegistradoPor().getNome() : null;
            this.ativo = entity.getAtivo();
        }
    }
}
