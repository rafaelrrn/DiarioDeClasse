-- =============================================================================
-- V2 — Tabelas do módulo Instituição
-- Cria as tabelas que descrevem a estrutura pedagógica da instituição:
-- tipo de ensino, grau, série, curso, turno e a própria instituição.
-- =============================================================================

-- Nível de ensino: Ensino Fundamental, Ensino Médio, etc.
CREATE TABLE IF NOT EXISTS ensino (
    id_ensino   BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_ensino)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Grau de ensino: 1º grau, 2º grau, etc.
CREATE TABLE IF NOT EXISTS grau (
    id_grau     BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_grau)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Série/ano escolar: 1ª série, 2º ano, etc.
CREATE TABLE IF NOT EXISTS serie (
    id_serie    BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_serie)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Turno de funcionamento das turmas: Manhã, Tarde, Noite.
CREATE TABLE IF NOT EXISTS turno (
    id_turno    BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_turno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Representa uma instituição de ensino (escola, colégio, etc.).
CREATE TABLE IF NOT EXISTS instituicao_ensino (
    id_instituicao_ensino   BIGINT      NOT NULL AUTO_INCREMENT,
    descricao               VARCHAR(255),
    codigo_estadual         VARCHAR(255),
    created_at              DATETIME    NOT NULL DEFAULT NOW(),
    updated_at              DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME    NULL,
    ativo                   BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_instituicao_ensino)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Curso oferecido pela instituição: combinação de Ensino + Grau + Série.
-- Ex: Ensino Fundamental + 1º Grau + 5ª Série.
CREATE TABLE IF NOT EXISTS curso (
    id_curso    BIGINT      NOT NULL AUTO_INCREMENT,
    id_ensino   BIGINT      NOT NULL,
    id_grau     BIGINT      NOT NULL,
    id_serie    BIGINT      NOT NULL,
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_curso),
    CONSTRAINT fk_id_ensino FOREIGN KEY (id_ensino) REFERENCES ensino (id_ensino),
    CONSTRAINT fk_id_grau   FOREIGN KEY (id_grau)   REFERENCES grau   (id_grau),
    CONSTRAINT fk_id_serie  FOREIGN KEY (id_serie)  REFERENCES serie  (id_serie)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
