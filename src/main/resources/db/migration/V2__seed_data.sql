-- V2: dados de exemplo (seed) para ambiente de desenvolvimento/demonstracao.

INSERT INTO usuarios (id, nome, email, senha_hash, role, ativo, criado_em) VALUES
    (1, 'Administrador', 'admin@pelada.com', '$2a$10$8mFd0MoxOsxoON1ZZ6fCTezPRzW/nyBelO0ZD/njBk0hNgQ8C1WVm', 'ADMIN', TRUE, now()),
    (2, 'Carlos Organizador', 'carlos@pelada.com', '$2a$10$E3porolDmqroxGneCmmkZu16gj3qr2uvUnJJQLKGo0Iwf5NzZpnvm', 'ORGANIZADOR', TRUE, now()),
    (3, 'Joao Jogador', 'joao@pelada.com', '$2a$10$E3porolDmqroxGneCmmkZu16gj3qr2uvUnJJQLKGo0Iwf5NzZpnvm', 'JOGADOR', TRUE, now());

INSERT INTO jogadores (id, usuario_id, nome, apelido, telefone, email, nota_geral, posicao, ativo, criado_em) VALUES
    (1, 1, 'Joao Silva', 'Joaozinho', '11999990001', 'joao@pelada.com', 4.5, 'ATACANTE', TRUE, now()),
    (2, NULL, 'Pedro Souza', 'Pedrinho', '11999990002', 'pedro@pelada.com', 4.0, 'MEIA', TRUE, now()),
    (3, NULL, 'Lucas Lima', 'Lucao', '11999990003', 'lucas@pelada.com', 3.5, 'ZAGUEIRO', TRUE, now()),
    (4, NULL, 'Rafael Costa', 'Rafa', '11999990004', 'rafael@pelada.com', 5.0, 'GOLEIRO', TRUE, now()),
    (5, NULL, 'Bruno Alves', 'Bruninho', '11999990005', 'bruno@pelada.com', 3.0, 'LATERAL', TRUE, now()),
    (6, NULL, 'Marcos Rocha', 'Marquinhos', '11999990006', 'marcos@pelada.com', 4.2, 'MEIA', TRUE, now());

INSERT INTO peladas (id, nome, descricao, dia_semana, horario, local, organizador_id, status, criado_em) VALUES
    (1, 'Pelada do Bairro', 'Pelada semanal entre amigos', 'QUINTA', '19:30:00', 'Quadra Municipal', 1, 'ATIVA', now());

INSERT INTO pelada_jogadores (id, pelada_id, jogador_id, nota_pelada, mensalista, ativo, data_entrada) VALUES
    (1, 1, 1, 4.5, TRUE, TRUE, now()),
    (2, 1, 2, 4.0, TRUE, TRUE, now()),
    (3, 1, 3, 3.5, FALSE, TRUE, now()),
    (4, 1, 4, 5.0, TRUE, TRUE, now()),
    (5, 1, 5, 3.0, FALSE, TRUE, now()),
    (6, 1, 6, 4.2, TRUE, TRUE, now());

INSERT INTO partidas (id, pelada_id, data, horario, local, status, numero_times, jogadores_por_time, criado_em) VALUES
    (1, 1, CURRENT_DATE + INTERVAL '7 day', '19:30:00', 'Quadra Municipal', 'AGENDADA', 2, 6, now());

INSERT INTO participantes (id, partida_id, jogador_id, confirmado, presente, goleiro) VALUES
    (2, 1, 2, TRUE, FALSE, FALSE),
    (3, 1, 3, TRUE, FALSE, FALSE),
    (4, 1, 4, TRUE, FALSE, TRUE),
    (5, 1, 5, TRUE, FALSE, FALSE),
    (6, 1, 6, TRUE, FALSE, FALSE);

SELECT setval(pg_get_serial_sequence('usuarios', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM usuarios;
SELECT setval(pg_get_serial_sequence('jogadores', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM jogadores;
SELECT setval(pg_get_serial_sequence('peladas', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM peladas;
SELECT setval(pg_get_serial_sequence('pelada_jogadores', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM pelada_jogadores;
SELECT setval(pg_get_serial_sequence('partidas', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM partidas;
SELECT setval(pg_get_serial_sequence('participantes', 'id'), COALESCE(MAX(id), 0) + 1, false) FROM participantes;