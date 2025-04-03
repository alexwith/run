package dev.run.api.manager.execution.entity

import dev.run.common.entity.Execution
import dev.run.common.enums.Language
import io.ktor.websocket.*
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture

class ApiExecution(val id: String, val language: Language, val code: String, val socket: WebSocketSession) {
    val socketFuture = CompletableFuture<Unit>()

    suspend fun await() {
        this.socketFuture.await()
    }

    fun toRawExecution(): Execution {
        return Execution(this.id, this.language, this.code)
    }
}