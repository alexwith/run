package dev.run.worker.manager

import dev.run.common.entity.Execution
import dev.run.common.manager.language.entity.Language
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class DockerManager {

    fun buildImage(execution: Execution, language: Language) {
        this.runCommand(
            "docker",
            "build",
            "--build-arg",
            "content=${execution.code}",
            "-t",
            execution.id,
            "-f",
            language.dockerfile.toString(),
            "."
        )
    }

    fun deleteImage(execution: Execution) {
        this.runCommand(
            "docker",
            "rmi",
            "-f",
            execution.id
        )
    }

    fun runContainer(execution: Execution, outputConsumer: (outputLine: String) -> Unit) {
        runCommand("docker", "run", "--rm", "--tty", execution.id) { process ->
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))

            var outputLine: String?
            while ((inputStream.readLine().also { outputLine = it }) != null) {
                outputConsumer(outputLine!!)
            }
        }
    }

    private fun runCommand(vararg command: String, processConsumer: (process: Process) -> Unit = {}) {
        try {
            val process = ProcessBuilder(*command)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            processConsumer(process)

            process.waitFor(60, TimeUnit.MINUTES)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}