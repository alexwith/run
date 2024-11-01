package dev.run.worker.manager

import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import dev.run.common.entity.Execution
import dev.run.common.manager.AbstractQueueManager
import dev.run.common.manager.language.LanguageManager
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QueueManager : KoinComponent, AbstractQueueManager() {

    init {
        this.initConsumer()
    }

    private fun initConsumer() {
        val languageManager by inject<LanguageManager>()
        val dockerManager by inject<DockerManager>()

        val socketBuilder = aSocket(SelectorManager(Dispatchers.IO)).tcp()

        val consumer = DeliverCallback { _, delivery: Delivery ->
            val json = String(delivery.body, charset("UTF-8"))
            val execution = Json.decodeFromString<Execution>(json)

            val language = languageManager.getLanguage(execution.language)
            if (language == null) {
                return@DeliverCallback
            }

            GlobalScope.launch {
                val socket = socketBuilder.connect("127.0.0.1", 8083)
                val sendChannel = socket.openWriteChannel(true)

                sendChannel.writeStringUtf8("run:${execution.id}\n")
                sendChannel.writeStringUtf8("run:building\n")

                dockerManager.buildImage(execution, language)

                sendChannel.writeStringUtf8("run:running\n")

                dockerManager.runContainer(execution) { output ->
                    GlobalScope.launch {
                        sendChannel.writeStringUtf8("run:output:$output\n")
                    }
                }

                dockerManager.deleteImage(execution)

                socket.close()
            }
        }

        this.channel.basicConsume(QUEUE_NAME, true, consumer) { _ -> }
    }
}