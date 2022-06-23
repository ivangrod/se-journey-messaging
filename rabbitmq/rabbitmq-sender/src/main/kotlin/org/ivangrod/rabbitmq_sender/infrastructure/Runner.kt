package org.ivangrod.rabbitmq_sender.infrastructure

import org.ivangrod.rabbitmq_sender.infrastructure.messaging.Sender
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class Runner(sender: Sender) : CommandLineRunner {
    private val sender: Sender

    init {
        this.sender = sender
    }

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        println("Sending message...")
        sender.send("Hello", "Hello Butcher")
    }
}
