package org.ivangrod.rabbitmq_receiver.infrastructure.messaging

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ivangrod.rabbitmq_receiver.infrastructure.messaging.dto.Message

class Receiver {

    fun receiveMessage(message: String) {
        val msg = Json.decodeFromString<Message>(message)
        println("Received <$msg>")
    }
}
