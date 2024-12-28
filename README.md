# Loan Simulator API

API simulação de empréstimos bancários, construída com Java 21 e Spring Boot 3.4.1, utilizando Postgres como banco de dados.

### Tecnologias utilizadas
- Spring framework
- Spring data jpa, postgres e flyway migrations
- swagger
- sl4j e logback
- Kover, Mockito, mockK e h2 database
- klint
- Docker e Docker Compose

## Endpoints

### Simulação de empréstimo
Simula um empréstimo a partir de valor solicitado, data de nascimento e prazo de pgto em meses.
- **Método:** POST
- **Rota:** `/api/v1/loan-simulation`

## Documentação da API
### swagger-ui
Com a aplicação rodando em ambiente local é possível visualizar o doc através do http://localhost:8080/swagger-ui/index.html

### postman-request
A chamada do endpoint pode ser importada com o script abaixo
```
curl --location 'http://localhost:8080/api/v1/loan-simulation' \
--header 'Content-Type: application/json' \
--data '{
"value": 10000.00,
"dateBirth": "01/07/1940",
"durationInMonths": 24
}'
```

## Configuração do Ambiente

- Java 21
- Spring Boot 3.4.1
- Gradle
- Postgres

## Como Executar

1. Clone o repositório.
2. Configure as propriedades do banco de dados no arquivo `application.yml`. *Contém valores default
3. O banco de dados, pode ser provisionado a partir do arquivo `docker-compose.yml`
```
docker-compose up -d
```
. *Certifique-se de ter o Docker e o Docker Compose instalados em sua máquina.
4. Execute o projeto usando sua IDE ou através do comando `./mvnw spring-boot:run`.
```
./mvnw spring-boot:run
```
