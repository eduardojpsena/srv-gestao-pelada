-- V1: schema inicial da API de gestao de peladas.

CREATE TABLE usuarios (
    id             UUID PRIMARY KEY,
    nome           VARCHAR(150) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    senha_hash     VARCHAR(255) NOT NULL,
    role           VARCHAR(20)  NOT NULL,
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em      TIMESTAMP    NOT NULL,
    CONSTRAINT uk_usuarios_email UNIQUE (email)
);

CREATE TABLE jogadores (
    id             UUID PRIMARY KEY,
    usuario_id     UUID,
    nome           VARCHAR(150) NOT NULL,
    apelido        VARCHAR(100),
    telefone       VARCHAR(30),
    email          VARCHAR(150),
    nota_geral     DOUBLE PRECISION NOT NULL DEFAULT 3.0,
    posicao        VARCHAR(20),
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em      TIMESTAMP    NOT NULL,
    CONSTRAINT fk_jogadores_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);

CREATE INDEX idx_jogadores_usuario_id ON jogadores (usuario_id);

CREATE TABLE peladas (
    id             UUID PRIMARY KEY,
    nome           VARCHAR(150) NOT NULL,
    descricao      VARCHAR(500),
    dia_semana     VARCHAR(20),
    horario        TIME,
    local          VARCHAR(200),
    organizador_id UUID NOT NULL,
    status         VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    criado_em      TIMESTAMP NOT NULL,
    CONSTRAINT fk_peladas_organizador FOREIGN KEY (organizador_id) REFERENCES usuarios (id)
);

CREATE INDEX idx_peladas_organizador_id ON peladas (organizador_id);

CREATE TABLE pelada_jogadores (
    id             UUID PRIMARY KEY,
    pelada_id      UUID NOT NULL,
    jogador_id     UUID NOT NULL,
    nota_pelada    DOUBLE PRECISION,
    mensalista     BOOLEAN NOT NULL DEFAULT FALSE,
    ativo          BOOLEAN NOT NULL DEFAULT TRUE,
    data_entrada   TIMESTAMP NOT NULL,
    CONSTRAINT fk_pj_pelada FOREIGN KEY (pelada_id) REFERENCES peladas (id) ON DELETE CASCADE,
    CONSTRAINT fk_pj_jogador FOREIGN KEY (jogador_id) REFERENCES jogadores (id) ON DELETE CASCADE,
    CONSTRAINT uk_pelada_jogador UNIQUE (pelada_id, jogador_id)
);

CREATE INDEX idx_pj_pelada_id ON pelada_jogadores (pelada_id);
CREATE INDEX idx_pj_jogador_id ON pelada_jogadores (jogador_id);

CREATE TABLE partidas (
    id                 UUID PRIMARY KEY,
    pelada_id          UUID NOT NULL,
    data               DATE NOT NULL,
    horario            TIME,
    local              VARCHAR(200),
    status             VARCHAR(20) NOT NULL DEFAULT 'AGENDADA',
    numero_times       INTEGER NOT NULL DEFAULT 2,
    jogadores_por_time INTEGER,
    criado_em          TIMESTAMP NOT NULL,
    CONSTRAINT fk_partidas_pelada FOREIGN KEY (pelada_id) REFERENCES peladas (id) ON DELETE CASCADE
);

CREATE INDEX idx_partidas_pelada_id ON partidas (pelada_id);

CREATE TABLE participantes (
    id             UUID PRIMARY KEY,
    partida_id     UUID NOT NULL,
    jogador_id     UUID NOT NULL,
    confirmado     BOOLEAN NOT NULL DEFAULT TRUE,
    presente       BOOLEAN NOT NULL DEFAULT FALSE,
    goleiro        BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_participantes_partida FOREIGN KEY (partida_id) REFERENCES partidas (id) ON DELETE CASCADE,
    CONSTRAINT fk_participantes_jogador FOREIGN KEY (jogador_id) REFERENCES jogadores (id) ON DELETE CASCADE,
    CONSTRAINT uk_partida_jogador UNIQUE (partida_id, jogador_id)
);

CREATE INDEX idx_participantes_partida_id ON participantes (partida_id);
CREATE INDEX idx_participantes_jogador_id ON participantes (jogador_id);

CREATE TABLE times (
    id             UUID PRIMARY KEY,
    partida_id     UUID NOT NULL,
    nome           VARCHAR(100) NOT NULL,
    cor            VARCHAR(30),
    nota_total     DOUBLE PRECISION,
    CONSTRAINT fk_times_partida FOREIGN KEY (partida_id) REFERENCES partidas (id) ON DELETE CASCADE
);

CREATE INDEX idx_times_partida_id ON times (partida_id);

CREATE TABLE time_jogadores (
    id             UUID PRIMARY KEY,
    time_id        UUID NOT NULL,
    jogador_id     UUID NOT NULL,
    goleiro        BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_tj_time FOREIGN KEY (time_id) REFERENCES times (id) ON DELETE CASCADE,
    CONSTRAINT fk_tj_jogador FOREIGN KEY (jogador_id) REFERENCES jogadores (id) ON DELETE CASCADE
);

CREATE INDEX idx_tj_time_id ON time_jogadores (time_id);
CREATE INDEX idx_tj_jogador_id ON time_jogadores (jogador_id);

CREATE TABLE eventos (
    id             UUID PRIMARY KEY,
    partida_id     UUID NOT NULL,
    time_id        UUID,
    jogador_id     UUID NOT NULL,
    tipo           VARCHAR(20) NOT NULL,
    minuto         INTEGER,
    criado_em      TIMESTAMP NOT NULL,
    CONSTRAINT fk_eventos_partida FOREIGN KEY (partida_id) REFERENCES partidas (id) ON DELETE CASCADE,
    CONSTRAINT fk_eventos_time FOREIGN KEY (time_id) REFERENCES times (id) ON DELETE SET NULL,
    CONSTRAINT fk_eventos_jogador FOREIGN KEY (jogador_id) REFERENCES jogadores (id) ON DELETE CASCADE
);

CREATE INDEX idx_eventos_partida_id ON eventos (partida_id);
CREATE INDEX idx_eventos_jogador_id ON eventos (jogador_id);