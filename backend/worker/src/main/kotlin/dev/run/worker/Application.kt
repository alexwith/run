package dev.run.worker

import dev.run.common.manager.language.LanguageManager
import dev.run.worker.manager.DockerManager
import dev.run.worker.manager.QueueManager
import dev.run.worker.routes.routes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    installKoin(this)

    routes()
}

fun installKoin(app: Application) {
    app.install(Koin) {
        modules(
            module {
                single { QueueManager() }
                single { DockerManager() }
                single { LanguageManager() }
            }
        )
    }
}