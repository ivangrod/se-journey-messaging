package org.ivangrod.rabbitmq_receiver.infrastructure.messaging.config

import org.ivangrod.rabbitmq_receiver.infrastructure.messaging.Receiver
import org.ivangrod.rabbitmq_receiver.infrastructure.messaging.DownloadInvoiceReplier
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessagingRabbitMQConfiguration {

    val topicExchangeName = "homelander-exchange"

    val queueName = "homelander"

    @Bean
    fun receiver(): Receiver? {
        return Receiver()
    }

    @Bean
    fun queue(): Queue? {
        return Queue(queueName, false)
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
        return TopicExchange(topicExchangeName)
    }

    @Bean
    fun rpcExchange(): TopicExchange? {
        return TopicExchange(INVOICE_RPC_EXCHANGE)
    }


    @Bean
    fun binding(queue: Queue?, exchange: TopicExchange?): Binding? {
        return BindingBuilder.bind(queue).to(exchange).with("v.#")
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
    fun container(
        connectionFactory: ConnectionFactory?,
        listenerAdapter: MessageListenerAdapter?
    ): SimpleMessageListenerContainer? {
        val container = SimpleMessageListenerContainer()
        connectionFactory?.let {
            container.connectionFactory = it
        }
        container.setQueueNames(queueName)
        container.setMessageListener(listenerAdapter!!)
        return container
    }

    @Bean
    fun listenerAdapter(receiver: Receiver?): MessageListenerAdapter? {
        return MessageListenerAdapter(receiver, "receiveMessage")
    }

    companion object {
        const val INVOICE_QUEUE = "invoice_queue"
        const val INVOICE_REPLY_QUEUE = "invoice_reply_queue"
        const val INVOICE_RPC_EXCHANGE = "invoice_rpc_exchange"
    }
}
