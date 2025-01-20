package dev.run.worker.manager

import dev.run.common.entity.Execution
import dev.run.common.manager.language.LanguageManager
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExecutionManager : KoinComponent {
    private val dockerManager by inject<DockerManager>()
    private val languageManager by inject<LanguageManager>()

    fun execute(execution: Execution) {
        val language = this.languageManager.getLanguage(execution.language)
        if (language == null) {
            return
        }

        openSocket { channel ->
            channel.writeStringUtf8("run:${execution.id}\n")

            channel.writeStringUtf8("run:building\n")

            val success = this@ExecutionManager.dockerManager.buildImage(execution, language)
            if (!success) {
                channel.writeStringUtf8("run:failed\n")
                return@openSocket
            }

            channel.writeStringUtf8("run:running\n")

            this@ExecutionManager.dockerManager.runContainer(execution) { output ->
                channel.writeStringUtf8("run:output:$output\n")
            }

            this@ExecutionManager.dockerManager.deleteImage(execution)

            channel.writeStringUtf8("run:executed\n")
        }
    }

    private fun openSocket(consumer: suspend (channel: ByteWriteChannel) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val socket = SOCKET_BUILDER.connect("127.0.0.1", 8083)
            val sendChannel = socket.openWriteChannel(true)
            consumer(sendChannel)
            socket.close()
        }
    }

    companion object {
        private val SOCKET_BUILDER = aSocket(SelectorManager(Dispatchers.IO)).tcp()
    }
}