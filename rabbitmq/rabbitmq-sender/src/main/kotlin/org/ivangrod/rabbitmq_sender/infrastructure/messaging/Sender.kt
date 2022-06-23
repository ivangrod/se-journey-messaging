package org.ivangrod.rabbitmq_sender.infrastructure.messaging

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.ivangrod.rabbitmq_sender.infrastructure.messaging.dto.Message
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate

class Sender(private val rabbitTemplate: RabbitTemplate) {

    fun send(subject: String, content: String) {

        log.info("Send msg with subject $subject to message broker")

        rabbitTemplate.convertAndSend(
            "homelander-exchange",
            "v.routing-key",
            Json.encodeToString(Message(subject, content))
        )
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(Sender::class.java)
    }
}
