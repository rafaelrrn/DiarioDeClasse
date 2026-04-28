-- =============================================================================
-- V12 — Cria tabelas de Aula, AlunoFrequencia e GradeCurricular.
--        Migra Avaliacao para referenciar periodo_letivo.
--
-- A tabela aula substitui o vínculo com calendario_escolar,
-- passando a referenciar periodo_letivo diretamente.
-- A tabela aluno_frequencia é refatorada para granularidade por aula/dia.
-- A tabela grade_curricular define o planejamento semanal de cada classe.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. Grade Curricular — planejamento semanal fixo de cada classe
-- Define quais dias da semana e horários cada classe acontece.
-- É usada para pré-gerar as aulas do período no início do bimestre.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS grade_curricular (
    id_grade_curricular     BIGINT          NOT NULL AUTO_INCREMENT,
    id_classe               BIGINT          NOT NULL,
    dia_semana              TINYINT         UNSIGNED NOT NULL, -- 1=Seg, 2=Ter, 3=Qua, 4=Qui, 5=Sex, 6=Sab
    numero_aula             TINYINT         UNSIGNED NOT NULL, -- 1ª aula, 2ª aula do dia
    horario_inicio          TIME            NOT NULL,
    horario_fim             TIME            NOT NULL,
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_grade_curricular),
    UNIQUE KEY uk_grade_curricular (id_classe, dia_semana, numero_aula),
    CONSTRAINT fk_grade_curricular_classe
        FOREIGN KEY (id_classe) REFERENCES classe (id_classe),
    CONSTRAINT chk_grade_dia_semana
        CHECK (dia_semana BETWEEN 1 AND 6),
    CONSTRAINT chk_grade_numero_aula
        CHECK (numero_aula BETWEEN 1 AND 10),
    CONSTRAINT chk_grade_horario
        CHECK (horario_fim > horario_inicio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 2. Aula — representa uma aula específica de uma classe em um dia
-- Granularidade: uma linha por classe por dia por número de aula.
-- O professor abre a chamada aqui antes de lançar as presenças.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS aula (
    id_aula                 BIGINT          NOT NULL AUTO_INCREMENT,
    id_classe               BIGINT          NOT NULL,
    id_periodo_letivo       BIGINT          NOT NULL,
    data_aula               DATE            NOT NULL,
    numero_aula             TINYINT         UNSIGNED NOT NULL,
    conteudo_ministrado     VARCHAR(500)    NULL,
    chamada_encerrada       BOOLEAN         NOT NULL DEFAULT FALSE,
    registrado_por          BIGINT          NOT NULL,  -- FK para pessoa (professor)
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aula),
    UNIQUE KEY uk_aula (id_classe, data_aula, numero_aula),
    CONSTRAINT fk_aula_classe
        FOREIGN KEY (id_classe) REFERENCES classe (id_classe),
    CONSTRAINT fk_aula_periodo_letivo
        FOREIGN KEY (id_periodo_letivo) REFERENCES periodo_letivo (id_periodo_letivo),
    CONSTRAINT fk_aula_registrado_por
        FOREIGN KEY (registrado_por) REFERENCES pessoa (id_pessoa),
    CONSTRAINT chk_aula_numero
        CHECK (numero_aula BETWEEN 1 AND 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 3. Recria aluno_frequencia com granularidade por aula
-- A tabela existente (V6+V8) vinculava ao calendario_escolar — estrutura legada.
-- Agora vincula diretamente à aula para granularidade por dia de aula.
-- -----------------------------------------------------------------------------

-- Remove FK antiga para permitir o DROP
ALTER TABLE aluno_frequencia
    DROP FOREIGN KEY fk_frequencia_calendario;

-- Remove a tabela legada
DROP TABLE aluno_frequencia;

-- Recria com novo schema
CREATE TABLE aluno_frequencia (
    id_aluno_frequencia     BIGINT          NOT NULL AUTO_INCREMENT,
    id_aula                 BIGINT          NOT NULL,
    id_aluno                BIGINT          NOT NULL,
    tipo_frequencia         VARCHAR(30)     NOT NULL DEFAULT 'PRESENTE',
    justificativa           VARCHAR(500)    NULL,
    created_at              DATETIME        NOT NULL DEFAULT NOW(),
    updated_at              DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME        NULL,
    ativo                   BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aluno_frequencia),
    UNIQUE KEY uk_aluno_frequencia (id_aula, id_aluno),
    CONSTRAINT fk_frequencia_aula
        FOREIGN KEY (id_aula) REFERENCES aula (id_aula),
    CONSTRAINT fk_frequencia_aluno
        FOREIGN KEY (id_aluno) REFERENCES pessoa (id_pessoa),
    CONSTRAINT chk_tipo_frequencia
        CHECK (tipo_frequencia IN ('PRESENTE','FALTA','FALTA_JUSTIFICADA'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 4. Migra avaliacao para referenciar periodo_letivo
-- Mantém id_calendario_escolar como nullable durante a transição.
-- A FK antiga só será removida na Etapa C (pós-MVP).
-- -----------------------------------------------------------------------------
ALTER TABLE avaliacao
    ADD COLUMN id_periodo_letivo BIGINT NULL,
    ADD CONSTRAINT fk_avaliacao_periodo_letivo
        FOREIGN KEY (id_periodo_letivo)
        REFERENCES periodo_letivo (id_periodo_letivo);

-- -----------------------------------------------------------------------------
-- 5. Adiciona tipo em avaliacao (PROVA, TRABALHO, RECUPERACAO, SIMULADO, OUTRO)
-- Campo ausente identificado na análise inicial.
-- -----------------------------------------------------------------------------
ALTER TABLE avaliacao
    ADD COLUMN tipo VARCHAR(30) NOT NULL DEFAULT 'PROVA',
    ADD CONSTRAINT chk_avaliacao_tipo
        CHECK (tipo IN ('PROVA','TRABALHO','RECUPERACAO','SIMULADO','OUTRO'));

-- -----------------------------------------------------------------------------
-- 6. Corrige campo dia em avaliacao: VARCHAR → DATE
-- Identificado na análise inicial como tipo incorreto.
-- -----------------------------------------------------------------------------
ALTER TABLE avaliacao
    ADD COLUMN dia_new DATE NULL;

UPDATE avaliacao
    SET dia_new = STR_TO_DATE(dia, '%Y-%m-%d')
    WHERE dia IS NOT NULL
    AND dia REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2}$';

ALTER TABLE avaliacao
    DROP COLUMN dia,
    CHANGE dia_new dia DATE NULL;
