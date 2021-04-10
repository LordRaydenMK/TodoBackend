package io.github.lordraydenmk.http

import java.util.*

fun String.toUUIDOrNull(): UUID? =
    try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }