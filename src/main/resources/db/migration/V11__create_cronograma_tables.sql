-- =============================================================================
-- V11 — Cria a nova estrutura de Calendário Escolar (refatoração)
--
-- Substitui futuramente: calendario_escolar, mes, periodo, ano_calendario
--
-- Nova hierarquia:
--   cronograma_anual
--       └── periodo_letivo  (bimestres, trimestres, semestres)
--       └── evento_calendario (feriados, recessos, eventos)
--   classe_cronograma       (ponte entre classe e cronograma)
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. Cronograma anual — raiz do calendário institucional
-- Representa o ano letivo completo de uma instituição.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cronograma_anual (
    id_cronograma_anual     BIGINT          NOT NULL AUTO_INCREMENT,
    ano                     YEAR            NOT NULL,
    data_inicio             DATE            NOT NULL,
    data_fim                DATE            NOT NULL,
    dias_letivos_previstos  SMALLINT        UNSIGNED NULL,   -- ex: 200 (mínimo MEC)
    carga_horaria_prevista  SMALLINT        UNSIGNED NULL,   -- ex: 800 horas
    status                  VARCHAR(20)     NOT NULL DEFAULT 'RASCUNHO',
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_cronograma_anual),
    CONSTRAINT chk_cronograma_status
        CHECK (status IN ('RASCUNHO','ATIVO','ENCERRADO')),
    CONSTRAINT chk_cronograma_datas
        CHECK (data_fim > data_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 2. Período letivo — divisão do cronograma em bimestres/trimestres/semestres
-- Cada período tem data de início, fim e ordem dentro do cronograma.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS periodo_letivo (
    id_periodo_letivo       BIGINT          NOT NULL AUTO_INCREMENT,
    id_cronograma_anual     BIGINT          NOT NULL,
    nome                    VARCHAR(100)    NOT NULL,   -- "1º Bimestre"
    tipo                    VARCHAR(20)     NOT NULL,   -- BIMESTRE, TRIMESTRE, SEMESTRE
    data_inicio             DATE            NOT NULL,
    data_fim                DATE            NOT NULL,
    ordem                   TINYINT         UNSIGNED NOT NULL,
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_periodo_letivo),
    UNIQUE KEY uk_periodo_letivo_ordem (id_cronograma_anual, ordem),
    CONSTRAINT fk_periodo_letivo_cronograma
        FOREIGN KEY (id_cronograma_anual)
        REFERENCES cronograma_anual (id_cronograma_anual),
    CONSTRAINT chk_periodo_letivo_tipo
        CHECK (tipo IN ('BIMESTRE','TRIMESTRE','SEMESTRE')),
    CONSTRAINT chk_periodo_letivo_datas
        CHECK (data_fim > data_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 3. Evento do calendário — feriados, recessos, férias, eventos institucionais
-- eh_letivo = FALSE desconta o dia da contagem de dias letivos.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS evento_calendario (
    id_evento_calendario    BIGINT          NOT NULL AUTO_INCREMENT,
    id_cronograma_anual     BIGINT          NOT NULL,
    titulo                  VARCHAR(200)    NOT NULL,
    descricao               TEXT            NULL,
    data_inicio             DATE            NOT NULL,
    data_fim                DATE            NOT NULL,
    tipo_evento             VARCHAR(20)     NOT NULL,
    eh_letivo               BOOLEAN         NOT NULL DEFAULT FALSE,
    cor                     CHAR(7)         NULL,      -- hex: #FF5733
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_evento_calendario),
    CONSTRAINT fk_evento_calendario_cronograma
        FOREIGN KEY (id_cronograma_anual)
        REFERENCES cronograma_anual (id_cronograma_anual),
    CONSTRAINT chk_evento_tipo
        CHECK (tipo_evento IN ('FERIAS','FERIADO','RECESSO','EVENTO','SUSPENSAO')),
    CONSTRAINT chk_evento_datas
        CHECK (data_fim >= data_inicio),
    CONSTRAINT chk_evento_cor
        CHECK (cor IS NULL OR cor REGEXP '^#[0-9A-Fa-f]{6}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 4. Classe-Cronograma — ponte entre uma classe e o cronograma anual
-- Substitui futuramente o vínculo classe → calendario_escolar.
-- Um UNIQUE garante que a mesma classe não seja vinculada duas vezes
-- ao mesmo cronograma.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS classe_cronograma (
    id_classe_cronograma    BIGINT          NOT NULL AUTO_INCREMENT,
    id_classe               BIGINT          NOT NULL,
    id_cronograma_anual     BIGINT          NOT NULL,
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_classe_cronograma),
    UNIQUE KEY uk_classe_cronograma (id_classe, id_cronograma_anual),
    CONSTRAINT fk_classe_cronograma_classe
        FOREIGN KEY (id_classe)
        REFERENCES classe (id_classe),
    CONSTRAINT fk_classe_cronograma_cronograma
        FOREIGN KEY (id_cronograma_anual)
        REFERENCES cronograma_anual (id_cronograma_anual)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
