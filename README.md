# srv-gestao-pelada

API backend para gerenciamento de peladas de futebol, desenvolvida em **Java 25 / Spring Boot 4**. Responsável pelo
cadastro e gestão de usuários, peladas, jogadores, partidas, times, sorteios e estatísticas.

## Stack

- Java 25 + Spring Boot 4 (Web, Validation, Security, Data JPA)
- PostgreSQL + Flyway (migrations e seed de dados)
- JWT (access + refresh token) com autorização baseada em papéis (`ADMIN`, `ORGANIZADOR`, `JOGADOR`)
- MapStruct + Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5, MockMvc, AssertJ, H2 (testes), Testcontainers (disponível para testes com Postgres real)
- Docker / Docker Compose

## Arquitetura

O projeto utiliza uma arquitetura modular por domínio (feature package), com camadas internas inspiradas em
Clean Architecture:

- `domain`: entidades JPA e enums exclusivos da persistência/regra de negócio.
- `application`: serviços (casos de uso), DTOs e mappers (MapStruct).
- `infrastructure`: controllers REST e repositórios Spring Data JPA.

### Estrutura de pacotes

```text
br.com.gestao_pelada
├── auth          # registro, login, refresh token, usuários
├── jogador       # cadastro de jogadores
├── pelada        # peladas e vínculo de jogadores à pelada
├── partida       # partidas e participantes (confirmação de presença)
├── time          # times formados a partir do sorteio
├── sorteio       # estratégias de sorteio (Strategy pattern)
├── evento        # eventos de partida (gol, assistência, cartões)
├── estatistica   # rankings e estatísticas agregadas
├── shared        # exceções, segurança (JWT), utilitários, enums comuns
└── config        # segurança (Spring Security) e OpenAPI
```

## Módulos e endpoints principais

| Módulo        | Endpoints                                                                 |
|---------------|-----------------------------------------------------------------------------|
| Auth          | `POST /api/v1/auth/register`, `/login`, `/refresh`                          |
| Jogadores     | `POST/GET/PUT/DELETE /api/v1/jogadores(/{id})`                              |
| Peladas       | `POST/GET/PUT/DELETE /api/v1/peladas(/{id})`, jogadores da pelada           |
| Partidas      | `POST/GET/PUT/PATCH/DELETE /api/v1/peladas/{peladaId}/partidas`, `/api/v1/partidas/{id}` |
| Participantes | `POST/GET/PATCH/DELETE /api/v1/partidas/{partidaId}/participantes`          |
| Sorteio       | `POST /api/v1/partidas/{partidaId}/sorteio` (estratégias: `POTES`, `ESTRELAS`, `AVULSO`) |
| Times         | `GET/DELETE /api/v1/partidas/{partidaId}/times`                             |
| Eventos       | `POST/GET/DELETE /api/v1/partidas/{partidaId}/eventos`, `/api/v1/eventos/{id}` |
| Estatísticas  | `GET /api/v1/peladas/{peladaId}/estatisticas/{ranking,artilheiros,assistencias,cartoes}` |

Endpoints de escrita (criar/atualizar/remover) exigem papel `ADMIN` ou `ORGANIZADOR`; leitura exige apenas usuário
autenticado. Documentação interativa disponível em `/swagger-ui.html` (OpenAPI em `/v3/api-docs`).

## Sorteio de times (Strategy pattern)

Três estratégias de sorteio, implementadas em `sorteio/domain/strategy`:

- **AVULSO**: distribuição totalmente aleatória.
- **ESTRELAS**: balanceamento guloso pela nota geral do jogador (maior soma de notas equilibrada entre os times).
- **POTES**: divide jogadores em potes por faixa de nota e distribui em "snake draft" entre os times.

## Executando localmente

### Com Docker Compose (recomendado)

```bash
docker compose up --build
```

Sobe a API em `http://localhost:8080` e um banco PostgreSQL. As migrations do Flyway (`src/main/resources/db/migration`)
são aplicadas automaticamente na subida, incluindo dados de exemplo (seed).

### Sem Docker

1. Suba um PostgreSQL local (ou ajuste as variáveis `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`).
2. Execute:

```powershell
./mvnw spring-boot:run
```

### H2 (local) vs. PostgreSQL

A aplicação possui dois profiles de banco de dados:

- **`dev`** (padrão): usa **H2 em memória**, sem exigir nenhuma infraestrutura externa — o schema é gerado
  automaticamente pelo Hibernate (`ddl-auto: create-drop`) e o Flyway fica desabilitado. Ideal para rodar
  rapidamente sem Docker/Postgres. Console disponível em `http://localhost:8080/h2-console`
  (JDBC URL `jdbc:h2:mem:gestao_pelada`, usuário `sa`, sem senha). A base sobe vazia; cadastre os dados via API.
- **`postgres`**: usa PostgreSQL com as migrations do Flyway e o seed de dados (veja a tabela de usuários abaixo).
  Ative com:

```powershell
$env:SPRING_PROFILES_ACTIVE = "postgres"
./mvnw spring-boot:run
```

## Usuários de exemplo (seed)

> Disponíveis apenas com o profile `postgres` (Flyway aplica o seed). No profile `dev` (H2), a base sobe vazia.

| E-mail                 | Senha       | Papel        |
|-------------------------|-------------|--------------|
| admin@pelada.com        | admin123    | ADMIN        |
| carlos@pelada.com       | senha123    | ORGANIZADOR  |
| joao@pelada.com         | senha123    | JOGADOR      |

## Fluxo de uso da API

Passo a passo simulando o ciclo de vida completo de uma pelada: autenticação → cadastro de jogadores → criação
da pelada → vínculo de jogadores → criação da partida → confirmação de presença → sorteio de times → registro de
eventos → consulta de estatísticas.

> Substitua `SEU_TOKEN` pelo `accessToken` retornado no login, e os UUIDs de exemplo pelos valores reais retornados
> em cada etapa.

### 1. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@pelada.com", "senha": "admin123"}'
```

Retorna `accessToken`, `refreshToken`, `tokenType` e `expiresInMs`. Use `/api/v1/auth/refresh` com o `refreshToken`
para renovar o `accessToken` quando expirar.

### 2. Cadastrar jogadores

```bash
curl -X POST http://localhost:8080/api/v1/jogadores \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome": "Carlos Silva", "apelido": "Carlinhos", "email": "carlos@pelada.com", "notaGeral": 4.5, "posicao": "ATACANTE"}'
```

Repita para os demais jogadores. Guarde os `id` (UUID) retornados.

### 3. Criar a pelada

```bash
curl -X POST http://localhost:8080/api/v1/peladas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome": "Pelada da Firma", "diaSemana": "QUARTA", "horario": "19:00:00", "local": "Quadra Central", "organizadorId": "UUID_DO_ORGANIZADOR"}'
```

Guarde o `id` da pelada retornado.

### 4. Vincular jogadores à pelada

```bash
curl -X POST http://localhost:8080/api/v1/peladas/UUID_DA_PELADA/jogadores \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"jogadorId": "UUID_DO_JOGADOR", "notaPelada": 4.0, "mensalista": true}'
```

Repita para cada jogador que participará da pelada.

### 5. Criar uma partida

```bash
curl -X POST http://localhost:8080/api/v1/peladas/UUID_DA_PELADA/partidas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"data": "2026-10-01", "horario": "19:00:00", "local": "Quadra Central", "numeroTimes": 2, "jogadoresPorTime": 5}'
```

Guarde o `id` da partida retornado.

### 6. Confirmar participantes

```bash
curl -X POST http://localhost:8080/api/v1/partidas/UUID_DA_PARTIDA/participantes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"jogadorId": "UUID_DO_JOGADOR", "goleiro": false}'
```

Repita para cada jogador confirmado. Opcionalmente, use
`PATCH /api/v1/partidas/{partidaId}/participantes/{jogadorId}/presenca` (payload `{"presente": true}`) no dia do
jogo para marcar quem efetivamente compareceu.

### 7. Sortear os times

```bash
curl -X POST http://localhost:8080/api/v1/partidas/UUID_DA_PARTIDA/sorteio \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo": "ESTRELAS", "numeroTimes": 2}'
```

Retorna a lista de times com seus jogadores. Consulte novamente com `GET /api/v1/partidas/{partidaId}/times`, ou
refaça o sorteio removendo os times atuais (`DELETE /api/v1/partidas/{partidaId}/times`).

### 8. Iniciar a partida

```bash
curl -X PATCH http://localhost:8080/api/v1/partidas/UUID_DA_PARTIDA/status \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "EM_ANDAMENTO"}'
```

### 9. Registrar eventos (gols, assistências, cartões)

```bash
curl -X POST http://localhost:8080/api/v1/partidas/UUID_DA_PARTIDA/eventos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"jogadorId": "UUID_DO_JOGADOR", "timeId": "UUID_DO_TIME", "tipo": "GOL", "minuto": 23}'
```

Repita para cada evento ocorrido durante a partida (`tipo`: `GOL`, `ASSISTENCIA`, `CARTAO_AMARELO`,
`CARTAO_VERMELHO`).

### 10. Finalizar a partida

```bash
curl -X PATCH http://localhost:8080/api/v1/partidas/UUID_DA_PARTIDA/status \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "FINALIZADA"}'
```

### 11. Consultar estatísticas da pelada

```bash
curl http://localhost:8080/api/v1/peladas/UUID_DA_PELADA/estatisticas/ranking \
  -H "Authorization: Bearer SEU_TOKEN"
```

Também disponíveis: `/estatisticas/artilheiros`, `/estatisticas/assistencias` e `/estatisticas/cartoes`.

> Toda a API está documentada e pode ser testada interativamente em `/swagger-ui.html`.

## Testes

```powershell
./mvnw test
```

Os testes usam banco H2 em memória (perfil `test`, veja `src/test/resources/application-test.yml`) e cobrem:

- Estratégias de sorteio (testes unitários puros).
- Fluxo de autenticação (registro, login, refresh) e autorização por papel via MockMvc.
- Fluxo ponta a ponta: jogadores → pelada → partida → participantes → sorteio → eventos → estatísticas.