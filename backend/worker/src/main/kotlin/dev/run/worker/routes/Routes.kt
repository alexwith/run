package dev.run.worker.routes

import dev.run.worker.manager.QueueManager
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.routes() {
    val queueManager by inject<QueueManager>()
    println(queueManager)
}