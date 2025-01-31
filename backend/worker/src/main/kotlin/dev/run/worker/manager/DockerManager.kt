package dev.run.worker.manager

import dev.run.common.entity.EmptyUnitContinuation
import dev.run.common.entity.Execution
import dev.run.common.manager.language.entity.Language
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import kotlin.coroutines.startCoroutine

class DockerManager {

    fun buildImage(execution: Execution, language: Language): Boolean {
        return this.runCommand(
            "docker",
            "build",
            "--network=none",
            "--build-arg",
            "content=${execution.code}",
            "-t",
            execution.id,
            "-f",
            language.dockerfile.toString(),
            "."
        ) { process ->
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
            "docker",
            "rmi",
            "-f",
            execution.id
        )
    }

    fun runContainer(execution: Execution, outputConsumer: suspend (outputLine: String) -> Unit) {
        this.runCommand(
            "docker",
            "run",
            "--rm",
            "--tty",
            "--ulimit",
            "cpu=1",
            "--memory=10m",
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

    private fun <T> runCommand(vararg command: String, processConsumer: (process: Process) -> T? = { null }): T? {
        try {
            val process = ProcessBuilder(*command)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            val result = processConsumer(process)

            process.waitFor(60, TimeUnit.MINUTES)

            return result
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}