ALTER TABLE usuarios
    ADD COLUMN cadastro_concluido BOOLEAN NOT NULL DEFAULT TRUE;

CREATE TABLE pelada_membros (
    id         UUID PRIMARY KEY,
    pelada_id  UUID NOT NULL,
    usuario_id UUID NOT NULL,
    papel      VARCHAR(20) NOT NULL,
    ativo      BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP NOT NULL,
    CONSTRAINT fk_pm_pelada FOREIGN KEY (pelada_id) REFERENCES peladas (id) ON DELETE CASCADE,
    CONSTRAINT fk_pm_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT uk_pelada_membro UNIQUE (pelada_id, usuario_id)
);

CREATE INDEX idx_pm_usuario_id ON pelada_membros (usuario_id);
CREATE INDEX idx_pm_pelada_id ON pelada_membros (pelada_id);

CREATE TABLE solicitacoes_entrada (
    id           UUID PRIMARY KEY,
    pelada_id    UUID NOT NULL,
    usuario_id   UUID NOT NULL,
    status       VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    criado_em    TIMESTAMP NOT NULL,
    decidido_em  TIMESTAMP,
    decidido_por UUID,
    CONSTRAINT fk_se_pelada FOREIGN KEY (pelada_id) REFERENCES peladas (id) ON DELETE CASCADE,
    CONSTRAINT fk_se_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_se_decisor FOREIGN KEY (decidido_por) REFERENCES usuarios (id)
);

CREATE INDEX idx_se_pelada_status ON solicitacoes_entrada (pelada_id, status);
CREATE UNIQUE INDEX uk_se_pendente
    ON solicitacoes_entrada (pelada_id, usuario_id)
    WHERE status = 'PENDENTE';

INSERT INTO pelada_membros (id, pelada_id, usuario_id, papel, ativo, criado_em)
SELECT gen_random_uuid(), p.id, p.organizador_id, 'ADMIN', TRUE, now()
FROM peladas p;

INSERT INTO pelada_membros (id, pelada_id, usuario_id, papel, ativo, criado_em)
SELECT gen_random_uuid(), pj.pelada_id, j.usuario_id, 'JOGADOR', TRUE, now()
FROM pelada_jogadores pj
JOIN jogadores j ON j.id = pj.jogador_id
WHERE j.usuario_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM pelada_membros pm
      WHERE pm.pelada_id = pj.pelada_id
        AND pm.usuario_id = j.usuario_id
  );

CREATE UNIQUE INDEX uk_jogadores_usuario_id
    ON jogadores (usuario_id)
    WHERE usuario_id IS NOT NULL;

DROP INDEX IF EXISTS idx_jogadores_usuario_id;
ALTER TABLE usuarios DROP COLUMN role;
