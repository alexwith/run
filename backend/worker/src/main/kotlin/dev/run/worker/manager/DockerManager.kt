package dev.run.worker.manager

import dev.run.common.entity.EmptyUnitContinuation
import dev.run.common.entity.Execution
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.coroutines.startCoroutine

class DockerManager {

    fun buildImage(execution: Execution): Boolean {
        return this.runCommand(
            execution,
            "docker",
            "build",
            "--network=none",
            "-t",
            execution.id,
            "-"
        ) { process ->
            process.outputStream.bufferedWriter().use { writer ->
                writer.write(execution.createImage())
            }

            val infoStream = BufferedReader(InputStreamReader(process.errorStream))

            var outputLine: String?
            while ((infoStream.readLine().also { outputLine = it }) != null) {
                if (outputLine!!.contains("ERROR")) {
                    return@runCommand false
                }
            }

            return@runCommand true
        }!!
    }

    fun deleteImage(execution: Execution) {
        this.runCommand<Unit>(
            execution,
            "docker",
            "rmi",
            "-f",
            execution.id
        )
    }

    fun runContainer(execution: Execution, outputConsumer: suspend (outputLine: String) -> Unit) {
        val language = execution.language

        this.runCommand(
            execution,
            "timeout",
            "--signal=SIGKILL",
            "${language.timeoutSeconds}",
            "docker",
            "run",
            "--rm",
            "--tty",
            "--ulimit",
            "cpu=${language.processLimit}",
            "--memory=${language.memoryLimit}",
            "--pids-limit=64",
            "--cap-drop=ALL",
            "--cpus=0.5",
            execution.id
        ) { process ->
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))
            val errorStream = BufferedReader(InputStreamReader(process.errorStream))

            var outputLine: String?
            while ((inputStream.readLine().also { outputLine = it }) != null) {
                outputConsumer.startCoroutine(outputLine!!, EmptyUnitContinuation)
            }

            while ((errorStream.readLine().also { outputLine = it }) != null) {
                outputConsumer.startCoroutine(outputLine!!, EmptyUnitContinuation)
            }
        }
    }

    private fun <T> runCommand(
        execution: Execution,
        vararg command: String,
        processConsumer: (process: Process) -> T? = { null }
    ): T? {
        try {
            val process = ProcessBuilder(*command)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            val result = processConsumer(process)

            process.waitFor(execution.language.timeoutSeconds.toLong(), TimeUnit.SECONDS)

            return result
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}