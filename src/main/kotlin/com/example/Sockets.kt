package com.example

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.*
import kotlinx.serialization.json.*
import kotlinx.datetime.*
import model.Message

val connections = mutableSetOf<DefaultWebSocketServerSession>()

suspend fun broadcast(message: String) {
    connections.forEach {
        it.send(Frame.Text(message))
    }
}
