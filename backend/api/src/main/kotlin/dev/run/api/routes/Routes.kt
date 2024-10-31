package dev.run.api.routes

import dev.run.common.entity.Execution
import dev.run.api.manager.QueueManager
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.routes() {
    routing {
        execute()
    }
}

fun Route.execute() {
    val queueManager by inject<QueueManager>()

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

        val url = this.javaClass.getResource("/langimages/$language")
        if (url == null) {
            return@webSocket
        }

        val id = UUID.randomUUID().toString()
        queueManager.enqueue(Execution(id, language, code))
        /*this.send("run:building")

        runCommand(
            "docker", "build", "--build-arg", "content=${code}", "-t", "test", "-f", url.toURI().toPath().toString(), "" +
                    ".", socket = null
        )

        this.send("run:running")

        runCommand("docker", "run", "--rm", "--tty", "test", socket = this)
        runCommand("docker", "rmi", "-f", "test", socket = null)*/
    }
}

fun runCommand(vararg command: String, socket: WebSocketSession?) {
    try {
        val process = ProcessBuilder(*command)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        println(command.joinToString(" "))

        if (socket != null) {
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))

            var outputLine: String?
            while ((inputStream.readLine().also { outputLine = it }) != null) {
                val ting = outputLine
                runBlocking {
                    launch {
                        socket.send(ting!!)
                    }
                }
            }
        }

        process.waitFor(60, TimeUnit.MINUTES)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}