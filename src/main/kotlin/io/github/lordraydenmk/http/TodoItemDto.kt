package io.github.lordraydenmk.http

import io.github.lordraydenmk.domain.TodoItem
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    val id: String? = null,
    val title: String? = null,
    val completed: Boolean? = null,
    val url: String? = null,
    val order: Int? = null
)

fun TodoItem.toDto(): TodoItemDto = TodoItemDto(id.id.toString(), title, completed, url, order)