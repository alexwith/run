package dev.run.common.entity

import dev.run.common.enums.Language
import kotlinx.serialization.Serializable

@Serializable
open class Execution(val id: String, val language: Language, val code: String) {

    fun createImage(): String {
        return this.language.createImage(this.code)
    }
}