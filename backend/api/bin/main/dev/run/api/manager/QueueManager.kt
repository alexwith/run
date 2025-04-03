package dev.run.api.manager

import dev.run.api.manager.execution.entity.ApiExecution
import dev.run.common.manager.AbstractQueueManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class QueueManager : KoinComponent, AbstractQueueManager() {

    fun enqueue(execution: ApiExecution) {
        val json = Json.encodeToString(execution.toRawExecution())
        this.channel.basicPublish("", QUEUE_NAME, null, json.toByteArray())
    }
}