package dev.run.execution.routes

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

fun runCommand(vararg command: String, readOutput: Boolean): String? {
    try {
        val process = ProcessBuilder(*command)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        if (readOutput) {
            val stdInput = BufferedReader(InputStreamReader(process.inputStream))
            val stdError = BufferedReader(InputStreamReader(process.errorStream))

            var s: String? = null
            while ((stdInput.readLine().also { s = it }) != null) {
                println(s)
            }

            while ((stdError.readLine().also { s = it }) != null) {
                println(s)
            }
        }

        process.waitFor(60, TimeUnit.MINUTES)
        return null
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
}

fun Route.executeRoute() {
    post("/api/v1/execute") {
        val execution = call.receive<Execution>()

        val url = this.javaClass.getResource("/langimages/Python")

        runCommand("docker", "build", "--build-arg", "content=${execution.code}", "-t", "test", "-f", url.toURI().toPath().toString(), "" +
                ".", readOutput = false)
        runCommand("docker", "run", "--rm", "test", readOutput = true)
        runCommand("docker", "rmi", "-f", "test", readOutput = false)

        call.respondText("Hello World!")
    }
}