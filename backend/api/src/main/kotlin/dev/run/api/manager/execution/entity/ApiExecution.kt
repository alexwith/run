package dev.run.api.manager.execution.entity

import dev.run.common.entity.Execution
import io.ktor.websocket.*
import java.util.concurrent.CompletableFuture

class ApiExecution(val id: String, val language: String, val code: String, val socket: WebSocketSession) {
    val socketFuture = CompletableFuture<Unit>()

    fun block() {
        this.socketFuture.join()
    }

    fun toRawExecution(): Execution {
        return Execution(this.id, this.language, this.code)
    }
}