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
    status VARCHAR(255) NOT NULL,
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
    telefone VARCHAR(11) UNIQUE NOT NULL,
    departamento VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    criado_em TIMESTAMPTZ DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'ALUNO')),
    polo_id BIGINT NOT NULL REFERENCES polos(id) ON DELETE CASCADE,
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

INSERT INTO cursos (nome, descricao) VALUES
('Análise e Desenvolvimento de Sistemas', 'Curso voltado para desenvolvimento de software, lógica e infraestrutura.'),
('Administração', 'Curso focado em gestão, negócios e finanças.'),
('Pedagogia', 'Formação de professores e especialistas em educação.'),
('Enfermagem', 'Curso voltado para cuidados de saúde e suporte clínico.'),
('Ciências Contábeis', 'Formação na área contábil, fiscal e financeira.');

INSERT INTO disciplinas (curso_id, nome, descricao) VALUES
(1, 'Lógica de Programação', 'Conceitos básicos de lógica e algoritmos.'),
(1, 'Programação Orientada a Objetos', 'POO com Java.'),
(1, 'Banco de Dados', 'Modelagem e SQL.'),
(1, 'Desenvolvimento Web', 'HTML, CSS, JavaScript e frameworks.'),
(1, 'Estrutura de Dados', 'Listas, filas, árvores e algoritmos.');

INSERT INTO disciplinas (curso_id, nome, descricao) VALUES
(2, 'Fundamentos de Administração', 'Princípios básicos da administração.'),
(2, 'Marketing', 'Conceitos e estratégias de marketing.'),
(2, 'Finanças Empresariais', 'Gestão financeira e orçamentária.'),
(2, 'Gestão de Pessoas', 'RH e desenvolvimento organizacional.'),
(2, 'Empreendedorismo', 'Criação e gestão de novos negócios.');

INSERT INTO disciplinas (curso_id, nome, descricao) VALUES
(3, 'História da Educação', 'Evolução dos modelos educacionais.'),
(3, 'Psicologia da Educação', 'Aspectos cognitivos e comportamentais.'),
(3, 'Didática', 'Técnicas e práticas pedagógicas.'),
(3, 'Língua Portuguesa', 'Leitura, escrita e análise textual.'),
(3, 'Metodologias Ativas', 'Novas abordagens para ensino-aprendizagem.');

INSERT INTO disciplinas (curso_id, nome, descricao) VALUES
(4, 'Anatomia Humana', 'Estudo da estrutura do corpo humano.'),
(4, 'Microbiologia', 'Estudo de microrganismos e saúde.'),
(4, 'Enfermagem Clínica', 'Práticas e fundamentos clínicos.'),
(4, 'Farmacologia', 'Medicamentos e aplicações clínicas.'),
(4, 'Saúde Coletiva', 'Estratégias de saúde pública.');

INSERT INTO disciplinas (curso_id, nome, descricao) VALUES
(5, 'Contabilidade Geral', 'Fundamentos da contabilidade.'),
(5, 'Contabilidade de Custos', 'Métodos de custeio e análise.'),
(5, 'Direito Empresarial', 'Aspectos legais e jurídicos.'),
(5, 'Auditoria Contábil', 'Processos e técnicas de auditoria.'),
(5, 'Controladoria', 'Gestão e controle financeiro.');

INSERT INTO polos (nome, endereco, cidade, estado, capacidade) VALUES
('Teresópolis', 'Av. Principal, 1200', 'Teresópolis', 'RJ', 50),
('Petrópolis', 'Rua das Flores, 85', 'Petrópolis', 'RJ', 50),
('Nova Friburgo', 'Rodovia BR-101, km 45', 'Nova Friburgo', 'RJ', 50);

INSERT INTO professores (nome, email, telefone, departamento, status) VALUES
('Ana Paula', 'ana.paula@faculdade.edu', '21987654321', 'Sistemas de Informação', 'ATIVO'),
('Carlos Silva', 'carlos.silva@faculdade.edu', '21998127745', 'Administração', 'ATIVO'),
('Mariana Costa', 'mariana.costa@faculdade.edu', '21986441209', 'Pedagogia', 'ATIVO'),
('João Pereira', 'joao.pereira@faculdade.edu', '21995738801', 'Enfermagem', 'ATIVO'),
('Fernanda Lima', 'fernanda.lima@faculdade.edu', '21984225698', 'Contabilidade', 'ATIVO');

INSERT INTO salas (polo_id, nome, capacidade, recursos, status) VALUES
(1, 'Sala 101', 30, 'Projetor, Quadro Branco', 'Disponível'),
(1, 'Sala 102', 25, 'Computadores, Quadro Branco', 'Disponível'),
(2, 'Sala 201', 40, 'Projetor, Mesa de Laboratório', 'Disponível'),
(2, 'Sala 202', 35, 'Computadores, Quadro Branco', 'Disponível'),
(3, 'Sala 301', 50, 'Auditório, Projetor', 'Disponível');

INSERT INTO provas (disciplina_id, professor_id, sala_id, titulo, descricao, inicio, fim) VALUES
(1, 1, 1, 'Prova Lógica de Programação', 'Avaliação da disciplina', '2025-12-01 09:00:00', '2025-12-01 11:00:00'),
(2, 1, 2, 'Prova POO', 'Avaliação da disciplina', '2025-12-02 14:00:00', '2025-12-02 16:00:00'),
(3, 1, 1, 'Prova Banco de Dados', 'Avaliação da disciplina', '2025-12-03 09:00:00', '2025-12-03 11:00:00'),
(4, 1, 2, 'Prova Desenvolvimento Web', 'Avaliação da disciplina', '2025-12-04 13:00:00', '2025-12-04 15:00:00'),
(5, 1, 1, 'Prova Estrutura de Dados', 'Avaliação da disciplina', '2025-12-05 08:00:00', '2025-12-05 10:00:00'),

(6, 2, 3, 'Prova Fundamentos de Administração', 'Avaliação da disciplina', '2025-12-06 10:00:00', '2025-12-06 12:00:00'),
(7, 2, 4, 'Prova Marketing', 'Avaliação da disciplina', '2025-12-07 14:00:00', '2025-12-07 16:00:00'),
(8, 2, 3, 'Prova Finanças Empresariais', 'Avaliação da disciplina', '2025-12-08 10:00:00', '2025-12-08 12:00:00'),
(9, 2, 4, 'Prova Gestão de Pessoas', 'Avaliação da disciplina', '2025-12-09 09:00:00', '2025-12-09 11:00:00'),
(10, 2, 3, 'Prova Empreendedorismo', 'Avaliação da disciplina', '2025-12-10 13:00:00', '2025-12-10 15:00:00'),

(11, 3, 4, 'Prova História da Educação', 'Avaliação da disciplina', '2025-12-11 09:00:00', '2025-12-11 11:00:00'),
(12, 3, 4, 'Prova Psicologia da Educação', 'Avaliação da disciplina', '2025-12-12 08:00:00', '2025-12-12 10:00:00'),
(13, 3, 4, 'Prova Didática', 'Avaliação da disciplina', '2025-12-13 14:00:00', '2025-12-13 16:00:00'),
(14, 3, 4, 'Prova Língua Portuguesa', 'Avaliação da disciplina', '2025-12-14 10:00:00', '2025-12-14 12:00:00'),
(15, 3, 4, 'Prova Metodologias Ativas', 'Avaliação da disciplina', '2025-12-15 13:00:00', '2025-12-15 15:00:00'),

(16, 4, 5, 'Prova Anatomia Humana', 'Avaliação da disciplina', '2025-12-16 08:00:00', '2025-12-16 10:00:00'),
(17, 4, 5, 'Prova Microbiologia', 'Avaliação da disciplina', '2025-12-17 14:00:00', '2025-12-17 16:00:00'),
(18, 4, 5, 'Prova Enfermagem Clínica', 'Avaliação da disciplina', '2025-12-18 10:00:00', '2025-12-18 12:00:00'),
(19, 4, 5, 'Prova Farmacologia', 'Avaliação da disciplina', '2025-12-19 09:00:00', '2025-12-19 11:00:00'),
(20, 4, 5, 'Prova Saúde Coletiva', 'Avaliação da disciplina', '2025-12-20 08:00:00', '2025-12-20 10:00:00'),

(21, 5, 1, 'Prova Contabilidade Geral', 'Avaliação da disciplina', '2025-12-21 14:00:00', '2025-12-21 16:00:00'),
(22, 5, 1, 'Prova Contabilidade de Custos', 'Avaliação da disciplina', '2025-12-22 13:00:00', '2025-12-22 15:00:00'),
(23, 5, 1, 'Prova Direito Empresarial', 'Avaliação da disciplina', '2025-12-23 09:00:00', '2025-12-23 11:00:00'),
(24, 5, 1, 'Prova Auditoria', 'Avaliação da disciplina', '2025-12-24 10:00:00', '2025-12-24 12:00:00'),
(25, 5, 1, 'Prova Controladoria', 'Avaliação da disciplina', '2025-12-25 08:00:00', '2025-12-25 10:00:00');

