package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoItem
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    val title: String? = null,
    val completed: Boolean? = null,
    val url: String? = null,
    val order: Int? = null
)

fun TodoItem.toDto(baseUrl: URLBuilder): TodoItemDto =
    TodoItemDto(title, completed, baseUrl.path(id.toString()).urlString(), order)

private fun URLBuilder.urlString(): String =
    when (host) {
        "localhost" -> "http://$host:$port$encodedPath" // include the port on local machine
        else -> "https://$host$encodedPath"
    }