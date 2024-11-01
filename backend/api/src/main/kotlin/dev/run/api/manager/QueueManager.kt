package dev.run.api.manager

import dev.run.api.manager.execution.ExecutionManager
import dev.run.api.manager.execution.entity.ApiExecution
import dev.run.common.manager.AbstractQueueManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QueueManager : KoinComponent, AbstractQueueManager() {
    private val executionManager by inject<ExecutionManager>()

    fun enqueue(execution: ApiExecution) {
        this.executionManager.registerExecution(execution)

        val json = Json.encodeToString(execution.toRawExecution())
        this.channel.basicPublish("", QUEUE_NAME, null, json.toByteArray())

        execution.block()
    }
}