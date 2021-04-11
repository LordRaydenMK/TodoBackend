package io.github.lordraydenmk.domain

import java.util.*

typealias TodoId = UUID

data class TodoItem(
    val id: TodoId,
    val title: String,
    val completed: Boolean = false,
    val order: Int? = null
)

fun TodoItem(id: TodoId, title: String, completed: Boolean?, order: Int?): TodoItem =
    TodoItem(id, title, completed ?: false, order)

fun TodoItem.patch(patch: PatchTodo): TodoItem =
    copy(
        title = patch.title ?: title,
        completed = patch.completed ?: completed,
        order = patch.order ?: order
    )

data class PatchTodo(
    val title: String?,
    val completed: Boolean?,
    val order: Int?
)