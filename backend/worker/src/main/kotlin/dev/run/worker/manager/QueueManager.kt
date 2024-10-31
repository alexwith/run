package dev.run.worker.manager

import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dev.run.common.entity.Execution
import dev.run.common.manager.AbstractQueueManager
import kotlinx.serialization.json.Json

class QueueManager : AbstractQueueManager() {

    init {
        this.initConsumer()
    }

    private fun initConsumer() {
        val deliverCallback = DeliverCallback { _, delivery: Delivery ->
            val json = String(delivery.body, charset("UTF-8"))
            val execution = Json.decodeFromString<Execution>(json)

            println("execution: ${execution.image}")
        }

        this.channel.basicConsume(QUEUE_NAME, true, deliverCallback) { _ -> }
    }
}