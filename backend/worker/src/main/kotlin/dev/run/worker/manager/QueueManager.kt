package dev.run.worker.manager

import com.rabbitmq.client.*
import org.koin.core.component.KoinComponent

class QueueManager : KoinComponent {
    private val connection = this.createConnection()

    init {
        val channel = connection.createChannel()
        channel.queueDeclare(QUEUE_NAME, false, false, false, null)
        channel.basicQos(1)

        val consumer = object : DefaultConsumer(channel) {

            override fun handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: ByteArray) {
                val message = String(body)

                println("received message: $message")

                try {
                    // TODO: run logic
                } finally {
                    channel.basicAck(envelope.deliveryTag, false)
                }
            }
        }

        channel.basicConsume(QUEUE_NAME, true, consumer)
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