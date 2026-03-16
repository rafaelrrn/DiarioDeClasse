package com.diario.de.classe.modules.avaliacao.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para lançamento de nota de um aluno em uma avaliação.
 *
 * <p>Usado no endpoint {@code POST /v1/avaliacoes/{id}/notas} para lançamento em lote.
 * O {@code idAvaliacao} é fornecido pelo path variable do endpoint.
 *
 * @param idAluno ID do aluno que receberá a nota
 * @param nota    Nota atribuída, entre 0.0 e 10.0
 * @param obs     Observação opcional (ex: "aluno com recuperação", "prova anulada")
 */
public record NotaLancamentoDTO(

        @NotNull(message = "O ID do aluno é obrigatório")
        Long idAluno,

        @NotNull(message = "A nota é obrigatória")
        @DecimalMin(value = "0.0", message = "A nota deve ser no mínimo 0.0")
        @DecimalMax(value = "10.0", message = "A nota deve ser no máximo 10.0")
        Float nota,

        String obs
) {}
