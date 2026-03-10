-- =============================================================================
-- V1 — Tabelas do módulo Pessoa
-- Cria as tabelas relacionadas a pessoas do sistema (alunos, professores,
-- responsáveis) e seus dados de contato e endereço.
--
-- Nota: Todas as tabelas incluem os campos de auditoria herdados de BaseEntity:
--   created_at, updated_at — gerenciados pelo Spring Data JPA Auditing
--   deleted_at, ativo      — soft delete (nunca deletar fisicamente)
-- =============================================================================

-- Categoriza o tipo de uma pessoa: ALUNO, PROFESSOR, RESPONSAVEL, etc.
CREATE TABLE IF NOT EXISTS tipo_pessoa (
    id_tipo_pessoa  BIGINT          NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(255),
    created_at      DATETIME        NOT NULL DEFAULT NOW(),
    updated_at      DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME        NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_tipo_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Representa qualquer pessoa no sistema (aluno, professor, responsável, etc.).
-- O tipo é determinado pelo relacionamento com tipo_pessoa.
CREATE TABLE IF NOT EXISTS pessoa (
    id_pessoa       BIGINT          NOT NULL AUTO_INCREMENT,
    id_tipo_pessoa  BIGINT          NOT NULL,
    nome            VARCHAR(255),
    sexo            VARCHAR(255),
    data_nascimento VARCHAR(255),
    situacao        VARCHAR(255),
    obs             VARCHAR(255),
    created_at      DATETIME        NOT NULL DEFAULT NOW(),
    updated_at      DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME        NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_pessoa),
    CONSTRAINT fk_id_tipo_pessoa FOREIGN KEY (id_tipo_pessoa) REFERENCES tipo_pessoa (id_tipo_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Representa um meio de contato (telefone, e-mail, etc.) sem vínculo direto com Pessoa.
CREATE TABLE IF NOT EXISTS contato (
    id_contato      BIGINT          NOT NULL AUTO_INCREMENT,
    tipo_contato    VARCHAR(255),
    contato         VARCHAR(255),
    created_at      DATETIME        NOT NULL DEFAULT NOW(),
    updated_at      DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME        NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_contato)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de associação N:N entre Pessoa e Contato, com atributo 'nome'.
CREATE TABLE IF NOT EXISTS contato_pessoa (
    id_contato_pessoa   BIGINT      NOT NULL AUTO_INCREMENT,
    id_pessoa           BIGINT      NOT NULL,
    id_contato          BIGINT      NOT NULL,
    nome                VARCHAR(255),
    created_at          DATETIME    NOT NULL DEFAULT NOW(),
    updated_at          DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME    NULL,
    ativo               BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_contato_pessoa),
    CONSTRAINT fk_contato_pessoa_pessoa  FOREIGN KEY (id_pessoa)  REFERENCES pessoa  (id_pessoa),
    CONSTRAINT fk_contato_pessoa_contato FOREIGN KEY (id_contato) REFERENCES contato (id_contato)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Representa um endereço sem vínculo direto com uma Pessoa.
CREATE TABLE IF NOT EXISTS endereco (
    id_endereco     BIGINT          NOT NULL AUTO_INCREMENT,
    uf              VARCHAR(255),
    cidade          VARCHAR(255),
    bairro          VARCHAR(255),
    rua             VARCHAR(255),
    numero          VARCHAR(255),
    cep             VARCHAR(255),
    complemento     VARCHAR(255),
    created_at      DATETIME        NOT NULL DEFAULT NOW(),
    updated_at      DATETIME        NOT NULL DEFAULT NOW(),
    deleted_at      DATETIME        NULL,
    ativo           BOOLEAN         NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabela de associação N:N entre Pessoa e Endereco, com atributo 'nome'.
CREATE TABLE IF NOT EXISTS endereco_pessoa (
    id_endereco_pessoa  BIGINT      NOT NULL AUTO_INCREMENT,
    id_pessoa           BIGINT      NOT NULL,
    id_endereco         BIGINT      NOT NULL,
    nome                VARCHAR(255),
    created_at          DATETIME    NOT NULL DEFAULT NOW(),
    updated_at          DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at          DATETIME    NULL,
    ativo               BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_endereco_pessoa),
    CONSTRAINT fk_endereco_pessoa_pessoa   FOREIGN KEY (id_pessoa)   REFERENCES pessoa   (id_pessoa),
    CONSTRAINT fk_endereco_pessoa_endereco FOREIGN KEY (id_endereco) REFERENCES endereco (id_endereco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Associação entre aluno (Pessoa tipo ALUNO) e seu responsável legal.
-- Um aluno pode ter múltiplos responsáveis (pai, mãe, tutor).
CREATE TABLE IF NOT EXISTS pessoa_responsavel (
    id_pessoa_responsavel   BIGINT      NOT NULL AUTO_INCREMENT,
    id_aluno                BIGINT      NOT NULL,
    id_responsavel          BIGINT      NOT NULL,
    parentesco              VARCHAR(255),
    created_at              DATETIME    NOT NULL DEFAULT NOW(),
    updated_at              DATETIME    NOT NULL DEFAULT NOW(),
    deleted_at              DATETIME    NULL,
    ativo                   BOOLEAN     NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id_pessoa_responsavel),
    CONSTRAINT fk_id_aluno      FOREIGN KEY (id_aluno)      REFERENCES pessoa (id_pessoa),
    CONSTRAINT fk_id_responsavel FOREIGN KEY (id_responsavel) REFERENCES pessoa (id_pessoa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
