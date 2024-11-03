package dev.run.worker.manager

import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dev.run.common.entity.Execution
import dev.run.common.manager.AbstractQueueManager
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QueueManager : KoinComponent, AbstractQueueManager() {

    init {
        this.initConsumer()
    }

    private fun initConsumer() {
        val executionManager by inject<ExecutionManager>()

        val consumer = DeliverCallback { _, delivery: Delivery ->
            val json = String(delivery.body, charset("UTF-8"))
            val execution = Json.decodeFromString<Execution>(json)
            executionManager.execute(execution)
        }

        this.channel.basicConsume(QUEUE_NAME, true, consumer) { _ -> }
    }
}