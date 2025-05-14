package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import java.util.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(WebSockets) {
            pingPeriod = 15.seconds
            timeout = Duration.ofSeconds(60)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
        install(CORS) {
            anyHost()
        }
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
