package com.example

import io.ktor.server.application.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.util.Collections
import model.Message
import kotlinx.datetime.Clock

fun Application.configureChatSession(session: DefaultWebSocketServerSession) {
    // Lista sincronizada para armazenar as conexões WebSocket ativas
    val connections = Collections.synchronizedList<DefaultWebSocketServerSession>(mutableListOf())

    // Adiciona a nova conexão
    connections += session
    println("Novo cliente conectado: ${session.hashCode()}")

    try {
        // Recebe as mensagens enviadas por cada cliente
        session.incoming.consumeEach { frame ->
            if (frame is Frame.Text) {
                // Decodifica a mensagem recebida de JSON para o formato Message
                val receivedText = frame.readText()
                val receivedMessage = Json.decodeFromString<Message>(receivedText)

                // Se a mensagem for "quit", desconecta o cliente
                if (receivedMessage.message.lowercase() == "quit") {
                    session.send("Você saiu do chat.")
                    session.close(CloseReason(CloseReason.Codes.NORMAL, "Usuário saiu"))
                    return@consumeEach
                }

                // Transmite a mensagem para todos os clientes conectados
                val messageToBroadcast = receivedMessage.copy(time = Clock.System.now())
                val json = Json.encodeToString(messageToBroadcast)

                println("Mensagem de ${receivedMessage.username}: ${receivedMessage.message}")

                // Envia a mensagem para todos os outros clientes conectados, exceto para o remetente
                connections.forEach { otherSession ->
                    if (otherSession != session) {
                        otherSession.send(json)
                    }
                }
            }
        }
    } catch (e: Exception) {
        println("Erro: ${e.localizedMessage}")
    } finally {
        // Remove a conexão quando o cliente desconectar
        println("Cliente desconectado: ${session.hashCode()}")
        connections -= session
        session.close(CloseReason(CloseReason.Codes.NORMAL, "Conexão encerrada"))
    }
}
