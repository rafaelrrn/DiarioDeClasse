-- =============================================================================
-- V5 — Tabelas do módulo Calendário
-- Depende de: V4 (classe).
--
-- O calendário escolar organiza as aulas por mês e período dentro de uma classe.
-- É a referência usada pelo lançamento de frequência e avaliações.
-- =============================================================================

-- Ano letivo de referência.
CREATE TABLE IF NOT EXISTS ano_calendario (
    id_ano_calendario   BIGINT      NOT NULL AUTO_INCREMENT,
    ano                 VARCHAR(255),
    created_at          DATETIME    NOT NULL DEFAULT NOW(),
    updated_at          DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME    NULL,
    ativo               BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_ano_calendario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Mês do calendário (Janeiro, Fevereiro, ..., Dezembro).
CREATE TABLE IF NOT EXISTS mes (
    id_mes      BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_mes)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Período letivo: 1º Bimestre, 2º Bimestre, etc.
CREATE TABLE IF NOT EXISTS periodo (
    id_periodo  BIGINT      NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(255),
    created_at  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at  DATETIME    NULL,
    ativo       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_periodo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Calendário escolar: vínculo entre uma Classe e um mês/período específico.
-- Registra os dias letivos e dias de avaliação daquele mês/período/classe.
CREATE TABLE IF NOT EXISTS calendario_escolar (
    id_calendario_escolar   BIGINT      NOT NULL AUTO_INCREMENT,
    id_mes                  BIGINT      NOT NULL,
    id_ano_calendario       BIGINT,             -- nullable: pode ser preenchido depois
    id_periodo              BIGINT      NOT NULL,
    id_classe               BIGINT      NOT NULL,
    dias_letivos            VARCHAR(255),
    dias_avaliacoes         VARCHAR(255),
    created_at              DATETIME    NOT NULL DEFAULT NOW(),
    updated_at              DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME    NULL,
    ativo                   BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_calendario_escolar),
    CONSTRAINT fk_calendario_mes           FOREIGN KEY (id_mes)           REFERENCES mes            (id_mes),
    CONSTRAINT fk_calendario_ano           FOREIGN KEY (id_ano_calendario) REFERENCES ano_calendario (id_ano_calendario),
    CONSTRAINT fk_calendario_periodo       FOREIGN KEY (id_periodo)        REFERENCES periodo        (id_periodo),
    CONSTRAINT fk_calendario_classe        FOREIGN KEY (id_classe)         REFERENCES classe         (id_classe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
