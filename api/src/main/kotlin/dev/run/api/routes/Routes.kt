package dev.run.api.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.io.path.toPath


fun Application.routes() {
    routing {
        executeRoute()
    }
}

@Serializable
data class Execution(val language: String, val code: String)

fun runCommand(vararg command: String, readOutput: Boolean) {
    try {
        val process = ProcessBuilder(*command)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        if (readOutput) {
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))

            var outputLine: String?
            while ((inputStream.readLine().also { outputLine = it }) != null) {
                println(outputLine)
            }
        }

        process.waitFor(60, TimeUnit.MINUTES)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun Route.executeRoute() {
    post("/api/v1/execute") {
        val execution = call.receive<Execution>()

        val url = this.javaClass.getResource("/langimages/Python")
        if (url == null) {
            call.respond(HttpStatusCode.InternalServerError)
            return@post
        }

        call.respond(HttpStatusCode.OK)

        runCommand(
            "docker", "build", "--build-arg", "content=${execution.code}", "-t", "test", "-f", url.toURI().toPath().toString(), "" +
                    ".", readOutput = false
        )
        runCommand("docker", "run", "--rm", "--tty", "test", readOutput = true)
        runCommand("docker", "rmi", "-f", "test", readOutput = false)
    }
}