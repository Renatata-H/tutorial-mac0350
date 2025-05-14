package com.example

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*  // Importando os Frames e CloseReason corretamente
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Message

fun Application.configureRouting() {
    routing {
        webSocket("/chat") {
            connections.add(this)
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val receivedText = frame.readText()
                        val message = Json.decodeFromString<Message>(receivedText)
                        val formattedMessage = Json.encodeToString(message)

                        // Broadcast para todos os clientes conectados
                        broadcast(formattedMessage)

                        // Remover conexão ao receber "quit"
                        if (message.message == "quit") {
                            connections.remove(this)
                            close(CloseReason(CloseReason.Codes.NORMAL, "User quit"))  // Correção aqui
                        }
                    }
                }
            } finally {
                connections.remove(this)
            }
        }

        staticResources("/static", "static")
    }
}
