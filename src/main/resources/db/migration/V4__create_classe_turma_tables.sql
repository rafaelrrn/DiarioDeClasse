-- =============================================================================
-- V4 — Tabelas do módulo Turma (parte 2: Classe e AlunoTurma)
-- Depende de: V1 (pessoa), V2 (instituicao_ensino, curso, turno),
--             V3 (turma, componente_curricular).
--
-- Classe representa uma turma em um contexto específico: a combinação de
-- uma turma, um professor, um curso, uma instituição e um componente curricular.
-- É o "diário de classe" propriamente dito.
-- =============================================================================

-- Classe: associa turma + professor + instituição + curso + turno + componente.
-- É a entidade central do sistema — o professor lança frequência e notas pela Classe.
CREATE TABLE IF NOT EXISTS classe (
    id_classe                   BIGINT      NOT NULL AUTO_INCREMENT,
    id_instituicao_ensino       BIGINT      NOT NULL,
    id_componente_curricular    BIGINT,                  -- nullable: turma pode não ter componente definido
    id_curso                    BIGINT      NOT NULL,
    id_turno                    BIGINT      NOT NULL,
    id_turma                    BIGINT      NOT NULL,
    id_professor                BIGINT      NOT NULL,    -- FK para pessoa (tipo PROFESSOR)
    created_at                  DATETIME    NOT NULL DEFAULT NOW(),
    updated_at                  DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at                  DATETIME    NULL,
    ativo                       BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_classe),
    CONSTRAINT fk_id_instituicao_ensino    FOREIGN KEY (id_instituicao_ensino)    REFERENCES instituicao_ensino    (id_instituicao_ensino),
    CONSTRAINT fk_id_componente_curricular FOREIGN KEY (id_componente_curricular) REFERENCES componente_curricular (id_componente_curricular),
    CONSTRAINT fk_id_curso                 FOREIGN KEY (id_curso)                 REFERENCES curso                 (id_curso),
    CONSTRAINT fk_id_turno                 FOREIGN KEY (id_turno)                 REFERENCES turno                 (id_turno),
    CONSTRAINT fk_id_turma                 FOREIGN KEY (id_turma)                 REFERENCES turma                 (id_turma),
    CONSTRAINT fk_id_professor             FOREIGN KEY (id_professor)             REFERENCES pessoa                (id_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Matrícula do aluno em uma turma.
-- Um aluno pode estar em múltiplas turmas ao longo dos anos.
CREATE TABLE IF NOT EXISTS aluno_turma (
    id_aluno_turma  BIGINT      NOT NULL AUTO_INCREMENT,
    id_aluno        BIGINT      NOT NULL,    -- FK para pessoa (tipo ALUNO)
    id_turma        BIGINT      NOT NULL,
    obs             VARCHAR(255),
    created_at      DATETIME    NOT NULL DEFAULT NOW(),
    updated_at      DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME    NULL,
    ativo           BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aluno_turma),
    CONSTRAINT fk_aluno_turma_aluno FOREIGN KEY (id_aluno) REFERENCES pessoa (id_pessoa),
    CONSTRAINT fk_aluno_turma_turma FOREIGN KEY (id_turma) REFERENCES turma  (id_turma)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
