package dev.run.api.manager.execution

import dev.run.api.manager.execution.entity.ApiExecution
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExecutionManager {
    private val executions = HashMap<String, ApiExecution>()

    init {
        val serverSocket = aSocket(SelectorManager(Dispatchers.IO)).tcp().bind("127.0.0.1", 8083)

        GlobalScope.launch {
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

                    while (true) {
                        val name = readChannel.readUTF8Line()
                        val args = name?.split(":")
                        if (name == null || args == null || args[0] != "run") {
                            socket.close()
                            execution.socketFuture.complete(null)
                            break
                        }

                        val executionSocket = execution.socket

                        val action = args[1]
                        when (action) {
                            "building" -> executionSocket.send("run:building")
                            "running" -> executionSocket.send("run:running")
                            "output" -> {
                                val outputLine = args[2]
                                executionSocket.send(outputLine)
                            }
                        }
                    }
                }
            }
        }
    }

    fun registerExecution(execution: ApiExecution) {
        this.executions[execution.id] = execution
    }
}