package dev.run.common.entity

import kotlinx.serialization.Serializable

@Serializable
class Execution(val id: String, val image: String, val code: String)