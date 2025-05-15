# chat em broadcast com websocket

O template desse projeto foi criado com [Ktor Project Generator](https://start.ktor.io)

## Features

Alguns plugins utilizados nesse projeto:

| Nome do plugin                                                         | Descrição                                                                        |
| ------------------------------------------------------------------------|------------------------------------------------------------------------------------ |
| [Routing](https://start.ktor.io/p/routing)                             | Fornece uma DSL estruturada para roteamento                                        |
| [Static Content](https://start.ktor.io/p/static-content)               | Serve arquivos estáticos a partir de locais definidos                              |
| [WebSockets](https://start.ktor.io/p/ktor-websockets)                  | Adiciona suporte ao protocolo WebSocket para conexões bidirecionais com o cliente  |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Fornece conversão automática de conteúdo com base nos cabeçalhos Content-Type e Accept |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | 	Realiza a serialização de JSON usando a biblioteca kotlinx.serialization          |

## Buildar e rodar


| Task                          | Descrição                                                              |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Faz os testes automatizados                                            |
| `./gradlew build`             | Builda tudo                                                            |
| `run`                         | Inicializa o server                                                    |
| `runDocker`                   | Executar usando a imagem Docker local.                                 |

Se o servidor iniciar com sucesso, você verá a seguinte saída:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

Basta abrir no seu navegador em http://0.0.0.0:8080/static.
