package com.diario.de.classe.modules.avaliacao.dto;

import com.diario.de.classe.modules.avaliacao.SituacaoAluno;

/**
 * DTO com o resultado da média ponderada de um aluno em uma disciplina.
 *
 * <p>Compõe o boletim do aluno ({@link BoletimResponseDTO}).
 *
 * <p>Fórmula da média ponderada:
 * {@code media = Σ(nota * peso) / Σ(peso)}
 *
 * @param idDisciplina   ID da disciplina
 * @param nomeDisciplina Nome da disciplina
 * @param mediaCalculada Média ponderada calculada (0.0 – 10.0). Retorna 0.0 se não há notas.
 * @param situacao       Situação do aluno nesta disciplina ({@link SituacaoAluno})
 */
public record MediaDisciplinaDTO(
        Long idDisciplina,
        String nomeDisciplina,
        double mediaCalculada,
        SituacaoAluno situacao
) {}
