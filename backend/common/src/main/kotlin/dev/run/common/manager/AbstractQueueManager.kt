package dev.run.common.manager

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory

abstract class AbstractQueueManager {
    protected val connection = this.createConnection()
    protected val channel: Channel = this.connection.createChannel()

    init {
        this.channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    }

    private fun createConnection(): Connection {
        val connectionFactory = ConnectionFactory()
        connectionFactory.host = "localhost"

        return connectionFactory.newConnection()
    }

    companion object {
        const val QUEUE_NAME = "execution-queue"
    }
}