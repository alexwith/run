package dev.run.common.manager

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import dev.run.common.util.dotenv

abstract class AbstractQueueManager {
    protected val connection = this.createConnection()
    protected val channel: Channel = this.connection.createChannel()

    init {
        this.channel.queueDeclare(
            QUEUE_NAME, false, false, false, mapOf(
                "x-message-ttl" to 10000
            )
        )
    }

    private fun createConnection(): Connection {
        val connectionFactory = ConnectionFactory()
        connectionFactory.host = dotenv["RABBITMQ_HOST"]

        return connectionFactory.newConnection()
    }

    companion object {
        const val QUEUE_NAME = "execution-queue"
    }
}