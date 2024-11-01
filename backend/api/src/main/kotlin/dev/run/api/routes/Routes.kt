package dev.run.api.routes

import dev.run.api.manager.QueueManager
import dev.run.api.manager.execution.entity.ApiExecution
import dev.run.common.manager.language.LanguageManager
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import java.util.*

fun Application.routes() {
    routing {
        execute()
    }
}

fun Route.execute() {
    val queueManager by inject<QueueManager>()
    val languageManager by inject<LanguageManager>()

    webSocket("/socket/v1/execute") {
        val language = this.call.parameters["language"]
        if (language == null) {
            return@webSocket
        }

        this.send("run:ready")

        var code: String? = null
        for (frame in this.incoming) {
            frame as? Frame.Text ?: continue
            code = frame.readText()
            break
        }

        if (code == null) {
            return@webSocket
        }

        if (languageManager.getLanguage(language) == null) {
            return@webSocket
        }

        val id = UUID.randomUUID().toString()
        queueManager.enqueue(ApiExecution(id, language, code, this))
    }
}