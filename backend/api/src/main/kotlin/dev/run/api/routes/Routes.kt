package dev.run.api.routes

import dev.run.api.manager.execution.ExecutionManager
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

fun Application.routes() {
    routing {
        execute()
    }
}

fun Route.execute() {
    val executionManager by inject<ExecutionManager>()

    webSocket("/socket/v1/execute") {
        val language = this.call.parameters["language"]
        if (language == null) {
            return@webSocket
        }

        executionManager.execute(language, this)
    }
}