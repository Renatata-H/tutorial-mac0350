package com.example
import io.ktor.server.testing.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.statement.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Message

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        client.get("/").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun testWebSocketConnection() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        val clientWithWebSocket = client.config {
            install(WebSockets) // Instala o plugin WebSockets
        }

        runBlocking {
            clientWithWebSocket.webSocket("/chat") {
                val testMessage = Message("TestUser", "Hello, World!", "2025-05-14T12:00:00Z")
                val formattedMessage = Json.encodeToString(testMessage)

                // Enviando mensagem
                send(Frame.Text(formattedMessage))

                // Recebendo a resposta
                val frame = incoming.receive() as? Frame.Text
                assertEquals(formattedMessage, frame?.readText())
            }
        }
    }

    @Test
    fun testQuitMessage() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }

        runBlocking {
            client.webSocket("/chat") {
                val quitMessage = Message("TestUser", "quit", "2025-05-14T12:00:00Z")
                val formattedQuitMessage = Json.encodeToString(quitMessage)

                // Enviando mensagem de quit
                send(Frame.Text(formattedQuitMessage))

                // Verificando se a conexão foi fechada
                val closeReason = closeReason.await()
                assertEquals(CloseReason(CloseReason.Codes.NORMAL, "User quit"), closeReason)
            }
        }
    }
}
