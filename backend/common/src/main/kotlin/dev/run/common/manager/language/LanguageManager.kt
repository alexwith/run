package dev.run.common.manager.language

import dev.run.common.manager.language.entity.Language
import java.io.File

class LanguageManager {
    private val languages = HashMap<String, Language>()

    init {
        val url = this.javaClass.getResource("/langimages") ?: throw NoLanguagesException()

        val directory = File(url.toURI())
        val files = directory.listFiles()
        if (files == null || files.isEmpty()) {
            throw NoLanguagesException()
        }

        for (file in files) {
            if (!file.isFile) {
                continue
            }

            val name = file.name
            languages[name] = Language(name, file.toPath())
        }
    }

    fun getLanguage(name: String): Language? {
        return this.languages[name]
    }

    private class NoLanguagesException : RuntimeException("Was unable to find any language dockerfiles!")
}