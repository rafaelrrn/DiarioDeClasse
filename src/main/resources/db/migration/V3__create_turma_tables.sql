-- =============================================================================
-- V3 — Tabelas do módulo Turma (parte 1: entidades independentes)
-- Turma, ComponenteCurricular e Disciplina não têm dependências externas.
-- Classe e AlunoTurma (que dependem de Pessoa e Instituição) ficam em V4.
-- =============================================================================

-- Turma: agrupamento de alunos (ex: 5A, 6B).
CREATE TABLE IF NOT EXISTS turma (
    id_turma    BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_turma)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Componente curricular: área de conhecimento (ex: Matemática, Língua Portuguesa).
CREATE TABLE IF NOT EXISTS componente_curricular (
    id_componente_curricular    BIGINT      NOT NULL AUTO_INCREMENT,
    nome                        VARCHAR(255),
    obs                         VARCHAR(255),
    created_at                  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at                  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at                  DATETIME    NULL,
    ativo                       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_componente_curricular)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Disciplina específica dentro de um componente curricular.
CREATE TABLE IF NOT EXISTS disciplina (
    id_disciplina   BIGINT      NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(255),
    created_at      DATETIME    NOT NULL DEFAULT NOW(),
    updated_at      DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME    NULL,
    ativo           BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_disciplina)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
