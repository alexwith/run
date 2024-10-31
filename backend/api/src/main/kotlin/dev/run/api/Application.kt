package dev.run.api

import dev.run.api.routes.routes
import dev.run.worker.manager.QueueManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.websocket.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(CORS) {
        allowHost("localhost:5173")
        allowHeader(HttpHeaders.ContentType)
    }

    install(WebSockets)

    install(ContentNegotiation) {
        json()
    }

    installKoin(this)

    routes()
}

fun installKoin(app: Application) {
    app.install(Koin) {
        modules(
            module {
                single { QueueManager() }
            }
        )
    }
}