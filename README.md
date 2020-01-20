# Desafio Dito
O desafio é composto de duas partes: uma api coletora de dados com um serviço de autocomplete e um problema de manipulação de dados.
## Linguagem e frameworks utilizados
> Java 8

> Spring Boot

> Lombok

> Gson

## Endpoints
> API Coletora

```
POST http://localhost:8080/api/events/collector
{
  "event": "buy",
  "timestamp": "2016-09-22T13:57:31.2311892-04:00"
}

```

> Autocomplete

```
GET http://localhost:8080/api/events/autocomplete/{keyword}
```

> Manipulação de Dados

```
GET http://localhost:8080/api/events/dataMnipulation
```
