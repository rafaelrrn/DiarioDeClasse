-- =============================================================================
-- V10 — Complementa a tabela pessoa e cria tabelas de perfil específico
--        (Extended Party Pattern).
--
-- Todos os passos são idempotentes: podem ser reexecutados com segurança
-- após DELETE FROM flyway_schema_history WHERE version = '10'.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. Adiciona cpf e foto_url (apenas se ainda não existirem)
-- -----------------------------------------------------------------------------
SET @col_exists = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'db-diario-de-classe'
      AND TABLE_NAME   = 'pessoa'
      AND COLUMN_NAME  = 'cpf'
);
SET @sql = IF(@col_exists = 0,
    'ALTER TABLE pessoa ADD COLUMN cpf CHAR(11) NULL, ADD COLUMN foto_url VARCHAR(500) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists = (
    SELECT COUNT(*) FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = 'db-diario-de-classe'
      AND TABLE_NAME   = 'pessoa'
      AND INDEX_NAME   = 'uk_pessoa_cpf'
);
SET @sql = IF(@idx_exists = 0,
    'ALTER TABLE pessoa ADD UNIQUE KEY uk_pessoa_cpf (cpf)',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- -----------------------------------------------------------------------------
-- 2. Ajusta situacao: VARCHAR → VARCHAR(30) + CHECK (apenas se necessário)
-- -----------------------------------------------------------------------------
SET @col_len = (
    SELECT CHARACTER_MAXIMUM_LENGTH FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = 'db-diario-de-classe'
      AND TABLE_NAME   = 'pessoa'
      AND COLUMN_NAME  = 'situacao'
);
SET @sql = IF(@col_len > 30,
    'ALTER TABLE pessoa MODIFY COLUMN situacao VARCHAR(30) NULL',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @con_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE TABLE_SCHEMA   = 'db-diario-de-classe'
      AND TABLE_NAME     = 'pessoa'
      AND CONSTRAINT_NAME = 'chk_situacao'
);
SET @sql = IF(@con_exists = 0,
    'ALTER TABLE pessoa ADD CONSTRAINT chk_situacao CHECK (situacao IN (''ATIVO'',''INATIVO'',''TRANSFERIDO'',''EVADIDO'',''FORMADO''))',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- -----------------------------------------------------------------------------
-- 3. Normaliza dados de sexo e adiciona CHECK
--
-- Execuções parciais anteriores gravaram UPPER(LEFT(sexo,2)), gerando valores
-- como 'MA', 'FE' que violam o CHECK. Este bloco corrige antes de adicionar
-- a constraint.
-- -----------------------------------------------------------------------------
SET @con_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE TABLE_SCHEMA   = 'db-diario-de-classe'
      AND TABLE_NAME     = 'pessoa'
      AND CONSTRAINT_NAME = 'chk_sexo'
);

-- Só normaliza e adiciona o CHECK se ele ainda não existir
SET @do_sexo = (@con_exists = 0);

SET @sql = IF(@do_sexo,
    'UPDATE pessoa SET sexo = ''M'' WHERE sexo NOT IN (''M'',''F'',''NB'',''NI'') AND sexo IS NOT NULL AND UPPER(LEFT(sexo,1)) = ''M''',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(@do_sexo,
    'UPDATE pessoa SET sexo = ''F'' WHERE sexo NOT IN (''M'',''F'',''NB'',''NI'') AND sexo IS NOT NULL AND UPPER(LEFT(sexo,1)) = ''F''',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Valores que não puderam ser mapeados vão para NULL (sem perda silenciosa)
SET @sql = IF(@do_sexo,
    'UPDATE pessoa SET sexo = NULL WHERE sexo IS NOT NULL AND sexo NOT IN (''M'',''F'',''NB'',''NI'')',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @sql = IF(@do_sexo,
    'ALTER TABLE pessoa ADD CONSTRAINT chk_sexo CHECK (sexo IN (''M'',''F'',''NB'',''NI''))',
    'SELECT 1'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- -----------------------------------------------------------------------------
-- 4. Perfil de Aluno (Extended Party Pattern)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS aluno_perfil (
    id_aluno_perfil      BIGINT      NOT NULL AUTO_INCREMENT,
    id_pessoa            BIGINT      NOT NULL,
    matricula            VARCHAR(30) NOT NULL,
    data_matricula       DATE        NOT NULL,
    necessidade_especial BOOLEAN     NOT NULL DEFAULT FALSE,
    descricao_nee        TEXT        NULL,
    created_at           DATETIME    NOT NULL DEFAULT NOW(),
    updated_at           DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at           DATETIME    NULL,
    ativo                BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_aluno_perfil),
    UNIQUE KEY uk_aluno_perfil_pessoa    (id_pessoa),
    UNIQUE KEY uk_aluno_perfil_matricula (matricula),
    CONSTRAINT fk_aluno_perfil_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 5. Perfil de Professor (Extended Party Pattern)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS professor_perfil (
    id_professor_perfil BIGINT       NOT NULL AUTO_INCREMENT,
    id_pessoa           BIGINT       NOT NULL,
    registro_mec        VARCHAR(30)  NULL,
    formacao            VARCHAR(200) NULL,
    data_admissao       DATE         NOT NULL,
    created_at          DATETIME     NOT NULL DEFAULT NOW(),
    updated_at          DATETIME     NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME     NULL,
    ativo               BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_professor_perfil),
    UNIQUE KEY uk_professor_perfil_pessoa (id_pessoa),
    CONSTRAINT fk_professor_perfil_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 6. Perfil de Responsável (Extended Party Pattern)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS responsavel_perfil (
    id_responsavel_perfil BIGINT       NOT NULL AUTO_INCREMENT,
    id_pessoa             BIGINT       NOT NULL,
    profissao             VARCHAR(100) NULL,
    local_trabalho        VARCHAR(200) NULL,
    created_at            DATETIME     NOT NULL DEFAULT NOW(),
    updated_at            DATETIME     NOT NULL DEFAULT NOW(),
    deleted_at            DATETIME     NULL,
    ativo                 BOOLEAN      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_responsavel_perfil),
    UNIQUE KEY uk_responsavel_perfil_pessoa (id_pessoa),
    CONSTRAINT fk_responsavel_perfil_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa (id_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
