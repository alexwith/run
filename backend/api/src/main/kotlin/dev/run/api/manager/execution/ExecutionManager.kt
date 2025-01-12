package dev.run.api.manager.execution

import dev.run.api.manager.QueueManager
import dev.run.api.manager.execution.entity.ApiExecution
import dev.run.common.manager.language.LanguageManager
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ExecutionManager : KoinComponent {
    private val executions = HashMap<String, ApiExecution>()
    private val queueManager by inject<QueueManager>()
    private val languageManager by inject<LanguageManager>()

    init {
        this.workerConnectionListener()
    }

    suspend fun execute(language: String, socket: WebSocketSession) {
        socket.send("run:ready")

        var code: String? = null
        for (frame in socket.incoming) {
            frame as? Frame.Text ?: continue
            code = frame.readText()
            break
        }

        if (code == null) {
            return
        }

        if (languageManager.getLanguage(language) == null) {
            return
        }

        val id = UUID.randomUUID().toString()
        val execution = ApiExecution(id, language, code, socket)
        this.executions[id] = execution
        this.queueManager.enqueue(execution)

        execution.await()
    }

    private fun workerConnectionListener() {
        val serverSocket = aSocket(SelectorManager(Dispatchers.IO)).tcp().bind("127.0.0.1", 8083)

        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val socket = serverSocket.accept()
                launch {
                    val readChannel = socket.openReadChannel()
                    val executionId = readChannel.readUTF8Line()
                    val execution = this@ExecutionManager.executions[executionId!!.split(":")[1]]
                    if (execution == null) {
                        socket.close()
                        return@launch
                    }

                    this@ExecutionManager.listenToWorker(socket, readChannel, execution)
                }
            }
        }
    }

    private suspend fun listenToWorker(worker: Socket, channel: ByteReadChannel, execution: ApiExecution) {
        while (true) {
            val name = channel.readUTF8Line()
            val args = name?.split(":")
            if (name == null || args == null || args[0] != "run") {
                worker.close()
                execution.socketFuture.complete(null)
                break
            }

            val executionSocket = execution.socket

            val action = args[1]
            when (action) {
                "building" -> executionSocket.send("run:building")
                "running" -> executionSocket.send("run:running")
                "failed" -> executionSocket.send("run:failed")
                "executed" -> executionSocket.send("run:executed")
                "output" -> {
                    val lastProtocolColonIndex = name.indexOf(":", 4) + 1
                    val outputLine = name.substring(lastProtocolColonIndex)
                    executionSocket.send(outputLine)
                }
            }
        }
    }
}