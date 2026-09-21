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

## Usuários de exemplo (seed)

| E-mail                 | Senha       | Papel        |
|-------------------------|-------------|--------------|
| admin@pelada.com        | admin123    | ADMIN        |
| carlos@pelada.com       | senha123    | ORGANIZADOR  |
| joao@pelada.com         | senha123    | JOGADOR      |

## Testes

```powershell
./mvnw test
```

Os testes usam banco H2 em memória (perfil `test`, veja `src/test/resources/application-test.yml`) e cobrem:

- Estratégias de sorteio (testes unitários puros).
- Fluxo de autenticação (registro, login, refresh) e autorização por papel via MockMvc.
- Fluxo ponta a ponta: jogadores → pelada → partida → participantes → sorteio → eventos → estatísticas.