-- =============================================================================
-- V7 — Tabela de autenticação (Users)
-- Separada do módulo Pessoa intencionalmente: isola a autenticação do domínio
-- pedagógico. Um User pode existir sem um Pessoa vinculado (ex: admin técnico).
--
-- O campo 'role' armazena o enum Role como STRING:
--   ADMINISTRADOR, DIRETOR, COORDENADOR, PROFESSOR, RESPONSAVEL, ALUNO
-- =============================================================================

CREATE TABLE IF NOT EXISTS users (
    id_users    BIGINT          NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    email       VARCHAR(255),
    senha       VARCHAR(255),
    role        VARCHAR(50),    -- enum Role armazenado como String
    created_at  DATETIME        NOT NULL DEFAULT NOW(),
    updated_at  DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME        NULL,
    ativo       BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_users)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
