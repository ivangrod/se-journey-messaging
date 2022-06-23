package org.ivangrod.rabbitmq_sender.infrastructure.messaging.config

import org.ivangrod.rabbitmq_sender.infrastructure.messaging.Sender
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MessagingRabbitMQConfiguration {

    @Bean
    fun sender(rabbitTemplate: RabbitTemplate): Sender? {
        return Sender(rabbitTemplate)
    }

    @Bean
    fun msgQueue(): Queue? {
        return Queue(INVOICE_QUEUE)
    }

    @Bean
    fun replyQueue(): Queue? {
        return Queue(INVOICE_REPLY_QUEUE)
    }

    @Bean
    fun exchange(): TopicExchange? {
        return TopicExchange(INVOICE_RPC_EXCHANGE)
    }

    @Bean
    fun msgBinding(): Binding? {
        return BindingBuilder.bind(msgQueue()).to(exchange()).with(INVOICE_QUEUE)
    }

    @Bean
    fun replyBinding(): Binding? {
        return BindingBuilder.bind(replyQueue()).to(exchange()).with(INVOICE_REPLY_QUEUE)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate? {
        val template = RabbitTemplate(connectionFactory)
        template.setReplyAddress(INVOICE_REPLY_QUEUE)
        template.setReplyTimeout(5000)
        return template
    }

    @Bean
    fun replyContainer(connectionFactory: ConnectionFactory): SimpleMessageListenerContainer? {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(INVOICE_REPLY_QUEUE)
        container.setMessageListener(rabbitTemplate(connectionFactory)!!)
        return container
    }

    companion object {
        const val INVOICE_QUEUE = "invoice_queue"
        const val INVOICE_REPLY_QUEUE = "invoice_reply_queue"
        const val INVOICE_RPC_EXCHANGE = "invoice_rpc_exchange"
    }
}
