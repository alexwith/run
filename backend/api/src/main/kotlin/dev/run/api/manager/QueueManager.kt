package dev.run.api.manager

import dev.run.common.entity.Execution
import dev.run.common.manager.AbstractQueueManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QueueManager : AbstractQueueManager() {

    fun enqueue(execution: Execution) {
        val json = Json.encodeToString(execution)
        this.channel.basicPublish("", QUEUE_NAME, null, json.toByteArray())
    }
}