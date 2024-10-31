package dev.run.worker.manager

import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import dev.run.api.entity.Execution
import org.koin.core.component.KoinComponent

class QueueManager : KoinComponent {
    val connection = this.createConnection()

    fun enqueue(execution: Execution) {
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        channel.basicPublish("", QUEUE_NAME, null, "hello".toByteArray())
        println("sent message")
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