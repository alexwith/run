package dev.run.worker

import dev.run.worker.manager.DockerManager
import dev.run.worker.manager.ExecutionManager
import dev.run.worker.manager.QueueManager
import dev.run.worker.routes.routes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

// docker build -f ./worker/Dockerfile -t run-worker .
// docker run -d -p 8081:8081 -p 8083:8083 -v /var/run/docker.sock:/var/run/docker.sock --network=run --name run-worker run-worker
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
                single { ExecutionManager() }
            }
        )
    }
}