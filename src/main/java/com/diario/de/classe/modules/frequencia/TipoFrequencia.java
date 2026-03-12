package com.diario.de.classe.modules.frequencia;

/**
 * Tipos de frequência de um aluno em uma aula, conforme a LDB (Lei de Diretrizes e Bases).
 *
 * <p>Regras de negócio:
 * <ul>
 *   <li>{@link #PRESENTE} — aluno compareceu. Conta como presença no cálculo do percentual.</li>
 *   <li>{@link #FALTA} — ausência sem justificativa. Conta como falta para efeito de reprovação.</li>
 *   <li>{@link #FALTA_JUSTIFICADA} — ausência com justificativa aceita pela instituição.
 *       Não conta para reprovação, mas o total de aulas permanece o mesmo
 *       (ou seja, reduz o denominador do cálculo). Veja {@link #contaParaReprovacao()}.</li>
 * </ul>
 *
 * <p>Fórmula de frequência (LDB Art. 24):
 * {@code percentual = (totalPresencas / totalAulas) * 100}
 * Frequência mínima obrigatória: 75%.
 */
public enum TipoFrequencia {

    /** Aluno presente na aula. */
    PRESENTE,

    /** Falta sem justificativa — conta para reprovação por frequência. */
    FALTA,

    /**
     * Falta com justificativa aceita — não causa reprovação por frequência,
     * mas ainda diminui o denominador do cálculo (a aula ocorreu para todos).
     */
    FALTA_JUSTIFICADA;

    /**
     * Indica se este tipo de ocorrência conta para reprovação por frequência.
     *
     * @return {@code true} somente para {@link #FALTA}
     */
    public boolean contaParaReprovacao() {
        return this == FALTA;
    }
}
