package com.diario.de.classe.modules.avaliacao.dto;

import com.diario.de.classe.modules.frequencia.dto.FrequenciaResumoDTO;

import java.util.List;

/**
 * DTO do boletim completo de um aluno.
 *
 * <p>Consolida frequência e médias por disciplina em uma única resposta,
 * expondo a situação do aluno em cada componente curricular.
 *
 * @param idAluno     ID do aluno
 * @param nomeAluno   Nome completo do aluno
 * @param frequencia  Resumo de frequência com percentual e indicador de risco (LDB 75%)
 * @param disciplinas Lista de médias e situações por disciplina
 */
public record BoletimResponseDTO(
        Long idAluno,
        String nomeAluno,
        FrequenciaResumoDTO frequencia,
        List<MediaDisciplinaDTO> disciplinas
) {}
