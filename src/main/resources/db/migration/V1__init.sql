-- V1__init.sql
-- Inicialização do banco de dados do projeto

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS btree_gist;


CREATE TABLE IF NOT EXISTS polos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    capacidade INTEGER CHECK (capacidade >= 0),
    criado_em TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS salas (
    id BIGSERIAL PRIMARY KEY,
    polo_id BIGINT NOT NULL REFERENCES polos(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    capacidade INTEGER CHECK (capacidade >= 0),
    recursos TEXT,
    criado_em TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (polo_id, nome)
);

CREATE TABLE IF NOT EXISTS cursos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS disciplinas (
    id BIGSERIAL PRIMARY KEY,
    curso_id BIGINT NOT NULL REFERENCES cursos(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS professores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    criado_em TIMESTAMPTZ DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'ALUNO')),
    polo_id BIGINT REFERENCES polos(id) ON DELETE CASCADE,
    criado_em TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS matriculas (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    curso_id BIGINT NOT NULL REFERENCES cursos(id) ON DELETE CASCADE,
    data_matricula TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (user_id, curso_id)
);

CREATE TABLE IF NOT EXISTS usuario_disciplina (
    id BIGSERIAL PRIMARY KEY,
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    cursando_disciplina BOOLEAN DEFAULT FALSE,
    criado_em TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE (disciplina_id, user_id)
);


CREATE TABLE IF NOT EXISTS provas (
    id BIGSERIAL PRIMARY KEY,
    disciplina_id BIGINT NOT NULL REFERENCES disciplinas(id) ON DELETE CASCADE,
    professor_id BIGINT NOT NULL REFERENCES professores(id) ON DELETE CASCADE,
    sala_id BIGINT NOT NULL REFERENCES salas(id) ON DELETE CASCADE,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    inicio TIMESTAMPTZ NOT NULL,
    fim TIMESTAMPTZ NOT NULL,
    criado_em TIMESTAMPTZ DEFAULT NOW(),
    CHECK (inicio <= fim)
);

ALTER TABLE provas
  ADD CONSTRAINT no_overlap_sala EXCLUDE USING gist (sala_id WITH =, tstzrange(inicio, fim, '[)') WITH &&),
  ADD CONSTRAINT no_overlap_professor EXCLUDE USING gist (professor_id WITH =, tstzrange(inicio, fim, '[)') WITH &&);

CREATE INDEX idx_provas_disciplina ON provas(disciplina_id);
CREATE INDEX idx_provas_periodo ON provas(inicio, fim);

CREATE TABLE IF NOT EXISTS agendamentos (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    prova_id BIGINT NOT NULL REFERENCES provas(id) ON DELETE CASCADE,
    polo_id BIGINT NOT NULL REFERENCES polos(id) ON DELETE CASCADE,
    data_agendamento TIMESTAMPTZ DEFAULT NOW(),
    tipo_status VARCHAR(50) DEFAULT 'PENDENTE' CHECK (tipo_status IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO')),
    UNIQUE (user_id, prova_id)
);


CREATE INDEX idx_users_polo ON users(polo_id);
CREATE INDEX idx_agendamentos_user ON agendamentos(user_id);
CREATE INDEX idx_agendamentos_prova ON agendamentos(prova_id);
CREATE INDEX idx_disciplinas_curso ON disciplinas(curso_id);


COMMENT ON TABLE provas IS 'Tabela de provas agendadas, com vinculação a sala, disciplina e professor';
COMMENT ON TABLE agendamentos IS 'Tabela de inscrições dos alunos nas provas';
COMMENT ON TABLE salas IS 'Salas pertencentes a um polo (campus físico)';
COMMENT ON COLUMN provas.inicio IS 'Data/hora de início da prova (timestamptz)';
COMMENT ON COLUMN provas.fim IS 'Data/hora de término da prova (timestamptz)';
