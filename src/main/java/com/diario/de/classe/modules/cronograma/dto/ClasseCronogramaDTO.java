package com.diario.de.classe.modules.cronograma.dto;

import com.diario.de.classe.modules.cronograma.ClasseCronograma;
import lombok.Data;

import java.io.Serializable;

@Data
public class ClasseCronogramaDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idClasseCronograma;
    private Long idClasse;
    private Long idCronogramaAnual;
    private Boolean ativo;

    public ClasseCronogramaDTO() {}

    public ClasseCronogramaDTO(ClasseCronograma entity) {
        if (entity != null) {
            this.idClasseCronograma = entity.getIdClasseCronograma();
            this.idClasse = entity.getClasse() != null ? entity.getClasse().getIdClasse() : null;
            this.idCronogramaAnual = entity.getCronogramaAnual() != null
                    ? entity.getCronogramaAnual().getIdCronogramaAnual() : null;
            this.ativo = entity.getAtivo();
        }
    }
}
