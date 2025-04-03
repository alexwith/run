package dev.run.common.util

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv {
    filename = ".env"
    ignoreIfMissing = true
}