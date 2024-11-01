package dev.run.common.entity

import kotlinx.serialization.Serializable

@Serializable
open class Execution(val id: String, val language: String, val code: String)