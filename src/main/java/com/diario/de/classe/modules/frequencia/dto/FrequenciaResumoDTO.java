package com.diario.de.classe.modules.frequencia.dto;

/**
 * DTO de resumo de frequência de um aluno.
 *
 * <p>Expõe as contagens e o percentual de presença calculado pelo serviço,
 * além do indicador de risco de reprovação por frequência (LDB Art. 24 — mínimo 75%).
 *
 * @param idAluno            ID do aluno consultado
 * @param totalAulas         Total de aulas registradas para o aluno (denominador da fórmula)
 * @param totalPresencas     Quantidade de ocorrências com tipo {@code PRESENTE}
 * @param totalFaltas        Quantidade de ocorrências com tipo {@code FALTA}
 * @param totalFaltasJust    Quantidade de ocorrências com tipo {@code FALTA_JUSTIFICADA}
 * @param percentualPresenca Percentual calculado: {@code (totalPresencas / totalAulas) * 100}.
 *                           Retorna {@code 0.0} quando não há aulas registradas.
 * @param emRiscoReprovacao  {@code true} quando {@code percentualPresenca < 75.0} (LDB)
 */
public record FrequenciaResumoDTO(
        Long idAluno,
        long totalAulas,
        long totalPresencas,
        long totalFaltas,
        long totalFaltasJust,
        double percentualPresenca,
        boolean emRiscoReprovacao
) {
    /** Frequência mínima legal exigida pela LDB (Lei nº 9.394/96, Art. 24). */
    public static final double FREQUENCIA_MINIMA_LDB = 75.0;
}
