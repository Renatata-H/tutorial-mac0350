package model

import kotlinx.serialization.Serializable

@Serializable
data class Message (
    val username: String, 
    val message: String, 
    val time: String
)
