package org.ivangrod.rabbitmq_sender.infrastructure

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RabbitMQReceiverApplication

fun main(args: Array<String>) {
    SpringApplication.run(RabbitMQReceiverApplication::class.java, *args)
}
