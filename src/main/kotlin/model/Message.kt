package model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant 

@Serializable
data class Message(
    val username: String,
    val message: String,
    val time: Instant
)