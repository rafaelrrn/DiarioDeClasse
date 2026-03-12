-- =============================================================================
-- V8 — Ajuste na tabela aluno_frequencia: substituir 'faltas' por 'tipo_frequencia'
--
-- Motivo: a coluna 'faltas' (VARCHAR) não refletia o modelo correto.
-- Cada registro deve representar a ocorrência de UM aluno em UMA aula,
-- com o tipo: PRESENTE, FALTA ou FALTA_JUSTIFICADA (LDB Art. 24).
-- =============================================================================

-- Adiciona a nova coluna com valor padrão PRESENTE para registros existentes.
ALTER TABLE aluno_frequencia
    ADD COLUMN tipo_frequencia VARCHAR(30) NOT NULL DEFAULT 'PRESENTE';

-- Remove a coluna legada que não mais representa o modelo.
ALTER TABLE aluno_frequencia
    DROP COLUMN faltas;
