-- =============================================================================
-- V13 — Adiciona campo peso em avaliacao
--
-- O campo peso permite cálculo de média ponderada entre avaliações.
-- Quando nulo, assume peso 1 (todas as avaliações com mesmo valor).
-- Identificado na Etapa B: campo existia na entidade sem migration correspondente.
-- =============================================================================

ALTER TABLE avaliacao
    ADD COLUMN peso INT NULL;
