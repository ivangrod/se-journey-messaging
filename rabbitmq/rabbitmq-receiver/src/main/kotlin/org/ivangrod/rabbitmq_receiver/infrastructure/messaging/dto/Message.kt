package org.ivangrod.rabbitmq_receiver.infrastructure.messaging.dto

import kotlinx.serialization.Serializable

@Serializable
data class Message(val subject: String, val content: String)
