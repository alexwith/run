package dev.run.worker

import dev.run.worker.manager.DockerManager
import dev.run.worker.manager.ExecutionManager
import dev.run.worker.manager.QueueManager
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

    QueueManager()
}

fun installKoin(app: Application) {
    app.install(Koin) {
        modules(
            module {
                single { DockerManager() }
                single { ExecutionManager() }
            }
        )
    }
}