# Simplified PicPay Backend

Este projeto é uma implementação simplificada do [desafio backend do PicPay](https://github.com/PicPay/picpay-desafio-backend). A aplicação foi desenvolvida em Java com Spring Boot e segue uma arquitetura em camadas (controller, service, repository), utilizando Spring Data JPA e H2 como banco de dados em memória.

## Índice

- [Visão Geral](#visão-geral)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Entidades e DTOs](#entidades-e-dtos)
- [Regras de Negócio](#regras-de-negócio)
- [Fluxo de Processos](#fluxo-de-processos)
- [Endpoints da API](#endpoints-da-api)
- [Tratamento de Exceções](#tratamento-de-exceções)
- [Como Executar](#como-executar)
- [Exemplos de JSON](#exemplos-de-json)
- [Observações Finais](#observações-finais)

## Visão Geral

A aplicação permite:

- Gerenciar usuários (criar, listar, editar e deletar)
- Realizar transações financeiras entre usuários
- Validar regras de negócio (saldo, tipo de usuário)
- Integrar-se com serviços externos para autorização e notificação

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3
- Spring Data JPA
- H2 (banco em memória)
- Maven
- MapStruct (para mapeamento de DTOs)
- WebClient (para chamadas HTTP a serviços externos)

## Estrutura do Projeto

```
src/main/
├── java/com/simplified/picpay
│   ├── configuration/          # Configurações (beans, WebClient)
│   ├── controller/             # Rest controllers
│   ├── model/
│   │   ├── domain/             # Entidades JPA (User, Transaction)
│   │   ├── dto/                # Objeto de Transferência de Dados
│   │   └── mapper/             # Mapeamento entre entidade e DTO
│   ├── repository/             # Interfaces Spring Data JPA
│   └── service/                # Lógica de negócio
└── resources/
    └── application.properties  # Configurações de ambiente
```

## Entidades e DTOs

### User (model.domain.user.User)
Representa o usuário da plataforma.

**Campos principais:**
- `id`
- `firstName`
- `lastName`
- `email`
- `document` (CPF)
- `password`
- `balance`
- `userType` (COMMON ou MERCHANT)

### Transaction (model.domain.transaction.Transaction)
Representa uma transação entre dois usuários.

**Campos principais:**
- `id`
- `amount`
- `sender` (ManyToOne User)
- `receiver` (ManyToOne User)
- `time`

### DTOs
- **UserDTO**: transporte de dados do usuário, utilizado em todas as operações (criação, atualização, leitura)
- **TransactionDTO**: transporte de dados para criação de transações (value, senderId, receiverId)

## Regras de Negócio

1. **Saldo Insuficiente**: um usuário do tipo COMMON não pode realizar uma transação cujo valor exceda seu saldo

2. **Tipo de Usuário**: apenas usuários COMMON podem ser remetentes; MERCHANT não pode iniciar transações

3. **Autorização Externa**: antes de efetivar, a transação é enviada ao serviço externo (`https://util.devi.tools/api/v2/authorize`) para autorização. Se a resposta for negativa, a transação é negada

4. **Notificação**: após executar a transação e atualizar saldos, ambos usuário remetente e destinatário são notificados via serviço externo (`https://util.devi.tools/api/v2/notify`)

## Fluxo de Processos

1. O cliente chama `POST /transactions/new-transaction` com um `TransactionDTO`
2. **Validação Local**: o `TransactionService` valida tipo de usuário e saldo
3. **Autorização Externa**: chama-se o WebClient para `util.auth.transaction.api.url`
4. **Se autorizado**:
   - Cria-se a entidade `Transaction` e persiste no banco
   - Atualiza saldos de remetente e destinatário
   - Salva as alterações de `User`
   - Envia chamadas de notificação para ambos
   - Retorna-se 202 ACCEPTED com o objeto `Transaction`

## Endpoints da API

### Usuários (`/users`)
- `GET /users` - lista todos os usuários
- `GET /users/{id}` - busca usuário por ID
- `POST /users/create` - cria novo usuário
- `PATCH /users/update/{id}` - atualiza dados de um usuário
- `DELETE /users/delete/{id}` - remove usuário

### Transações (`/transactions`)
- `POST /transactions/new-transaction` - cria nova transação

## Tratamento de Exceções

Definido em `ControllerExceptionHandler`:

- **DataIntegrityViolationException**: retorna 409 CONFLICT com mensagem apropriada
- **AccessDeniedException**: retorna 403 FORBIDDEN para transações não autorizadas
- **RuntimeException**: retorna 500 INTERNAL_SERVER_ERROR para erros gerais

## Como Executar

1. **Clonar o repositório:**
   ```bash
   git clone https://github.com/joaobotoni/simplified-picpay.git
   cd simplified-picpay/picpay
   ```

2. **Compilar e rodar com Maven:**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

3. **Acessar o H2 Console (opcional):**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`

## Exemplos de JSON

### Request para criação de transação:
```json
{
  "senderId": 1,
  "receiverId": 2,
  "value": 50.0
}
```

### Response de exemplo (status 202 ACCEPTED):
```json
{
  "id": 3,
  "amount": 50.0,
  "sender": {
    "id": 1,
    "firstName": "João",
    "lastName": "da Silva",
    "email": "joao@example.com",
    "balance": 950.0,
    "userType": "COMMON"
  },
  "receiver": {
    "id": 2,
    "firstName": "Maria",
    "lastName": "Oliveira",
    "email": "maria@example.com",
    "balance": 550.0,
    "userType": "COMMON"
  },
  "time": "2025-06-30T17:45:00"
}
```

## Observações Finais

- O projeto utiliza banco em memória H2 para facilitar testes. Em produção, configurar um banco persistente
- As URLs de autorizações e notificações podem ser configuradas em `application.properties`
- MapStruct gera o mapper em `target/generated-sources`
