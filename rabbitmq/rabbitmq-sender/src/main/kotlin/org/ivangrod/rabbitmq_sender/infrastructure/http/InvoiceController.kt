package org.ivangrod.rabbitmq_sender.infrastructure.http

import org.ivangrod.rabbitmq_sender.infrastructure.messaging.config.MessagingRabbitMQConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageBuilder
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/api")
class InvoiceController {

    @Autowired
    private val rabbitTemplate: RabbitTemplate? = null

    @GetMapping("/user/{userId}/invoices")
    fun downloadInvoices(@PathVariable userId: String): String {

        val newMessage: Message = MessageBuilder.withBody(userId.toByteArray()).build()
        logger.info("Requester send：{}", newMessage)

        val result: Message? =
            rabbitTemplate?.sendAndReceive(
                MessagingRabbitMQConfiguration.INVOICE_RPC_EXCHANGE,
                MessagingRabbitMQConfiguration.INVOICE_QUEUE,
                newMessage
            )

        result.let { msgResult ->

            // If we don’t configure correlated in application.properties, there will be no correlation_id in the
            // sent message,
            val correlationId: String? = newMessage.messageProperties?.correlationId
                logger.info(" [correlation-id]: $correlationId ")

                val headers = msgResult?.messageProperties?.headers as HashMap<String, Any>
                return if (headers["spring_returned_message_correlation"] as String? == correlationId)
                    result?.body?.let { String(it) }.toString() else ""
            
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(InvoiceController::class.java)
    }
}
