# srv-gestao-pelada

API backend para gerenciamento de peladas de futebol, desenvolvida em **Java 25 / Spring Boot 4**. Responsável pelo
cadastro e gestão de usuários, peladas, jogadores, partidas, times, sorteios e estatísticas.

## Stack

- Java 25 + Spring Boot 4 (Web, Validation, Security, Data JPA)
- PostgreSQL + Flyway (migrations e seed de dados)
- JWT (access + refresh token) com autorização contextual por pelada (`ADMIN`, `ORGANIZADOR`, `JOGADOR`)
- MapStruct + Lombok
- springdoc-openapi (Swagger UI)
- JUnit 5, MockMvc, AssertJ, H2 (testes), Testcontainers (disponível para testes com Postgres real)
- Docker / Docker Compose

## Arquitetura

O projeto utiliza arquitetura modular por domínio (feature package) e camadas explícitas:

- `controller`: contratos OpenAPI e adaptadores REST (`*Api` e `*Controller`).
- `service`: casos de uso e regras de negócio.
- `repository`: interfaces Spring Data JPA para persistência.
- `model/entity`: entidades JPA.
- `model/enums`: enums do módulo.
- `model/dto`: objetos de entrada e saída da API.
- `model/mapper`: mapeamentos entre entidades e DTOs (MapStruct).
- `infrastructure`: integrações técnicas externas, quando necessárias (`client`, configurações ou adaptadores).

O fluxo principal é `controller -> service -> repository`. A organização por feature evita que um módulo dependa de
detalhes internos de outro.

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

Exemplo de estrutura interna:

```text
pelada/
├── controller/
├── service/
├── repository/
└── model/
    ├── entity/
    ├── dto/
    └── mapper/
```

## Endpoints

Todos os endpoints usam o prefixo `/api/v1`. Os endpoints protegidos exigem
`Authorization: Bearer <accessToken>`

| Módulo | Endpoints |
|---|---|
| Auth | `POST /auth/register`, `POST /auth/complete-registration`, `POST /auth/login`, `POST /auth/refresh` |
| Jogadores | `POST/GET /jogadores`, `GET/PUT/DELETE /jogadores/{id}` |
| Peladas | `POST/GET /peladas`, `GET/PUT/DELETE /peladas/{id}` |
| Membros | `POST /peladas/{id}/membros`, `POST /peladas/{id}/membros/provisionar`, `PATCH /peladas/{id}/membros/{usuarioId}/papel` |
| Solicitações | `POST/GET /peladas/{id}/solicitacoes-entrada`, `PATCH /peladas/{id}/solicitacoes-entrada/{solicitacaoId}` |
| Jogadores da pelada | `POST/GET /peladas/{id}/jogadores`, `DELETE /peladas/{id}/jogadores/{jogadorId}` |
| Partidas | `POST/GET /peladas/{peladaId}/partidas`, `GET/PUT/DELETE /partidas/{id}`, `PATCH /partidas/{id}/status` |
| Participantes | `POST/GET /partidas/{partidaId}/participantes`, `PATCH /partidas/{partidaId}/participantes/{jogadorId}/presenca`, `DELETE /partidas/{partidaId}/participantes/{jogadorId}` |
| Sorteio | `POST /partidas/{partidaId}/sorteio` (`POTES`, `ESTRELAS` ou `AVULSO`) |
| Times | `GET/DELETE /partidas/{partidaId}/times` |
| Eventos | `POST/GET /partidas/{partidaId}/eventos`, `DELETE /eventos/{id}` |
| Estatísticas | `GET /peladas/{peladaId}/estatisticas/ranking`, `/artilheiros`, `/assistencias` e `/cartoes` |

Não existem papéis globais. Todo usuário registrado pode criar uma pelada e torna-se `ADMIN` dela. As permissões
de escrita dependem do papel nessa pelada; a leitura é restrita a membros. Documentação interativa disponível em
`/swagger-ui.html` (OpenAPI em `/v3/api-docs`).

## Sorteio de times (Strategy pattern)

Três estratégias de sorteio, implementadas em `sorteio/service/strategy`:

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

Os usuários do seed não possuem papel global. `carlos@pelada.com` é migrado como `ADMIN` da Pelada do Bairro.

## Fluxo de uso da API

Passo a passo simulando o ciclo de vida completo de uma pelada: autenticação → criação da pelada → entrada de
jogadores → criação da partida → confirmação de presença → sorteio de times → registro de
eventos → consulta de estatísticas.

> Substitua `SEU_TOKEN` pelo `accessToken` retornado no login, e os IDs numéricos de exemplo pelos valores reais retornados
> em cada etapa.

### 1. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@pelada.com", "senha": "admin123"}'
```

Retorna `accessToken`, `refreshToken`, `tokenType` e `expiresInMs`. Use `/api/v1/auth/refresh` com o `refreshToken`
para renovar o `accessToken` quando expirar.

### 2. Criar a pelada

```bash
curl -X POST http://localhost:8080/api/v1/peladas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome": "Pelada da esquina", "diaSemana": "QUARTA", "horario": "19:00:00", "local": "Quadra Central"}'
```

O usuário autenticado torna-se `ADMIN` automaticamente.

### 3. Adicionar jogadores à pelada

```bash
curl -X POST http://localhost:8080/api/v1/peladas/ID_DA_PELADA/membros \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"email": "jogador@pelada.com", "papel": "JOGADOR"}'
```

Também é possível solicitar entrada em `POST /api/v1/peladas/{id}/solicitacoes-entrada`,
provisionar um jogador em `POST /api/v1/peladas/{id}/membros/provisionar` e 
delegar papel em `PATCH /api/v1/peladas/{id}/membros/{usuarioId}/papel`.

### 4. Aprovar solicitações e concluir cadastros

```bash
curl -X PATCH http://localhost:8080/api/v1/peladas/ID_DA_PELADA/solicitacoes-entrada/ID_DA_SOLICITACAO \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "APROVADA"}'
```

Contas provisionadas possuem `cadastroConcluido=false` e não podem fazer login. O usuário conclui o cadastro em
`POST /api/v1/auth/complete-registration`, informando `email`, `senhaInicial` e `novaSenha`.

### 5. Criar uma partida

```bash
curl -X POST http://localhost:8080/api/v1/peladas/ID_DA_PELADA/partidas \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"data": "2026-10-01", "horario": "19:00:00", "local": "Quadra Central", "numeroTimes": 2, "jogadoresPorTime": 5}'
```

Guarde o `id` da partida retornado.

### 6. Confirmar participantes

```bash
curl -X POST http://localhost:8080/api/v1/partidas/ID_DA_PARTIDA/participantes \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"jogadorId": "ID_DO_JOGADOR", "goleiro": false}'
```

Repita para cada jogador confirmado. Opcionalmente, use
`PATCH /api/v1/partidas/{partidaId}/participantes/{jogadorId}/presenca` (payload `{"presente": true}`) no dia do
jogo para marcar quem efetivamente compareceu.

### 7. Sortear os times

```bash
curl -X POST http://localhost:8080/api/v1/partidas/ID_DA_PARTIDA/sorteio \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"tipo": "ESTRELAS", "numeroTimes": 2}'
```

Retorna a lista de times com seus jogadores. Consulte novamente com `GET /api/v1/partidas/{partidaId}/times`, ou
refaça o sorteio removendo os times atuais (`DELETE /api/v1/partidas/{partidaId}/times`).

### 8. Iniciar a partida

```bash
curl -X PATCH http://localhost:8080/api/v1/partidas/ID_DA_PARTIDA/status \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "EM_ANDAMENTO"}'
```

### 9. Registrar eventos (gols, assistências, cartões)

```bash
curl -X POST http://localhost:8080/api/v1/partidas/ID_DA_PARTIDA/eventos \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"jogadorId": "ID_DO_JOGADOR", "timeId": "ID_DO_TIME", "tipo": "GOL", "minuto": 23}'
```

Repita para cada evento ocorrido durante a partida (`tipo`: `GOL`, `ASSISTENCIA`, `CARTAO_AMARELO`,
`CARTAO_VERMELHO`).

### 10. Finalizar a partida

```bash
curl -X PATCH http://localhost:8080/api/v1/partidas/ID_DA_PARTIDA/status \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"status": "FINALIZADA"}'
```

### 11. Consultar estatísticas da pelada

```bash
curl http://localhost:8080/api/v1/peladas/ID_DA_PELADA/estatisticas/ranking \
  -H "Authorization: Bearer SEU_TOKEN" \
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
