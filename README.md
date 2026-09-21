# srv-gestao-pelada
[Em fase de desenvolvimento] - API backend para gerenciamento de peladas de futebol, desenvolvida em Java com Spring Boot. Responsável pelo cadastro e gestão de usuários, peladas, jogadores e partidas.

## Arquitetura

O projeto utiliza arquitetura em camadas MVC:

- `controller`: endpoints REST e conversão entre requisições e respostas HTTP.
- `service`: regras de negócio e orquestração dos casos de uso.
- `repository`: persistência com Spring Data JPA.
- `model/dto`: contratos usados pelo controller e pelo service.
- `model/entity`: entidade JPA exclusiva da persistência.
- `mapper`: conversão entre DTOs e a entidade de persistência.

### Estrutura de pacotes

```text
br.com.gestao_pelada
├── controller
├── mapper
├── repository
├── service
└── model
    ├── dto
    └── entity
```
