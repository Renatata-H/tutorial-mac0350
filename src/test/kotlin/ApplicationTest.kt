package com.example
import io.ktor.server.testing.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.statement.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Message
import kotlin.time.Duration.Companion.seconds

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            install(io.ktor.server.websocket.WebSockets) {
                pingPeriod = 15.seconds
                timeout = 60.seconds
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            install(CORS) {
                anyHost()
            }
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
            install(io.ktor.server.websocket.WebSockets) {
                pingPeriod = 15.seconds
                timeout = 60.seconds
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            install(CORS) {
                anyHost()
            }
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
            install(io.ktor.server.websocket.WebSockets) {
                pingPeriod = 15.seconds
                timeout = 60.seconds
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            install(CORS) {
                anyHost()
            }
            configureSerialization()
            configureRouting()
        }

        val clientWithWebSocket = client.config {
            install(WebSockets) // Instala o plugin WebSockets
        }

        runBlocking {
            clientWithWebSocket.webSocket("/chat") {
                val quitMessage = Message("TestUser", "quit", "[14/05/2025]")
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
