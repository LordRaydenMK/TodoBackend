package io.github.lordraydenmk.domain

import java.util.*

inline class TodoId(val id: UUID)

data class TodoItem(
    val id: TodoId,
    val title: String,
    val completed: Boolean = false,
    val order: Int
)

fun TodoItem.patch(patch: PatchTodo): TodoItem =
    TodoItem(
        id = patch.id ?: id,
        title = patch.title ?: title,
        completed = patch.completed ?: completed,
        order = patch.order ?: order
    )

data class PatchTodo(
    val id: TodoId?,
    val title: String?,
    val completed: Boolean?,
    val url: String?,
    val order: Int?
)