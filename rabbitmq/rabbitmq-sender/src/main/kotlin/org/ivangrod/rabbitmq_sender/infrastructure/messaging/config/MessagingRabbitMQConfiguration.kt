package org.ivangrod.rabbitmq_sender.infrastructure.messaging.config

import org.ivangrod.rabbitmq_sender.infrastructure.messaging.Sender
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MessagingRabbitMQConfiguration {

    @Bean
    fun sender(rabbitTemplate: RabbitTemplate): Sender? {
        return Sender(rabbitTemplate)
    }
}
