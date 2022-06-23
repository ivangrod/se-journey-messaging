package org.ivangrod.rabbitmq_receiver.infrastructure.messaging

import org.ivangrod.rabbitmq_receiver.infrastructure.messaging.config.MessagingRabbitMQConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.connection.CorrelationData
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class DownloadInvoiceReplier {

    @Autowired
    private val rabbitTemplate: RabbitTemplate? = null

    @RabbitListener(queues = [MessagingRabbitMQConfiguration.INVOICE_QUEUE])
    fun process(msg: Message) {

        logger.info("Server receive : {}", msg.toString())

        val response: Message = MessageBuilder
            .withBody(("invoices_url:[/${String(msg.body)}/invoices]").toByteArray())
            .build()
        val correlationData = CorrelationData(msg.messageProperties.correlationId)

        rabbitTemplate!!.sendAndReceive(
            MessagingRabbitMQConfiguration.INVOICE_RPC_EXCHANGE,
            MessagingRabbitMQConfiguration.INVOICE_REPLY_QUEUE,
            response,
            correlationData
        )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(DownloadInvoiceReplier::class.java)
    }
}
