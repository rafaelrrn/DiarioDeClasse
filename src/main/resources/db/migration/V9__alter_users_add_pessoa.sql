-- =============================================================================
-- V9 — Vincula tabela users à tabela pessoa (opcional)
--
-- Um User pode existir sem Pessoa vinculada (ex: administrador técnico).
-- Quando preenchido, permite que o sistema identifique qual Pessoa do domínio
-- pedagógico corresponde ao usuário autenticado (professor, aluno, responsável).
-- =============================================================================

ALTER TABLE users
    ADD COLUMN id_pessoa BIGINT NULL,
    ADD CONSTRAINT fk_users_pessoa
        FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa);
