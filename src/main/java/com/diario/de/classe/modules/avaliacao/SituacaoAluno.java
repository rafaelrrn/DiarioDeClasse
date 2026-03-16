package com.diario.de.classe.modules.avaliacao;

/**
 * Situação final do aluno em uma disciplina, calculada com base na
 * média ponderada e no percentual de frequência.
 *
 * <p>Regras (INSTRUCTIONS.md §7.2):
 * <ul>
 *   <li>{@link #APROVADO}: média ≥ 5.0 <strong>e</strong> frequência ≥ 75%</li>
 *   <li>{@link #EM_RECUPERACAO}: média entre 3.0 e 4.9 (vai para recuperação)</li>
 *   <li>{@link #REPROVADO_NOTA}: média < 3.0 (reprovação direta, sem recuperação)</li>
 *   <li>{@link #REPROVADO_FREQUENCIA}: frequência < 75%, independente da nota</li>
 * </ul>
 */
public enum SituacaoAluno {

    /** Média ≥ 5.0 e frequência ≥ 75% — aluno aprovado. */
    APROVADO,

    /** Média entre 3.0 e 4.9 — aluno vai para recuperação. */
    EM_RECUPERACAO,

    /** Média < 3.0 — reprovado diretamente, sem direito a recuperação. */
    REPROVADO_NOTA,

    /** Frequência < 75% (LDB Art. 24) — reprovado por falta, independente da nota. */
    REPROVADO_FREQUENCIA
}
