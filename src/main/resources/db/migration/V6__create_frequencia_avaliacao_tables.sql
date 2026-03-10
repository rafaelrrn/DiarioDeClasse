-- =============================================================================
-- V6 — Tabelas dos módulos Frequência e Avaliação
-- Depende de: V1 (pessoa), V5 (calendario_escolar), V3 (disciplina).
--
-- Frequência: registra a presença ou falta de cada aluno em cada aula.
-- Avaliação:  registra as avaliações (provas, trabalhos) e as notas dos alunos.
-- =============================================================================

-- Registro de frequência de um aluno em um calendário escolar (aula/mês).
-- Tipos de falta: PRESENTE, FALTA, FALTA_JUSTIFICADA (ver regra da LDB Art. 24).
CREATE TABLE IF NOT EXISTS aluno_frequencia (
    id_aluno_frequencia     BIGINT      NOT NULL AUTO_INCREMENT,
    id_aluno                BIGINT      NOT NULL,   -- FK para pessoa (tipo ALUNO)
    id_calendario_escolar   BIGINT      NOT NULL,
    faltas                  VARCHAR(255),
    created_at              DATETIME    NOT NULL DEFAULT NOW(),
    updated_at              DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME    NULL,
    ativo                   BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aluno_frequencia),
    CONSTRAINT fk_frequencia_aluno      FOREIGN KEY (id_aluno)              REFERENCES pessoa            (id_pessoa),
    CONSTRAINT fk_frequencia_calendario FOREIGN KEY (id_calendario_escolar) REFERENCES calendario_escolar (id_calendario_escolar)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Avaliação: prova ou trabalho aplicado em uma disciplina em um período letivo.
-- O campo 'dia' registra a data da avaliação; 'materia' registra o conteúdo avaliado.
CREATE TABLE IF NOT EXISTS avaliacao (
    id_avaliacao            BIGINT      NOT NULL AUTO_INCREMENT,
    id_disciplina           BIGINT      NOT NULL,
    id_calendario_escolar   BIGINT,             -- nullable: avaliação pode ser criada antes do calendário
    materia                 VARCHAR(255),
    dia                     VARCHAR(255),
    created_at              DATETIME    NOT NULL DEFAULT NOW(),
    updated_at              DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME    NULL,
    ativo                   BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_avaliacao),
    CONSTRAINT fk_avaliacao_disciplina   FOREIGN KEY (id_disciplina)         REFERENCES disciplina         (id_disciplina),
    CONSTRAINT fk_avaliacao_calendario   FOREIGN KEY (id_calendario_escolar) REFERENCES calendario_escolar (id_calendario_escolar)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Nota do aluno em uma avaliação específica.
-- A nota é um FLOAT para suportar casas decimais (ex: 7.5, 8.3).
CREATE TABLE IF NOT EXISTS aluno_avaliacao (
    id_aluno_avaliacao  BIGINT      NOT NULL AUTO_INCREMENT,
    id_aluno            BIGINT      NOT NULL,   -- FK para pessoa (tipo ALUNO)
    id_avaliacao        BIGINT      NOT NULL,
    nota                FLOAT,
    obs                 VARCHAR(255),
    created_at          DATETIME    NOT NULL DEFAULT NOW(),
    updated_at          DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME    NULL,
    ativo               BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aluno_avaliacao),
    CONSTRAINT fk_aluno_avaliacao_aluno     FOREIGN KEY (id_aluno)     REFERENCES pessoa    (id_pessoa),
    CONSTRAINT fk_aluno_avaliacao_avaliacao FOREIGN KEY (id_avaliacao) REFERENCES avaliacao (id_avaliacao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
