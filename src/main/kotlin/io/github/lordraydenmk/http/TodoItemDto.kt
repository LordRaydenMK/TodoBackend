package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoItem
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    val id: String? = null,
    val title: String? = null,
    val completed: Boolean? = null,
    val url: String? = null,
    val order: Int? = null
)

fun TodoItem.toDto(baseUrl: URLBuilder): TodoItemDto =
    TodoItemDto(id.id.toString(), title, completed, baseUrl.path(id.id.toString()).urlString(), order)

private fun URLBuilder.urlString(): String =
    when (protocol.defaultPort) {
        port -> "${protocol.name}://$host$encodedPath"
        else -> "${protocol.name}://$host:$port$encodedPath"
    }